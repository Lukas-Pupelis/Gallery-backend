package lt.example.controllers;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.UploadDto;
import lt.example.helpers.UploadHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final UploadHelper uploadHelper;

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadImage(@ModelAttribute UploadDto uploadDto) {

        MultipartFile file = uploadDto.getFile();

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"File is empty\"}");
        }
        try {
            uploadHelper.processUpload(file, uploadDto.getPhotoName(),
            uploadDto.getPhotoDescription(), uploadDto.getTags());
            return ResponseEntity.ok("{\"message\": \"File uploaded\"}");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error processing file\"}");
        }
    }
}
