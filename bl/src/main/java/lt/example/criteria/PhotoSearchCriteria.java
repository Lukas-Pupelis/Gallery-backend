package lt.example.criteria;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoSearchCriteria {
    private String name;
    private String description;
    private LocalDate createdAt;
    private String tag;
}
