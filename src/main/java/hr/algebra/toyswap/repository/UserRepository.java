package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByResetPasswordToken(String token);

  boolean existsByEmail(String email);
}
