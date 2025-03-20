package lt.example.repositories;

import lt.example.entities.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhotoRepositoryCustom {
    Page<Photo> findAllWithTags(Pageable pageable);
}
