package lt.example.helpers;

import jakarta.persistence.Tuple;
import lt.example.dtos.PhotoListDto;
import lt.example.entities.Photo;
import lt.example.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lt.example.services.PhotoService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoListHelper {

    private final TagRepository tagRepository;
    private final PhotoService photoService;

    private PhotoListDto toDto(Photo photo, List<String> tagList) throws IOException {
        PhotoListDto dto = new PhotoListDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setDescription(photo.getDescription());
        dto.setCreatedAt(photo.getCreatedAt());

        if (photo.getThumbnail() == null) {
            dto.setThumbnail(photoService.generateAndSaveThumbnail(photo.getId(), photo.getFile()));
        } else dto.setThumbnail(photo.getThumbnail());

        dto.setTags(tagList);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<Photo> photos) {
        Set<Long> photoIds = photos.getContent().stream()
        .map(Photo::getId)
        .collect(Collectors.toSet());

        List<Tuple> tagData = tagRepository.findPhotoTags(photoIds);

        return photos.map(photo -> {
            List<String> tagList = tagData.stream()
            .filter(tuple -> tuple.get("photoId", Long.class).equals(photo.getId()))
            .map(tuple -> tuple.get("tagName", String.class))
            .collect(Collectors.toList());

            try {
                return toDto(photo, tagList);
            } catch (IOException e) {
                throw new RuntimeException("Error converting photo to DTO", e);
            }
        });
    }
}
