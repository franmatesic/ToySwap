package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {}
