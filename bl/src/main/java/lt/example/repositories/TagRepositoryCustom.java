package lt.example.repositories;

import java.util.List;
import java.util.Set;

public interface TagRepositoryCustom {
    List<Object[]> findPhotoTags(Set<Long> photoIds);
}