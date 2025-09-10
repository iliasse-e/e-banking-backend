package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.*;
import ebankingbackend.demo.enums.AccountStatus;
import ebankingbackend.demo.enums.OperationType;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.mappers.CustomerMapperImpl;
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

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CustomerMapperImpl customerMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        Customer entity = customerMapper.toEntity(customer);
        Customer savedCustomer = customerRepository.save(entity);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
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

        return savedAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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

        return savedAccount;
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customersDTO = customers
                .stream().map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
        return customersDTO;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
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
        BankAccount bankAccount = getBankAccount(accountId);

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
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO dto) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(dto.getId()).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        BeanUtils.copyProperties(dto, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
