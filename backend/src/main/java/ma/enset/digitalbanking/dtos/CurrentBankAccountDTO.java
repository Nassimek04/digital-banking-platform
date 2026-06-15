package ma.enset.digitalbanking.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ma.enset.digitalbanking.enums.AccountStatus;
import java.util.Date;

@Data @EqualsAndHashCode(callSuper = true)
public class CurrentBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdDate;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double overDraft;
}
