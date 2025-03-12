package lt.example.helpers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.UploadDto;
import lt.example.services.PhotoService;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadHelper {

    private final PhotoService photoService;

    public void processUpload(UploadDto uploadDto) throws IOException {
        byte[] photoData = uploadDto.getFile().getBytes();
        Set<String> tagNames = new HashSet<>();
        if (uploadDto.getTags() != null) {
            uploadDto.getTags().stream()
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .forEach(tagNames::add);
        }
        photoService.savePhoto(
            photoData,
            uploadDto.getPhotoName(),
            uploadDto.getPhotoDescription(),
            tagNames
        );
    }
}
