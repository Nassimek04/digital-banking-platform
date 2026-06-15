package ma.enset.digitalbanking.mappers;

import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);
        return dto;
    }

    public Customer fromCustomerDTO(CustomerDTO dto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        dto.setType(savingAccount.getClass().getSimpleName());
        return dto;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO dto) {
        SavingAccount account = new SavingAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return account;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        dto.setType(currentAccount.getClass().getSimpleName());
        return dto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO dto) {
        CurrentAccount account = new CurrentAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return account;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation op) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(op, dto);
        return dto;
    }
}
