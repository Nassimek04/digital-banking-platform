package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.digitalbanking.enums.AccountStatus;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;               // UUID
    private double balance;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AccountOperation> accountOperations;

    // --- Audit ---
    private String createdBy;
}
