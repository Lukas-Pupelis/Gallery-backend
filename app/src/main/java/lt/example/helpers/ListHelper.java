package lt.example.helpers;

import lt.example.dtos.PhotoSendDto;
import lt.example.entities.Photo;
import lt.example.repositories.TagRepository;
import lt.example.utilities.ThumbnailUtility;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListHelper {

    private final TagRepository tagRepository;

    public PhotoSendDto toDto(Photo photo, Map<Long, List<String>> tagMap) {
        PhotoSendDto dto = new PhotoSendDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setDescription(photo.getDescription());
        dto.setCreatedAt(photo.getCreatedAt());
        try {
            dto.setThumbnail(ThumbnailUtility.createThumbnailBase64(photo.getFile()));
        } catch (IOException e) {
            throw new RuntimeException("Error generating thumbnail", e);
        }
        dto.setTags(tagMap.getOrDefault(photo.getId(), Collections.emptyList()));
        return dto;
    }

    public Page<PhotoSendDto> toDtoPage(Page<Photo> photos) {
        Set<Long> photoIds = photos.getContent().stream()
        .map(Photo::getId)
        .collect(Collectors.toSet());

        List<Object[]> tagData = tagRepository.findPhotoTags(photoIds);

        Map<Long, List<String>> tagMap = tagData.stream()
        .collect(Collectors.groupingBy(
            arr -> (Long) arr[0],
            Collectors.mapping(arr -> (String) arr[1], Collectors.toList())
        ));
        return photos.map(photo -> toDto(photo, tagMap));
    }
}
