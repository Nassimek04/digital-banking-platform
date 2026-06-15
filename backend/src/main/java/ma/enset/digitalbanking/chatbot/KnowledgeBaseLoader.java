package ma.enset.digitalbanking.chatbot;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking.entities.BankAccount;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads the knowledge base into the vector store at startup:
 *  - a static banking FAQ (the "documents" of the RAG system)
 *  - dynamic facts derived from the actual bank data (customers / accounts).
 */
@Component
@AllArgsConstructor
@Slf4j
public class KnowledgeBaseLoader {

    private final VectorStore vectorStore;
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;

    @PostConstruct
    public void load() {
        try {
            List<Document> docs = new ArrayList<>();

            // --- Static FAQ knowledge ---
            docs.add(new Document("A current account (compte courant) allows an authorized overdraft (overDraft). It is meant for daily transactions."));
            docs.add(new Document("A saving account (compte epargne) earns interest based on an interestRate field. It is meant for storing money over time."));
            docs.add(new Document("An operation of type CREDIT increases the account balance. An operation of type DEBIT decreases it."));
            docs.add(new Document("A transfer moves money from a source account (debit) to a destination account (credit)."));
            docs.add(new Document("To open an account, a customer must already exist in the system. Each account belongs to exactly one customer."));
            docs.add(new Document("Account statuses are CREATED, ACTIVATED, and SUSPENDED."));

            // --- Dynamic facts from the database ---
            customerRepository.findAll().forEach(c ->
                docs.add(new Document("Customer #%d is named %s with email %s."
                        .formatted(c.getId(), c.getName(), c.getEmail()))));

            for (BankAccount a : bankAccountRepository.findAll()) {
                String owner = a.getCustomer() != null ? a.getCustomer().getName() : "unknown";
                docs.add(new Document("Account %s belongs to %s and has a balance of %.2f %s."
                        .formatted(a.getId(), owner, a.getBalance(), a.getCurrency())));
            }

            vectorStore.add(docs);
            log.info("Knowledge base loaded into vector store: {} documents", docs.size());
        } catch (Exception e) {
            log.warn("Could not load knowledge base (is the OpenAI key configured?): {}", e.getMessage());
        }
    }
}
