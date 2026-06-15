package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppRole {
    @Id
    private String role;   // e.g. "USER", "ADMIN"
}
