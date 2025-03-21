package lt.example.helpers;

import lt.example.dtos.PhotoListDto;
import lt.example.entities.Photo;
import lt.example.services.ThumbnailService;
import lt.example.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoListHelper {

    private final ThumbnailService thumbnailService;
    private final TagRepository tagRepository;

    private PhotoListDto toDto(Photo photo, Map<Long, List<String>> tagMap) throws IOException {
        PhotoListDto dto = new PhotoListDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setDescription(photo.getDescription());
        dto.setCreatedAt(photo.getCreatedAt());
        dto.setThumbnail(thumbnailService.createThumbnailBase64(photo.getFile()));

        List<String> tagNames = tagMap.getOrDefault(photo.getId(), List.of());
        dto.setTags(tagNames);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<Photo> photos) {
        Set<Long> photoIds = photos.getContent().stream()
        .map(Photo::getId)
        .collect(Collectors.toSet());

        List<Object[]> tagData = tagRepository.findPhotoTags(photoIds);

        Map<Long, List<String>> tagMap = tagData.stream()
        .collect(Collectors.groupingBy(
        arr -> (Long) arr[0],
        Collectors.mapping(arr -> (String) arr[1], Collectors.toList())
        ));

        return photos.map(photo -> {
            try {
                return toDto(photo, tagMap);
            } catch (IOException e) {
                throw new RuntimeException("Error converting photo to DTO", e);
            }
        });
    }
}
