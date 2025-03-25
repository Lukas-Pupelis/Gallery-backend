package lt.example.helpers;

import lt.example.dtos.PhotoSearchDto;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.dtos.PhotoListDto;
import lt.example.entities.Photo;
import lt.example.services.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchHelper {

    private final PhotoService photoService;
    private final PhotoListHelper photoListHelper;

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

    public Page<PhotoListDto> processSearch(PhotoSearchDto searchDto) {
        PhotoSearchCriteria criteria = toBusinessCriteria(searchDto);

        Page<Photo> photosPage = hasSearchCriteria(searchDto)
            ? photoService.searchPhotos(criteria)
            : photoService.getPhotos(criteria);

        return photoListHelper.toDtoPage(photosPage);
    }

    private boolean hasSearchCriteria(PhotoSearchDto searchDto) {
        return (StringUtils.hasText(searchDto.getName())
            || StringUtils.hasText(searchDto.getDescription())
            || StringUtils.hasText(searchDto.getTag())
            || StringUtils.hasText(String.valueOf(searchDto.getCreatedAt())));
    }
}
