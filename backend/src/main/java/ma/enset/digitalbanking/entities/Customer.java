package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BankAccount> bankAccounts;

    // --- Audit: who created/last modified this record (username of authenticated user) ---
    private String createdBy;
    private String lastModifiedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastModifiedAt;
}
