package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.AccountOperationDTO;
import ebankingbackend.demo.entities.AccountOperation;
import ebankingbackend.demo.exceptions.OperationNotFoundException;
import ebankingbackend.demo.mappers.AccountOperationMapperImpl;
import ebankingbackend.demo.repositories.AccountOperationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountOperationServiceImpl implements AccountOperationService{

    private AccountOperationRepository accountOperationRepository;
    private AccountOperationMapperImpl mapper;

    @Override
    public List<AccountOperationDTO> listOperation() {
        return accountOperationRepository
                .findAll()
                .stream().map(operation -> mapper.toDto(operation))
                .collect(Collectors.toList());
    }

    @Override
    public AccountOperationDTO getOperation(Long id) throws OperationNotFoundException {
        AccountOperation entity = getOperationEntity(id);

        return mapper.toDto(entity);
    }

    private AccountOperation getOperationEntity(Long id) throws OperationNotFoundException {
        return accountOperationRepository.findById(id).orElseThrow(() -> new OperationNotFoundException("Operation not found"));
    }

    @Override
    public AccountOperationDTO saveOperation(AccountOperationDTO dto) {
        AccountOperation savedEntity = accountOperationRepository.save(mapper.toEntity(dto));
        return mapper.toDto(savedEntity);
    }

    @Override
    public AccountOperationDTO updateOperation(AccountOperationDTO dto) throws OperationNotFoundException {
        AccountOperation entity = getOperationEntity(dto.getId());
        BeanUtils.copyProperties(dto, entity);

        AccountOperation savedEntity = accountOperationRepository.save(entity);

        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteOperation(Long id) {
        accountOperationRepository.deleteById(id);
    }
}
