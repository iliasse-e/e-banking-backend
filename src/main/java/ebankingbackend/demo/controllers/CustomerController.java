package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.BankAccountService;
import ebankingbackend.demo.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> customers() {
        return customerService.listCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO dto) {
        return customerService.saveCustomer(dto);
    }

    @PatchMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long id, @RequestBody CustomerDTO dto) throws CustomerNotFoundException {
        dto.setId(id);
        return customerService.updateCustomer(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id) {
        customerService.deleteCustomer(id);
    }
}
