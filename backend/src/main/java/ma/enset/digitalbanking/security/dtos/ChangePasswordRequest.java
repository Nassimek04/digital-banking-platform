package ma.enset.digitalbanking.security.dtos;
import lombok.Data;
@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
