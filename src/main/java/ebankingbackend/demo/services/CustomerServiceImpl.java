package ebankingbackend.demo.services;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.mappers.CustomerMapperImpl;
import ebankingbackend.demo.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapperImpl customerMapper;

    public Customer getCustomerEntity(Long id) throws CustomerNotFoundException {
        log.info("Fetching customer saved with ID: {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> {
            log.error("Customer with ID: {} not found", id);
            return new CustomerNotFoundException("Customer not found");
        });
        log.info("Customer with ID: {} found", id);
        return customer;
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = getCustomerEntity(id);
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("Saving new customer with email: {}", customer.getEmail());
        Customer entity = customerMapper.toEntity(customer);
        Customer savedCustomer = customerRepository.save(entity);
        log.info("Customer saved with email: {}", savedCustomer.getEmail());
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        log.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customersDTO = customers
                .stream().map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
        log.info("Found {} customers", customersDTO.size());
        return customersDTO;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO dto) throws CustomerNotFoundException {
        log.info("Updating customer with email: {}", dto.getEmail());
        Customer customer = getCustomerEntity(dto.getId());
        BeanUtils.copyProperties(dto, customer);

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Updated customer with email: {}", dto.getEmail());

        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id)  {
        log.info("Deleting customer with ID: {}", id);
        if (!customerRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent customer with ID: {}", id);
            return;
        }
        customerRepository.deleteById(id);
        log.debug("Deleted customer with ID: {}", id);
    }

}
