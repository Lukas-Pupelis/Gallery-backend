package lt.example.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lt.example.services.PhotoService;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UploadHelper {

    private final PhotoService photoService;

    public void processUpload(MultipartFile file, String photoName, String photoDescription, String tagsStr) throws IOException {
        byte[] photoData = file.getBytes();
        String[] tagArray = tagsStr.split(",");
        Set<String> tagNames = new HashSet<>();
        Arrays.stream(tagArray)
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .forEach(tagNames::add);
        photoService.savePhoto(photoData, photoName, photoDescription, tagNames);
    }
}
