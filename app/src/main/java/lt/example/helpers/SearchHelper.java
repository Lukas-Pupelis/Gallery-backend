package lt.example.helpers;

import lt.example.dtos.PhotoSearchDto;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.dtos.PhotoSendDto;
import lt.example.entities.Photo;
import lt.example.services.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchHelper {

    private final PhotoService photoService;
    private final ListHelper listHelper;

    private PhotoSearchCriteria toBusinessCriteria(PhotoSearchDto dto) {
        PhotoSearchCriteria criteria = new PhotoSearchCriteria();
        criteria.setName(dto.getName());
        criteria.setDescription(dto.getDescription());
        criteria.setTag(dto.getTag());
        criteria.setCreatedAt(dto.getCreatedAt());
        return criteria;
    }

    public Page<PhotoSendDto> processSearch(PhotoSearchDto searchDto, int page, int size, String sortField, String sortDir) {
        PhotoSearchCriteria criteria = toBusinessCriteria(searchDto);
        Page<Photo> photosPage = photoService.searchPhotos(criteria, page, size, sortField, sortDir);
        return listHelper.toDtoPage(photosPage);
    }
}
