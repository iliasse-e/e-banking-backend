package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.AccountOperationDTO;
import ebankingbackend.demo.exceptions.OperationNotFoundException;

import java.util.List;

public interface AccountOperationService {

    public List<AccountOperationDTO> listOperation();

    public AccountOperationDTO getOperation(Long id) throws OperationNotFoundException;

    AccountOperationDTO saveOperation(AccountOperationDTO dto);

    AccountOperationDTO updateOperation(AccountOperationDTO dto) throws OperationNotFoundException;

    void deleteOperation(Long id);
}
