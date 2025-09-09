package ebankingbackend.demo.repositories;

import ebankingbackend.demo.entities.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, String> {
}
