package lt.example.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String, String>> uploadImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("tags") String tags) {

        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "File is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            uploadHelper.processUpload(file, tags);
            response.put("message", "File uploaded");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Error processing file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
