package hr.algebra.toyswap.converter;

import hr.algebra.toyswap.dto.PostDto;
import hr.algebra.toyswap.dto.TagDto;
import hr.algebra.toyswap.model.post.Post;
import hr.algebra.toyswap.model.post.PostImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostConverter implements Converter<Post, PostDto> {

  @Override
  public PostDto convert(Post source) {
    final var tags =
        source.getTags().stream()
            .map(tag -> TagDto.builder().id(tag.getId()).name(tag.getName()).build())
            .toList();
    final var imageIds = source.getImages().stream().map(PostImage::getId).toList();

    return PostDto.builder()
        .id(source.getId())
        .title(source.getTitle())
        .description(source.getDescription())
        .condition(source.getCondition())
        .price(source.getPrice())
        .tags(tags)
        .imageIds(imageIds)
        .build();
  }
}
