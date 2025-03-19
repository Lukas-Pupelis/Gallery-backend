package lt.example.dtos;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

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
}
