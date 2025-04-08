package lt.example.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoReviewDto {
    private Long id;
    private String name;
    private String description;
    private String originalImageBase64;
    private LocalDateTime createdAt;
    private List<String> tags;
}
