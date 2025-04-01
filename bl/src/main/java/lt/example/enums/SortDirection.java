package lt.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SortDirection {
    ASCENDING("ASCENDING"),
    DESCENDING("DESCENDING");

    private final String directionName;
}
