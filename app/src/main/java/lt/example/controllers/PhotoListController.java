package lt.example.controllers;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.PhotoSendDto;
import lt.example.entities.Photo;
import lt.example.helpers.SendHelper;
import lt.example.services.PhotoService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class PhotoListController {

    private final PhotoService photoService;
    private final SendHelper sendHelper;

    @GetMapping
    public Page<PhotoSendDto> getPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<Photo> photosPage = photoService.getPhotos(page, size, sortField, sortDir);
        return sendHelper.toDtoPage(photosPage);
    }
}
