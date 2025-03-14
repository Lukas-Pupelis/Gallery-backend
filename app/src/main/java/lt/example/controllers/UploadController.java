package lt.example.controllers;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lt.example.GenericResponse;
import lt.example.dtos.PhotoUploadDto;
import lt.example.helpers.UploadHelper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final UploadHelper uploadHelper;

    @PostMapping("/api/upload")
    public ResponseEntity<GenericResponse> uploadImage(@ModelAttribute PhotoUploadDto photoUploadDto) {
        if (photoUploadDto.getFile().isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new GenericResponse("File is empty"));
        }
        try {
            uploadHelper.processUpload(photoUploadDto);
            return ResponseEntity.ok(new GenericResponse("File uploaded"));
        } catch (IOException e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new GenericResponse("Error processing file"));
        }
    }
}
