package lt.example.helpers;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.PhotoSendDto;
import lt.example.entities.Photo;
import lt.example.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendHelper {

    private final ThumbnailHelper thumbnailHelper;

    public PhotoSendDto toDto(Photo photo) {
        System.out.println("Mapping photo id " + photo.getId() + " with tags: " + photo.getTags());

        PhotoSendDto dto = new PhotoSendDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setDescription(photo.getDescription());
        try {
            dto.setThumbnail(thumbnailHelper.createThumbnailBase64(photo.getFile()));
        } catch (IOException e) {
            throw new RuntimeException("Error generating thumbnail", e);
        }
        List<String> tagNames = photo.getTags().stream()
        .map(Tag::getName)
        .collect(Collectors.toList());

        dto.setTags(tagNames);
        return dto;
    }

    public Page<PhotoSendDto> toDtoPage(Page<Photo> photos) {
        return photos.map(this::toDto);
    }
}
