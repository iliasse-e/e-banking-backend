package ebankingbackend.demo.repositories;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ebankingbackend.demo.entities.Customer;

@DataJpaTest 
public class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
      Stream.of("Hassan", "Yassine", "Aicha", "Yassmine").forEach(name -> {
        customerRepository.save(Customer.builder()
            .name(name)
            .email(name.toLowerCase(Locale.ROOT) + "@gmail.com")
            .build()
        );
      });
  }

  @Test
  void findByEmail_shouldReturnEntity_whenCustomerExists() {
    String givenEmail = "aicha@gmail.com";

    Optional<Customer> result = customerRepository.findByEmail(givenEmail);

    AssertionsForClassTypes.assertThat(result).isPresent();
  }

  @Test
  void findByEmail_shouldReturnEmptyOptional_whenCustomerNotFound() {
    String notExistingEmail = "soumaya@gmail.com";

    Optional<Customer> result = customerRepository.findByEmail(notExistingEmail);

    assertThat(result).isEmpty();
  }

  @Test
  void findByNameContainsIgnoreCase_shouldReturnEntitiesList_whenCustomersFound() {
    String searchChar = "ass";

    List<Customer> result = customerRepository.findByNameContainsIgnoreCase(searchChar);

    assertThat(result)
      .isNotEmpty()
      .allMatch(customer -> customer.getName().contains(searchChar));
  }

  @Test
  void findByNameContainsIgnoreCase_shouldReturnEmptyList_whenCustomersNotFound() {
    String searchChar = "Yoshi";

    List<Customer> result = customerRepository.findByNameContainsIgnoreCase(searchChar);

    assertThat(result).isEmpty();
  }

  
}
