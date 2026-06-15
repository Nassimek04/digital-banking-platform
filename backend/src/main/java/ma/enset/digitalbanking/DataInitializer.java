package ma.enset.digitalbanking;

import lombok.AllArgsConstructor;
import ma.enset.digitalbanking.dtos.CustomerDTO;
import ma.enset.digitalbanking.entities.AppUser;
import ma.enset.digitalbanking.security.AccountService;
import ma.enset.digitalbanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds users, customers, accounts and operations on startup.
 * This also serves as a simple "DAO layer test" (Part 4 of the brief).
 */
@Configuration
@AllArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(AccountService accountService) {
        return args -> {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");

            if (accountService.loadUserByUsername("admin") == null) {
                AppUser admin = accountService.addNewUser("admin", "admin123", "admin@bank.ma", "admin123");
                accountService.addRoleToUser("admin", "USER");
                accountService.addRoleToUser("admin", "ADMIN");
            }
            if (accountService.loadUserByUsername("user1") == null) {
                accountService.addNewUser("user1", "user123", "user1@bank.ma", "user123");
                accountService.addRoleToUser("user1", "USER");
            }
        };
    }

    @Bean
    CommandLineRunner initData(BankAccountService bankAccountService) {
        return args -> {
            String[] names = {"Hassan", "Yassine", "Aicha", "Mohamed", "Fatima"};
            for (String name : names) {
                CustomerDTO c = new CustomerDTO();
                c.setName(name);
                c.setEmail(name.toLowerCase() + "@gmail.com");
                bankAccountService.saveCustomer(c);
            }
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            bankAccountService.bankAccountList().forEach(acc -> {
                String id = (acc instanceof ma.enset.digitalbanking.dtos.CurrentBankAccountDTO cur) ? cur.getId()
                        : ((ma.enset.digitalbanking.dtos.SavingBankAccountDTO) acc).getId();
                for (int i = 0; i < 5; i++) {
                    bankAccountService.credit(id, 1000 + Math.random() * 12000, "Credit");
                    bankAccountService.debit(id, 100 + Math.random() * 2000, "Debit");
                }
            });
        };
    }
}
