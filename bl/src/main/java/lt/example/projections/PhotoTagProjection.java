package lt.example.projections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhotoTagProjection {
    private Long photoId;
    private String tagName;
}
