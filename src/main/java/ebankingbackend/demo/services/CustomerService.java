package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customer);

    List<CustomerDTO> listCustomer();

    CustomerDTO updateCustomer(CustomerDTO dto) throws CustomerNotFoundException;

    void deleteCustomer(Long id) ;

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
}
