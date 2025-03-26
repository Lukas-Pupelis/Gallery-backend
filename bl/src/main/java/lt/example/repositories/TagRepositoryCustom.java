package lt.example.repositories;

import java.util.List;
import java.util.Set;

import lt.example.projections.PhotoTagProjection;

public interface TagRepositoryCustom {
    List<PhotoTagProjection> findPhotoTags(Set<Long> photoIds);
}