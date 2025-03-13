package lt.example.helpers;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.PhotoUploadDto;
import lt.example.services.PhotoService;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadHelper {

    private final PhotoService photoService;

    public void processUpload(PhotoUploadDto photoUploadDto) throws IOException {
        byte[] photoData = photoUploadDto.getFile().getBytes();

        Set<String> tagNames;
        if (photoUploadDto.getTags() != null) {
            tagNames = photoUploadDto.getTags().stream()
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.toSet());
        }
        else {
            tagNames = Collections.emptySet();
        }
        photoService.savePhoto(
            photoData,
            photoUploadDto.getPhotoName(),
            photoUploadDto.getPhotoDescription(),
            tagNames
        );
    }
}
