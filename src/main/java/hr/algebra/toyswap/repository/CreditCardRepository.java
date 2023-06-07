package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.user.CreditCard;
import hr.algebra.toyswap.model.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

  List<CreditCard> findAllByUser(User user);
}
