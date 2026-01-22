package ebankingbackend.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.repositories.CustomerRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private Long customerId; // On l'utilise pour palier à l'autoincrémentation dynamique

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        Customer saved = customerRepository.save(
          Customer.builder().name("Ahmed").email("ahmed@gmail.com").build()
        );
        this.customerId = saved.getId();
    }

    @Test
    void getCustomer_shouldReturnCustomerDto_whenCustomerExists() throws Exception {
        mockMvc.perform(get("/customers/" + customerId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Ahmed"));
    }

    @Test
    void getCustomer_shouldReturn404_whenCustomerNotFound() throws Exception {
        mockMvc.perform(get("/customers/99")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void saveCustomer_shouldPersistAndReturnCustomer() throws Exception {
        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"nassim@example.com\", \"name\":\"Nassim\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("nassim@example.com"));

        // Vérifie que le customer est bien en DB 
        assertThat(customerRepository.findAll())
          .extracting(Customer::getEmail)
          .contains("nassim@example.com");
    }

    @Test void updateCustomer_shouldUpdateAndReturnCustomerDto() throws Exception { 
      mockMvc.perform(patch("/customers/" + customerId)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Nezha\",\"email\":\"nezha@example.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Nezha"))
        .andExpect(jsonPath("$.email").value("nezha@example.com"));
      
      Customer updated = customerRepository.findById(customerId).orElseThrow();

      assertThat(updated.getName()).isEqualTo("Nezha");
      assertThat(updated.getEmail()).isEqualTo("nezha@example.com");
    }
}

