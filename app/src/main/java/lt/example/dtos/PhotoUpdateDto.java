package lt.example.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoUpdateDto {
    private String description;
    private List<String> tags;
}
