package ebankingbackend.demo.dtos;

import lombok.Data;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
