package ebankingbackend.demo.controllers;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.CustomerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCustomer_shouldReturnCustomer_whenFound() throws Exception {
        when(customerService.getCustomer(1L))
            .thenReturn(new CustomerDTO(1L, "Hassan", "hassan@gmail.com"));

        mockMvc.perform(get("/customers/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value("Hassan"));

        verify(customerService).getCustomer(1L);
    }

    @Test
    void getCustomer_shouldReturn404_whenNotFound() throws Exception {
        when(customerService.getCustomer(2L))
            .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/customers/2"))
            .andExpect(status().isNotFound());

        verify(customerService).getCustomer(2L);
    }

    @Test
    void listCustomer_ReturnEmptyList_whenExists() throws Exception {
        when(customerService.listCustomer())
            .thenReturn(List.of(
                new CustomerDTO(1L, "Hassan", "hassan@gmail.com"),
                new CustomerDTO(3L, "Yassine", "yassine@gmail.com")
            ));

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Hassan"));

        verify(customerService).listCustomer();
    }

    @Test
    void listCustomer_ReturnEmptyList_whenNotFound() throws Exception {
        when(customerService.listCustomer())
            .thenReturn(List.of());

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("[]"));

        verify(customerService).listCustomer();
    }

    @Test
    void saveCustomer_shouldCreateCustomer() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, "Bob", "bob@mail.com");
        when(customerService.saveCustomer(any(CustomerDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Bob\",\"email\":\"bob@mail.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Bob"))
            .andExpect(jsonPath("$.email").value("bob@mail.com"));

        verify(customerService).saveCustomer(any(CustomerDTO.class));
    }

    @Test
    void deleteCustomer_shouldDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
            .andExpect(status().isOk());

        verify(customerService).deleteCustomer(1L); // On vérifie que la méthode du service a été appellé (étant void)
    }

    @Test
    void updateCustomer_shouldUpdateCustomer() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, "Charlie", "charlie@mail.com");
        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(dto);

        mockMvc.perform(patch("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Charlie\",\"email\":\"charlie@mail.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Charlie"))
            .andExpect(jsonPath("$.email").value("charlie@mail.com"));

        verify(customerService).updateCustomer(any(CustomerDTO.class));
    }

    @Test
    void saveCustomer_shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String invalidCustomerJson = """
            { "name": "John", "email": "not-an-email", "age": 25 }
        """;

        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidCustomerJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("email"))
            .andExpect(jsonPath("$.errors[0].message").value("must be a well-formed email address"));
    }

        @Test
    void saveCustomer_shouldReturnBadRequestWhenNameIsInvalid() throws Exception {
        String invalidCustomerJson = """
            { "name": "J", "email": "namek@rmail.com", "age": 25 }
        """;

        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidCustomerJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("name"))
            .andExpect(jsonPath("$.errors[0].message").value("Name must have at least 2 characters"));
    }

    @Test
    void updateCustomer_shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String invalidCustomerJson = """
            { "name": "John", "email": null, "age": 25 }
        """;

        mockMvc.perform(patch("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidCustomerJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("email"))
            .andExpect(jsonPath("$.errors[0].message").value("Email cannot be null"));
    }

}
