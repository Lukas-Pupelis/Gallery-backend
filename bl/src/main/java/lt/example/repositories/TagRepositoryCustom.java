package lt.example.repositories;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Tuple;
import lt.example.projections.PhotoTagProjection;

public interface TagRepositoryCustom {
    List<Tuple> findPhotoTags(Set<Long> photoIds);
}