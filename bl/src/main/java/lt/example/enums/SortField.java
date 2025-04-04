package lt.example.enums;

import java.util.function.Supplier;

import jakarta.persistence.metamodel.SingularAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;

@RequiredArgsConstructor
@Getter
public enum SortField {
    ID(() -> Photo_.id),
    NAME(() -> Photo_.name),
    DESCRIPTION(() -> Photo_.description),
    DATE(() -> Photo_.createdAt);

    private final Supplier<SingularAttribute<Photo, ?>> sortAttribute;
}
