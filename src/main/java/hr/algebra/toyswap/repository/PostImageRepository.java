package hr.algebra.toyswap.repository;

import hr.algebra.toyswap.model.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {}
