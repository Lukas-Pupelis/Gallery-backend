package lt.example.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoSendDto {

    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private List<String> tags;
}
