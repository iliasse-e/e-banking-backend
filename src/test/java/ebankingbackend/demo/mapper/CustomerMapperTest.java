package ebankingbackend.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.mappers.CustomerMapperImpl;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    private CustomerMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new CustomerMapperImpl();
    }

    @Test
    void shouldMapDtoToEntity() {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");

        Customer entity = mapper.toEntity(dto);

        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getEmail(), entity.getEmail());
    }

    @Test
    void shouldMapEntityToDto() {
        Customer entity = new Customer();
        entity.setName("Jane Doe");
        entity.setEmail("jane@example.com");

        CustomerDTO dto = mapper.toDto(entity);

        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getEmail(), dto.getEmail());
    }
}
