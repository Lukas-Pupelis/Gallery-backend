package lt.example.controllers;

import java.io.IOException;
import lt.example.helpers.UploadHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    private final UploadHelper uploadHelper;

    public UploadController(UploadHelper uploadHelper) {
        this.uploadHelper = uploadHelper;
    }

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("tags") String tags) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"File is empty\"}");
        }
        try {
            uploadHelper.processUpload(file, tags);
            return ResponseEntity.ok("{\"message\": \"File uploaded\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error processing file\"}");
        }
    }
}
