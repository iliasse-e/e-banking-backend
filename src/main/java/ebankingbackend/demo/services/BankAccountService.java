package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.BankAccountDTO;
import ebankingbackend.demo.dtos.CurrentAccountDTO;
import ebankingbackend.demo.dtos.SavingAccountDTO;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    void deleteBankAccount(Long id);

    BankAccountDTO saveBankAccount(BankAccountDTO dto);

    BankAccountDTO updateBankAccount(BankAccountDTO dto) throws BankAccountNotFoundException;
}
