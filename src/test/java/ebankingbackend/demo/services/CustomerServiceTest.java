package ebankingbackend.demo.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.mappers.CustomerMapperImpl;
import ebankingbackend.demo.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Mock 
  private CustomerRepository customerRepository;

  @Mock 
  private CustomerMapperImpl customerMapper;
  
  @InjectMocks 
  private CustomerServiceImpl customerService;

  @Test
  void getCustomer_shouldReturnDto_whenCustomerExists() throws CustomerNotFoundException {

    Customer customer = Customer.builder().name("Jahid").email("jahid@gmail.com").build();
    CustomerDTO dto = CustomerDTO.builder().name("Jahid").email("jahid@gmail.com").build();
 
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer)); 
    when(customerMapper.toDto(customer)).thenReturn(dto);

    CustomerDTO result = customerService.getCustomer(1L);

    assertThat(result)
      .isNotNull()
      .isEqualTo(dto);

    verify(customerRepository).findById(1L);
    verify(customerMapper).toDto(customer);
  }
  
  @Test
  void getCustomer_shouldThrowError_whenCustomerNotFound() throws CustomerNotFoundException {

    when(customerRepository.findById(22L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> customerService.getCustomer(22L))
    .isInstanceOf(CustomerNotFoundException.class)
    .hasMessageContaining("Customer not found");;

  }
}
