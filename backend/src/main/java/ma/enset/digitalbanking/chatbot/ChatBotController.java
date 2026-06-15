package ma.enset.digitalbanking.chatbot;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoint consumed by the Angular chat widget (Part 5: integration).
 */
@RestController
@RequestMapping("/chatbot")
@AllArgsConstructor
@CrossOrigin("*")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> body) {
        String question = body.getOrDefault("message", "");
        return Map.of("response", chatBotService.ask(question));
    }
}
