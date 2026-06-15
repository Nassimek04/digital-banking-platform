package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SAV")
@Data @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SavingAccount extends BankAccount {
    private double interestRate;   // taux d'intérêt
}
