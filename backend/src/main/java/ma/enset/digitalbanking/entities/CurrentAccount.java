package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("CURR")
@Data @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrentAccount extends BankAccount {
    private double overDraft;   // découvert autorisé
}
