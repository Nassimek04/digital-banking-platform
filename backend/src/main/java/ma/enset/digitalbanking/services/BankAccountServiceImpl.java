package ma.enset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.entities.*;
import ma.enset.digitalbanking.enums.AccountStatus;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.exceptions.*;
import ma.enset.digitalbanking.mappers.BankAccountMapperImpl;
import ma.enset.digitalbanking.repositories.AccountOperationRepository;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImpl dtoMapper;

    // ---------------------------------------------------------------- Customers
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customer.setId(null);
        customer.setCreatedBy(AuditUtils.currentUsername());
        customer.setCreatedAt(new Date());
        return dtoMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer {}", customerDTO.getId());
        Customer existing = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        existing.setName(customerDTO.getName());
        existing.setEmail(customerDTO.getEmail());
        existing.setLastModifiedBy(AuditUtils.currentUsername());
        existing.setLastModifiedAt(new Date());
        return dtoMapper.fromCustomer(customerRepository.save(existing));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(dtoMapper::fromCustomer).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomer("%" + keyword + "%").stream()
                .map(dtoMapper::fromCustomer).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------- Accounts
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setCreatedDate(new Date());
        account.setBalance(initialBalance);
        account.setOverDraft(overDraft);
        account.setStatus(AccountStatus.CREATED);
        account.setCurrency("MAD");
        account.setCustomer(customer);
        account.setCreatedBy(AuditUtils.currentUsername());
        return dtoMapper.fromCurrentBankAccount(bankAccountRepository.save(account));
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount account = new SavingAccount();
        account.setId(UUID.randomUUID().toString());
        account.setCreatedDate(new Date());
        account.setBalance(initialBalance);
        account.setInterestRate(interestRate);
        account.setStatus(AccountStatus.CREATED);
        account.setCurrency("MAD");
        account.setCustomer(customer);
        account.setCreatedBy(AuditUtils.currentUsername());
        return dtoMapper.fromSavingBankAccount(bankAccountRepository.save(account));
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        return toDTO(account);
    }

    @Override
    public List<BankAccountDTO> getAccountsByCustomer(Long customerId) {
        return bankAccountRepository.findByCustomerId(customerId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private BankAccountDTO toDTO(BankAccount account) {
        if (account instanceof SavingAccount sa) return dtoMapper.fromSavingBankAccount(sa);
        return dtoMapper.fromCurrentBankAccount((CurrentAccount) account);
    }

    // ---------------------------------------------------------------- Operations
    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (account.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation op = AccountOperation.builder()
                .type(OperationType.DEBIT)
                .amount(amount)
                .description(description)
                .operationDate(new Date())
                .bankAccount(account)
                .performedBy(AuditUtils.currentUsername())
                .build();
        accountOperationRepository.save(op);
        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation op = AccountOperation.builder()
                .type(OperationType.CREDIT)
                .amount(amount)
                .description(description)
                .operationDate(new Date())
                .bankAccount(account)
                .performedBy(AuditUtils.currentUsername())
                .build();
        accountOperationRepository.save(op);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        return accountOperationRepository.findByBankAccountId(accountId).stream()
                .map(dtoMapper::fromAccountOperation).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        Page<AccountOperation> operations = accountOperationRepository
                .findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO dto = new AccountHistoryDTO();
        dto.setAccountOperationDTOS(operations.getContent().stream()
                .map(dtoMapper::fromAccountOperation).collect(Collectors.toList()));
        dto.setAccountId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setTotalPages(operations.getTotalPages());
        return dto;
    }
}
