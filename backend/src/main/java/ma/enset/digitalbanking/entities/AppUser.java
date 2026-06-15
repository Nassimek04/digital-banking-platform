package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {
    @Id
    private String userId;          // UUID
    @Column(unique = true)
    private String username;
    private String password;        // BCrypt-encoded
    private String email;
    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private List<AppRole> roles = new ArrayList<>();
}
