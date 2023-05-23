package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDeactivatedAtIsNull();

    List<Post> findAllByUserAndDeactivatedAtIsNull(User user);
}
