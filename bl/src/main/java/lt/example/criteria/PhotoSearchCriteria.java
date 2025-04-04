package lt.example.criteria;

import java.time.LocalDate;
import lt.example.enums.SortDirection;
import lt.example.enums.SortField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoSearchCriteria {
    private String name;
    private String description;
    private LocalDate createdAt;
    private String tag;
    private int page;
    private int size;
    private SortField sortField;
    private SortDirection sortDir;
}
