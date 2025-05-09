package lt.example.dtos;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lt.example.enums.SortDirection;
import lt.example.enums.SortField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoSearchDto {
    private String name;
    private String description;
    private String tag;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    private int page;
    private int size;
    private SortField sortField;
    private SortDirection sortDir;
}
