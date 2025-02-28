package lt.example.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @PostMapping("/api/upload")
    public ResponseEntity<Map<String, String>>
    uploadImage(@RequestParam("file")MultipartFile file,
            @RequestParam("tags") String tags) {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());
        System.out.println(file.getSize());
        System.out.println("method called");
        System.out.println(tags);
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "File is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else {
            response.put("message", "File uploaded");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

}
