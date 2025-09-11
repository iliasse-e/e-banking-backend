package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.mappers.CustomerMapperImpl;
import ebankingbackend.demo.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapperImpl customerMapper;

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        Customer entity = customerMapper.toEntity(customer);
        Customer savedCustomer = customerRepository.save(entity);
        return customerMapper.toDto(savedCustomer);
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
