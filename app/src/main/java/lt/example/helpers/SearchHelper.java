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
        criteria.setPage(dto.getPage());
        criteria.setSize(dto.getSize());
        criteria.setSortField(dto.getSortField());
        criteria.setSortDir(dto.getSortDir());
        return criteria;
    }

    public Page<PhotoSendDto> processSearch(PhotoSearchDto searchDto) {
        PhotoSearchCriteria criteria = toBusinessCriteria(searchDto);
        Page<Photo> photosPage;

        if(searchDto.getName() != null && !searchDto.getName().trim().isEmpty()
        || (searchDto.getDescription() != null && !searchDto.getDescription().trim().isEmpty())
        || (searchDto.getTag() != null && !searchDto.getTag().trim().isEmpty())
        || (searchDto.getCreatedAt() != null)) {
            photosPage = photoService.searchPhotos(criteria);
        } else photosPage = photoService.getPhotos(criteria);

        return listHelper.toDtoPage(photosPage);
    }
}
