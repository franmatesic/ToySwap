package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.Message;
import hr.algebra.toyswap.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySenderAndReceiver(User sender, User receiver);

    List<Message> findAllBySenderOrReceiver(User sender, User receiver);
}
