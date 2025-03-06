package lt.example.dtos;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDto {
    private MultipartFile file;
    private String photoName;
    private String photoDescription;
    private String tags;
}