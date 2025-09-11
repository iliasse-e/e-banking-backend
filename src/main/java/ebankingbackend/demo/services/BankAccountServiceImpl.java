package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.BankAccountDTO;
import ebankingbackend.demo.dtos.CurrentAccountDTO;
import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.dtos.SavingAccountDTO;
import ebankingbackend.demo.entities.*;
import ebankingbackend.demo.enums.AccountStatus;
import ebankingbackend.demo.enums.OperationType;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.mappers.CurrentAccountMapperImpl;
import ebankingbackend.demo.mappers.CustomerMapperImpl;
import ebankingbackend.demo.mappers.SavingAccountMapperImpl;
import ebankingbackend.demo.repositories.AccountOperationRepository;
import ebankingbackend.demo.repositories.BankAccountRepository;
import ebankingbackend.demo.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    // Repositories
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    // Mappers
    private CustomerMapperImpl customerMapper;
    private SavingAccountMapperImpl savingAccountMapper;
    private CurrentAccountMapperImpl currentAccountMapper;


    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount bankAccount = new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setOverDraft(200);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCreatedAt(new Date());
        bankAccount.setCurrency("DH");
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(initialBalance);

        CurrentAccount savedAccount = bankAccountRepository.save(bankAccount);

        return currentAccountMapper.toDto(savedAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount bankAccount = new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setInterestRate(2.4);
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setCurrency("DH");
        bankAccount.setCreatedAt(new Date());
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(initialBalance);

        SavingAccount savedAccount = bankAccountRepository.save(bankAccount);

        return savingAccountMapper.toDto(savedAccount);
    }


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountEntity(accountId);
        if (bankAccount instanceof SavingAccount) {
            return savingAccountMapper.toDto((SavingAccount) bankAccount);
        } else {
            return currentAccountMapper.toDto((CurrentAccount) bankAccount);
        }
    }

    private BankAccount getBankAccountEntity(String accountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccountEntity(accountId);
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountEntity(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepository
                .findAll()
                .stream()
                .map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount) {
                        return savingAccountMapper.toDto((SavingAccount) bankAccount);
                    } else {
                        return currentAccountMapper.toDto((CurrentAccount) bankAccount);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO saveBankAccount(BankAccountDTO dto) {
        BankAccount entity;

        if (dto instanceof SavingAccountDTO) {
            entity = savingAccountMapper.toEntity((SavingAccountDTO) dto);
        } else {
            entity = currentAccountMapper.toEntity((CurrentAccountDTO) dto);
        }

        BankAccount savedEntity = bankAccountRepository.save(entity);

        if (dto instanceof SavingAccountDTO) {
            return savingAccountMapper.toDto((SavingAccount) savedEntity);
        } else {
            return currentAccountMapper.toDto((CurrentAccount) savedEntity);
        }
    }

    @Override
    public BankAccountDTO updateBankAccount(BankAccountDTO dto) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(dto.getId()).orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        BeanUtils.copyProperties(dto, account);

        BankAccount updatedCustomer = bankAccountRepository.save(account);

        if (account instanceof SavingAccount) {
            return savingAccountMapper.toDto((SavingAccount) updatedCustomer);
        } else {
            return currentAccountMapper.toDto((CurrentAccount) updatedCustomer);
        }
    }

    @Override
    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id.toString());

    }
}