package finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finance.domain.acounts.Account;

@Repository
public interface RepositoryAccount extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

}
