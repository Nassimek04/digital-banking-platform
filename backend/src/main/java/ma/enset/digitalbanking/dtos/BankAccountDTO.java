package ma.enset.digitalbanking.dtos;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CurrentBankAccountDTO.class, name = "CurrentAccount"),
        @JsonSubTypes.Type(value = SavingBankAccountDTO.class, name = "SavingAccount")
})
public class BankAccountDTO {
    private String type;
}
