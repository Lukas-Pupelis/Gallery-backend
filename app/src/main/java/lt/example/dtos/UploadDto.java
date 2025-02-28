package lt.example.dtos;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDto {
    private byte[] photoData;
    private Set<String> tagNames;

    public UploadDto(byte[] photoData, Set<String> tagNames) {
        this.photoData = photoData;
        this.tagNames = tagNames;
    }
}