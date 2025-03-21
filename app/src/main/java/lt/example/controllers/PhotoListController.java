package lt.example.controllers;

import lombok.RequiredArgsConstructor;
import lt.example.dtos.PhotoSearchDto;
import lt.example.dtos.PhotoListDto;
import lt.example.helpers.SearchHelper;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class PhotoListController {
    private final SearchHelper searchHelper;

    @GetMapping
    public Page<PhotoListDto> searchPhotos(@ModelAttribute PhotoSearchDto searchDto) {
        return searchHelper.processSearch(searchDto);
    }
}
