package lt.example.helpers;

import jakarta.persistence.Tuple;
import lt.example.dtos.PhotoListDto;
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
        dto.setId(tuple.get("id", Long.class));
        dto.setName(tuple.get("name", String.class));
        dto.setDescription(tuple.get("description", String.class));
        dto.setThumbnail(tuple.get("thumbnail", String.class));
        dto.setCreatedAt(tuple.get("createdAt", LocalDateTime.class));
        dto.setTags(tagList);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<Tuple> projectionsPage) {
        Set<Long> photoIds = projectionsPage.getContent().stream()
        .map(tuple -> tuple.get("id", Long.class))
        .collect(Collectors.toSet());

        List<Tuple> tagData = tagRepository.findPhotoTags(photoIds);

        Map<Long, List<String>> tagMap = tagData.stream()
        .collect(Collectors.groupingBy(
            tuple -> tuple.get("photoId", Long.class),
            Collectors.mapping(tuple -> tuple.get("tagName", String.class), Collectors.toList())
        ));

        return projectionsPage.map(tuple -> {
            List<String> tagList = tagMap.getOrDefault(tuple.get("id", Long.class), List.of());
            try {
                return toDto(tuple, tagList);
            } catch (IOException e) {
                throw new RuntimeException("Error converting tuple to DTO", e);
            }
        });
    }
}
