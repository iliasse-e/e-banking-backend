package ebankingbackend.demo.repositories;

import ebankingbackend.demo.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    public List<AccountOperation> findByBankAccountId(String id);
    public Page<AccountOperation> findByBankAccountId(String id, Pageable pageable);
}
