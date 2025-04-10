package lt.example.helpers;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lt.example.dtos.PhotoReviewDto;
import lt.example.entities.Photo;
import lt.example.entities.Tag;

@Component
public class PhotoReviewHelper {

    public PhotoReviewDto toDto(Photo photo) {
        PhotoReviewDto dto = new PhotoReviewDto();
        dto.setId(photo.getId());
        dto.setName(photo.getName());
        dto.setDescription(photo.getDescription());
        dto.setCreatedAt(photo.getCreatedAt());
        byte[] fileData = photo.getFile();
        dto.setOriginalImageBase64(fileData != null ? Base64.getEncoder().encodeToString(fileData) : null);

        List<String> tagNames = photo.getTags().stream()
            .map(Tag::getName)
            .collect(Collectors.toList());

        dto.setTags(tagNames);
        return dto;
    }
}
