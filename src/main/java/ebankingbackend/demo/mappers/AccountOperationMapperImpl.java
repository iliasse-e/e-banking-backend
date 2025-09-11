package ebankingbackend.demo.mappers;

import ebankingbackend.demo.dtos.AccountOperationDTO;
import ebankingbackend.demo.entities.AccountOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountOperationMapperImpl {

    public AccountOperation toEntity(AccountOperationDTO dto) {
        AccountOperation operation = new AccountOperation();
        BeanUtils.copyProperties(dto, operation);

        return operation;
    }

    public AccountOperationDTO toDto(AccountOperation operation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(operation, dto);

        return dto;
    }
}
