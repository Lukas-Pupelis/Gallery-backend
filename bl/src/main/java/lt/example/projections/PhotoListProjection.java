package lt.example.projections;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhotoListProjection {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private LocalDateTime createdAt;
}
