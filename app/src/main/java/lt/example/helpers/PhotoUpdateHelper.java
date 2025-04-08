package lt.example.helpers;

import lt.example.dtos.PhotoUpdateDto;
import lt.example.model.PhotoUpdateModel;
import lt.example.services.PhotoService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhotoUpdateHelper {

    private final PhotoService photoService;

    public void processUpdate(Long id, PhotoUpdateDto dto) {
        PhotoUpdateModel model = toBusinessModel(dto);
        photoService.updatePhoto(id, model);
    }

    private PhotoUpdateModel toBusinessModel(PhotoUpdateDto dto) {
        PhotoUpdateModel model = new PhotoUpdateModel();
        model.setDescription(dto.getDescription());
        model.setTags(dto.getTags());
        return model;
    }
}
