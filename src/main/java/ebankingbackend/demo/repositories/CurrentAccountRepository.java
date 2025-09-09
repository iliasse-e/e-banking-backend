package ebankingbackend.demo.repositories;

import ebankingbackend.demo.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, String> {
}
