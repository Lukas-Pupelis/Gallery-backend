package lt.example.helpers;

import lt.example.dtos.PhotoListDto;
import lt.example.model.PhotoListModel;
import lt.example.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoListHelper {

    private final TagRepository tagRepository;

    private PhotoListDto toDto(PhotoListModel model, List<String> tagList) {
        PhotoListDto dto = new PhotoListDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setThumbnail(model.getThumbnail());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setTags(tagList);
        return dto;
    }

    public Page<PhotoListDto> toDtoPage(Page<PhotoListModel> Page) {
        Set<Long> photoIds = Page.getContent().stream()
            .map(PhotoListModel::getId)
            .collect(Collectors.toSet());

        Map<Long, List<String>> tagMap = tagRepository.findPhotoTags(photoIds);

        return Page.map(model -> {
            List<String> tagList = tagMap.getOrDefault(model.getId(), List.of());
            return toDto(model, tagList);
        });
    }
}
