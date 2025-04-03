package lt.example.model;

import java.time.LocalDateTime;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoListModel {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private List<String> tags;
    private LocalDateTime createdAt;
}
