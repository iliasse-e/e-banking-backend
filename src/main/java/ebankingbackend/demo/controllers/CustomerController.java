package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers/create")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO dto) {
        return bankAccountService.saveCustomer(dto);
    }

    @PatchMapping("/customers/update/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long id, @RequestBody CustomerDTO dto) throws CustomerNotFoundException {
        dto.setId(id);
        return bankAccountService.updateCustomer(dto);
    }

    @DeleteMapping("/customers/delete/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id) {
        bankAccountService.deleteCustomer(id);
    }
}
