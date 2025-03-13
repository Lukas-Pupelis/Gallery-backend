package lt.example.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoUploadDto {
    private MultipartFile file;
    private String photoName;
    private String photoDescription;
    private List<String> tags;
}