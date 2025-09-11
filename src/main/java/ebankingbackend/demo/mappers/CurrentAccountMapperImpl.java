package ebankingbackend.demo.mappers;

import ebankingbackend.demo.dtos.CurrentAccountDTO;
import ebankingbackend.demo.entities.CurrentAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentAccountMapperImpl {
    private CustomerMapperImpl customerMapper;

    public CurrentAccount toEntity(CurrentAccountDTO dto) {
        CurrentAccount account = new CurrentAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(customerMapper.toEntity(dto.getCustomer()));

        return account;
    }

    public CurrentAccountDTO toDto(CurrentAccount bankAccount) {
        CurrentAccountDTO dto = new CurrentAccountDTO();
        BeanUtils.copyProperties(bankAccount, dto);
        dto.setCustomer(customerMapper.toDto(bankAccount.getCustomer()));

        return dto;
    }
}
