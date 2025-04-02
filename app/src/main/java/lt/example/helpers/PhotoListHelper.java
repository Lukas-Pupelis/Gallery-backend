package lt.example.helpers;

import jakarta.persistence.Tuple;
import lt.example.dtos.PhotoListDto;
import lt.example.entities.Photo_;
import lt.example.entities.Tag_;
import lt.example.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoListHelper {

    private final TagRepository tagRepository;

    private PhotoListDto toDto(Tuple tuple, List<String> tagList) throws IOException {
        PhotoListDto dto = new PhotoListDto();
        dto.setId(tuple.get(Photo_.id.getName(), Long.class));
        dto.setName(tuple.get(Photo_.name.getName(), String.class));
        dto.setDescription(tuple.get(Photo_.description.getName(), String.class));
        dto.setThumbnail(tuple.get(Photo_.thumbnail.getName(), String.class));
        dto.setCreatedAt(tuple.get(Photo_.createdAt.getName(), LocalDateTime.class));
        dto.setTags(tagList);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<Tuple> Page) {
        Set<Long> photoIds = Page.getContent().stream()
        .map(tuple -> tuple.get(Photo_.id.getName(), Long.class))
        .collect(Collectors.toSet());

        List<Tuple> tagData = tagRepository.findPhotoTags(photoIds);

        Map<Long, List<String>> tagMap = tagData.stream()
        .collect(Collectors.groupingBy(
            tuple -> tuple.get(Photo_.id.getName(), Long.class),
            Collectors.mapping(tuple -> tuple.get(Tag_.name.getName(), String.class), Collectors.toList())
        ));

        return Page.map(tuple -> {
            List<String> tagList = tagMap.getOrDefault(tuple.get(Photo_.id.getName(), Long.class), List.of());
            try {
                return toDto(tuple, tagList);
            } catch (IOException e) {
                throw new RuntimeException("Error converting tuple to DTO", e);
            }
        });
    }
}
