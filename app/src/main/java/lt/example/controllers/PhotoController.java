package lt.example.controllers;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lt.example.GenericResponse;
import lt.example.dtos.PhotoListDto;
import lt.example.dtos.PhotoSearchDto;
import lt.example.dtos.PhotoUploadDto;
import lt.example.helpers.SearchHelper;
import lt.example.helpers.UploadHelper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PhotoController {

    private final UploadHelper uploadHelper;
    private final SearchHelper searchHelper;

    @GetMapping("/list")
    public Page<PhotoListDto> searchPhotos(@ModelAttribute PhotoSearchDto searchDto) {
        return searchHelper.processSearch(searchDto);
    }

    @PostMapping("/upload")
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
