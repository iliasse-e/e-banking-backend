package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.AccountOperationDTO;
import ebankingbackend.demo.exceptions.OperationNotFoundException;
import ebankingbackend.demo.services.AccountOperationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operations")
@AllArgsConstructor
public class OperationController {
    private AccountOperationService accountOperationService;

    @GetMapping
    public List<AccountOperationDTO> operations() {
        return accountOperationService.listOperation();
    }

    @GetMapping("/{id}")
    public AccountOperationDTO getOperation(@PathVariable(name = "id") Long id) throws OperationNotFoundException {
        return accountOperationService.getOperation(id);
    }

    @PostMapping
    public AccountOperationDTO saveOperation(@RequestBody AccountOperationDTO dto) {
        return accountOperationService.saveOperation(dto);
    }

    @PatchMapping("/{id}")
    public AccountOperationDTO updateOperation(@PathVariable(name = "id") Long id, @RequestBody AccountOperationDTO dto) throws OperationNotFoundException {
        dto.setId(id);
        return accountOperationService.updateOperation(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteOperation(@PathVariable(name = "id") Long id) {
        accountOperationService.deleteOperation(id);
    }
}
