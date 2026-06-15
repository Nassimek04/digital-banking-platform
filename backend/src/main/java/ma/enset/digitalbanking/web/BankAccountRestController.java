package ma.enset.digitalbanking.web;

import lombok.AllArgsConstructor;
import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccountDTO> getAccountsByCustomer(@PathVariable Long customerId) {
        return bankAccountService.getAccountsByCustomer(customerId);
    }

    @GetMapping("/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/current/{customerId}")
    public CurrentBankAccountDTO createCurrentAccount(
            @PathVariable Long customerId,
            @RequestParam double initialBalance,
            @RequestParam(defaultValue = "0") double overDraft) {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/saving/{customerId}")
    public SavingBankAccountDTO createSavingAccount(
            @PathVariable Long customerId,
            @RequestParam double initialBalance,
            @RequestParam(defaultValue = "0") double interestRate) {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequestDTO dto) {
        bankAccountService.transfer(dto.getAccountSource(), dto.getAccountDestination(), dto.getAmount());
    }
}
