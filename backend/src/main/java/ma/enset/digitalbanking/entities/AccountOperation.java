package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.digitalbanking.enums.OperationType;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    private BankAccount bankAccount;
    private String description;

    // --- Audit: identifiant de l'utilisateur authentifié qui a effectué l'opération ---
    private String performedBy;
}
