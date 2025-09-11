package ebankingbackend.demo.mappers;

import ebankingbackend.demo.dtos.SavingAccountDTO;
import ebankingbackend.demo.entities.SavingAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SavingAccountMapperImpl {
    private CustomerMapperImpl customerMapper;

    public SavingAccount toEntity(SavingAccountDTO dto) {
        SavingAccount account = new SavingAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(customerMapper.toEntity(dto.getCustomer()));

        return account;
    }

    public SavingAccountDTO toDto(SavingAccount bankAccount) {
        SavingAccountDTO dto = new SavingAccountDTO();
        BeanUtils.copyProperties(bankAccount, dto);
        dto.setCustomer(customerMapper.toDto(bankAccount.getCustomer()));

        return dto;
    }
}
