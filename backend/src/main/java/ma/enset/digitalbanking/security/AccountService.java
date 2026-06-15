package ma.enset.digitalbanking.security;

import ma.enset.digitalbanking.entities.AppRole;
import ma.enset.digitalbanking.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);
    AppUser loadUserByUsername(String username);
    void changePassword(String username, String oldPassword, String newPassword);
    java.util.List<AppUser> listUsers();
}
