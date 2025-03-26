package lt.example.helpers;

import lt.example.dtos.PhotoListDto;
import lt.example.projections.PhotoListProjection;
import lt.example.projections.PhotoTagProjection;
import lt.example.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lt.example.services.PhotoService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoListHelper {

    private final TagRepository tagRepository;
    private final PhotoService photoService;

    private PhotoListDto toDto(PhotoListProjection projection, List<String> tagList) throws IOException {
        PhotoListDto dto = new PhotoListDto();
        dto.setId(projection.getId());
        dto.setName(projection.getName());
        dto.setDescription(projection.getDescription());
        dto.setCreatedAt(projection.getCreatedAt());

        if (projection.getThumbnail() == null) {
            dto.setThumbnail(photoService.generateAndSaveThumbnail(projection.getId()));
        } else {
            dto.setThumbnail(projection.getThumbnail());
        }
        dto.setTags(tagList);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<PhotoListProjection> projectionsPage) {
        Set<Long> photoIds = projectionsPage.getContent().stream()
        .map(PhotoListProjection::getId)
        .collect(Collectors.toSet());

        List<PhotoTagProjection> tagData = tagRepository.findPhotoTags(photoIds);

        Map<Long, List<String>> tagMap = tagData.stream()
        .collect(Collectors.groupingBy(
            PhotoTagProjection::getPhotoId,
            Collectors.mapping(PhotoTagProjection::getTagName, Collectors.toList())
        ));

        return projectionsPage.map(projection -> {
            List<String> tagList = tagMap.getOrDefault(projection.getId(), List.of());
            try {
                return toDto(projection, tagList);
            } catch (IOException e) {
                throw new RuntimeException("Error converting projection to DTO", e);
            }
        });
    }
}
