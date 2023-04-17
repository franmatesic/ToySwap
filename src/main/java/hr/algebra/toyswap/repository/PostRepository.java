package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.post.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByDeactivatedAtIsNull();
}
