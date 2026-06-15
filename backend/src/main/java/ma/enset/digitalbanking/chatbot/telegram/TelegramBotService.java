package ma.enset.digitalbanking.chatbot.telegram;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking.chatbot.ChatBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Lightweight Telegram client using the Bot API via long-polling (getUpdates).
 * Set telegram.bot.token in application.properties (or as an env variable) to enable it.
 *
 * Talk to @BotFather on Telegram to create a bot and obtain the token.
 */
@Service
@Slf4j
public class TelegramBotService {

    private final ChatBotService chatBotService;
    private final RestClient restClient = RestClient.create();

    @Value("${telegram.bot.token:}")
    private String botToken;

    private long lastUpdateId = 0;
    private boolean enabled = false;

    public TelegramBotService(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostConstruct
    public void init() {
        enabled = botToken != null && !botToken.isBlank();
        if (enabled) log.info("Telegram bot enabled (long-polling).");
        else log.info("Telegram bot disabled (no telegram.bot.token).");
    }

    @Scheduled(fixedDelay = 3000)
    @SuppressWarnings("unchecked")
    public void poll() {
        if (!enabled) return;
        try {
            String url = "https://api.telegram.org/bot%s/getUpdates?offset=%d&timeout=2"
                    .formatted(botToken, lastUpdateId + 1);
            Map<String, Object> response = restClient.get().uri(url).retrieve().body(Map.class);
            if (response == null) return;
            List<Map<String, Object>> updates = (List<Map<String, Object>>) response.get("result");
            if (updates == null) return;

            for (Map<String, Object> update : updates) {
                lastUpdateId = ((Number) update.get("update_id")).longValue();
                Map<String, Object> message = (Map<String, Object>) update.get("message");
                if (message == null) continue;
                Map<String, Object> chat = (Map<String, Object>) message.get("chat");
                long chatId = ((Number) chat.get("id")).longValue();
                String text = (String) message.get("text");
                if (text == null) continue;

                String answer = chatBotService.ask(text);
                sendMessage(chatId, answer);
            }
        } catch (Exception e) {
            log.warn("Telegram poll error: {}", e.getMessage());
        }
    }

    private void sendMessage(long chatId, String text) {
        try {
            restClient.post()
                    .uri("https://api.telegram.org/bot%s/sendMessage".formatted(botToken))
                    .body(Map.of("chat_id", chatId, "text", text))
                    .retrieve().toBodilessEntity();
        } catch (Exception e) {
            log.warn("Telegram send error: {}", e.getMessage());
        }
    }
}
