package lt.example.controllers;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.ResponseDto;
import lt.example.dtos.UploadDto;
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
    public ResponseEntity<ResponseDto> uploadImage(@ModelAttribute UploadDto uploadDto) {
        if (uploadDto.getFile().isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ResponseDto("File is empty"));
        }
        try {
            uploadHelper.processUpload(uploadDto);
            return ResponseEntity.ok(new ResponseDto("File uploaded"));
        } catch (IOException e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ResponseDto("Error processing file"));
        }
    }
}
