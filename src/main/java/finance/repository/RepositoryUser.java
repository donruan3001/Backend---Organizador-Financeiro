package finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finance.domain.user.User;


@Repository
public interface RepositoryUser extends JpaRepository<User, Long> {

User findByUsername(String username);

  
    

}
