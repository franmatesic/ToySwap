package hr.algebra.toyswap.converter;

import hr.algebra.toyswap.dto.TagDto;
import hr.algebra.toyswap.model.post.Tag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements Converter<Tag, TagDto> {

    @Override
    public TagDto convert(Tag source) {
        return TagDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
