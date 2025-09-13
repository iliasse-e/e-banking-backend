package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> customers() {
        log.info("GET /customers - Listing all customers");
        return customerService.listCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        log.info("GET /customers/{} - Fetching customer", id);
        return customerService.getCustomer(id);
    }

    @PostMapping
    public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO dto) {
        log.info("POST /customers - Creating customer with email: {}", dto.getEmail());
        return customerService.saveCustomer(dto);
    }

    @PatchMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long id, @RequestBody CustomerDTO dto) throws CustomerNotFoundException {
        log.info("PATCH /customers/{} - Updating customer", id);
        dto.setId(id);
        return customerService.updateCustomer(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id) {
        log.info("DELETE /customers/{} - Deleting customer", id);
        customerService.deleteCustomer(id);
    }
}
