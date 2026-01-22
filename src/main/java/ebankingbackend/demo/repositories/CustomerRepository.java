package ebankingbackend.demo.repositories;

import ebankingbackend.demo.entities.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByNameContainsIgnoreCase(String name);
  Optional<Customer> findByEmail(String name);
}
