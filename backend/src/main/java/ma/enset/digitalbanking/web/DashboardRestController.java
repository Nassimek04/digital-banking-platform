package ma.enset.digitalbanking.web;

import lombok.AllArgsConstructor;
import ma.enset.digitalbanking.entities.BankAccount;
import ma.enset.digitalbanking.entities.CurrentAccount;
import ma.enset.digitalbanking.entities.SavingAccount;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.repositories.AccountOperationRepository;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aggregated statistics consumed by the Angular dashboard (ng2-charts / Chart.js).
 */
@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
@CrossOrigin("*")
public class DashboardRestController {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;

    @GetMapping("/stats")
    public Map<String, Object> globalStats() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        double totalBalance = accounts.stream().mapToDouble(BankAccount::getBalance).sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerRepository.count());
        stats.put("totalAccounts", accounts.size());
        stats.put("totalOperations", accountOperationRepository.count());
        stats.put("totalBalance", totalBalance);
        return stats;
    }

    @GetMapping("/accountTypes")
    public Map<String, Long> accountTypeDistribution() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        long current = accounts.stream().filter(a -> a instanceof CurrentAccount).count();
        long saving = accounts.stream().filter(a -> a instanceof SavingAccount).count();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("CurrentAccount", current);
        map.put("SavingAccount", saving);
        return map;
    }

    @GetMapping("/operationsByType")
    public Map<String, Long> operationsByType() {
        Map<String, Long> map = new LinkedHashMap<>();
        long credits = accountOperationRepository.findAll().stream()
                .filter(o -> o.getType() == OperationType.CREDIT).count();
        long debits = accountOperationRepository.findAll().stream()
                .filter(o -> o.getType() == OperationType.DEBIT).count();
        map.put("CREDIT", credits);
        map.put("DEBIT", debits);
        return map;
    }

    @GetMapping("/balanceByCustomer")
    public Map<String, Double> balanceByCustomer() {
        return bankAccountRepository.findAll().stream()
                .filter(a -> a.getCustomer() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getCustomer().getName(),
                        LinkedHashMap::new,
                        Collectors.summingDouble(BankAccount::getBalance)));
    }
}
