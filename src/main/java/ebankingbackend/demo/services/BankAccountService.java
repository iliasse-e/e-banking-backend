package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.BankAccount;
import ebankingbackend.demo.entities.CurrentAccount;
import ebankingbackend.demo.entities.SavingAccount;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomer();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO dto) throws CustomerNotFoundException;

    void deleteCustomer(Long id);
}
