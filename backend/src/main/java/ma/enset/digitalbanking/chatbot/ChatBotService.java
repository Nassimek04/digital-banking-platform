package ma.enset.digitalbanking.chatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Core RAG logic: retrieve relevant documents from the vector store,
 * then augment the LLM prompt with that context before generation.
 */
@Service
@Slf4j
public class ChatBotService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    private static final String SYSTEM_TEMPLATE = """
            You are a helpful banking assistant for the "Digital Banking" application.
            Answer the user question using ONLY the context below. If the context does not
            contain the answer, say you don't have that information.

            CONTEXT:
            %s
            """;

    public ChatBotService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        List<Document> similar = vectorStore.similaritySearch(
                SearchRequest.query(question).withTopK(4));
        String context = similar.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        return chatClient.prompt()
                .system(SYSTEM_TEMPLATE.formatted(context))
                .user(question)
                .call()
                .content();
    }
}
