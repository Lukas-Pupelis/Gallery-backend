package lt.example.controllers;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lt.example.GenericResponse;
import lt.example.dtos.PhotoListDto;
import lt.example.dtos.PhotoReviewDto;
import lt.example.dtos.PhotoSearchDto;
import lt.example.dtos.PhotoUpdateDto;
import lt.example.dtos.PhotoUploadDto;
import lt.example.entities.Photo;
import lt.example.helpers.PhotoReviewHelper;
import lt.example.helpers.PhotoUpdateHelper;
import lt.example.helpers.SearchHelper;
import lt.example.helpers.UploadHelper;
import lt.example.services.PhotoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/photo")
public class PhotoController {

    private final UploadHelper uploadHelper;
    private final SearchHelper searchHelper;
    private final PhotoService photoService;
    private final PhotoReviewHelper photoReviewHelper;
    private final PhotoUpdateHelper photoUpdateHelper;

    @PostMapping("/photo-list")
    public Page<PhotoListDto> searchPhotos(@RequestBody PhotoSearchDto searchDto) {
        return searchHelper.processSearch(searchDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<GenericResponse> uploadImage(
    @RequestPart("file") MultipartFile file,
    @RequestPart("dataDto") PhotoUploadDto photoUploadDto) {
        if (file.isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new GenericResponse("File is empty"));
        }
        try {
            photoUploadDto.setFile(file);
            uploadHelper.processUpload(photoUploadDto);
            return ResponseEntity.ok(new GenericResponse("File uploaded"));
        } catch (IOException e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new GenericResponse("Error processing file"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoReviewDto> getFullPhoto(@PathVariable Long id) {
        Photo photo = photoService.getPhotoById(id);
        PhotoReviewDto dto = photoReviewHelper.toDto(photo);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updatePhoto(@PathVariable Long id,
    @RequestBody PhotoUpdateDto updateDto) {
        try {
            photoUpdateHelper.processUpdate(id, updateDto);
            return ResponseEntity.ok(new GenericResponse("Photo updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new GenericResponse(e.getMessage()));
        }
    }
}
