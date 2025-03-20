package lt.example.controllers;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.PhotoSearchDto;
import lt.example.dtos.PhotoSendDto;
import lt.example.entities.Photo;
import lt.example.helpers.SearchHelper;
import lt.example.helpers.ListHelper;
import lt.example.services.PhotoService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class PhotoListController {

    private final PhotoService photoService;
    private final ListHelper listHelper;
    private final SearchHelper searchHelper;

    @GetMapping
    public Page<PhotoSendDto> getPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<Photo> photosPage = photoService.getPhotos(page, size, sortField, sortDir);
        return listHelper.toDtoPage(photosPage);
    }

    @GetMapping("/search")
    public Page<PhotoSendDto> searchPhotos(@ModelAttribute PhotoSearchDto searchDto) {
        return searchHelper.processSearch(searchDto);
    }
}
