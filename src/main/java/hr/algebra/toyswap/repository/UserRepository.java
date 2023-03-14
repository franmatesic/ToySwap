package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
