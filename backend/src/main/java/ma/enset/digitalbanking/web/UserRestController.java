package ma.enset.digitalbanking.web;

import lombok.AllArgsConstructor;
import ma.enset.digitalbanking.entities.AppUser;
import ma.enset.digitalbanking.security.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class UserRestController {

    private final AccountService accountService;

    @GetMapping
    public List<AppUser> users() {
        return accountService.listUsers();
    }

    @PostMapping("/{username}/roles/{role}")
    public void addRole(@PathVariable String username, @PathVariable String role) {
        accountService.addNewRole(role);
        accountService.addRoleToUser(username, role);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public void removeRole(@PathVariable String username, @PathVariable String role) {
        accountService.removeRoleFromUser(username, role);
    }
}
