package lt.example.repositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagRepositoryCustom {
    Map<Long, List<String>> findPhotoTags(Set<Long> photoIds);
}