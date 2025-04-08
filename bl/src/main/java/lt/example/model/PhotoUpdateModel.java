package lt.example.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoUpdateModel {
    private String description;
    private List<String> tags;
}
