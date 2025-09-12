package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.BankAccountDTO;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankaccount")
@AllArgsConstructor
public class BankAccountController {
    private BankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> bankAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") Long id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id.toString());
    }

    @PostMapping
    public BankAccountDTO saveAccount(@RequestBody BankAccountDTO dto) {
        return bankAccountService.saveBankAccount(dto);
    }

    @PatchMapping("/{id}")
    public BankAccountDTO updateBankAccount(@PathVariable(name = "id") Long id, @RequestBody BankAccountDTO dto) throws BankAccountNotFoundException {
        dto.setId(id.toString());
        return bankAccountService.updateBankAccount(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id) {
        bankAccountService.deleteBankAccount(id);
    }

}
