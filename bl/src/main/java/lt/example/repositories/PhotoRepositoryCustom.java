package lt.example.repositories;

import jakarta.persistence.Tuple;
import lt.example.entities.Photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PhotoRepositoryCustom {
    Page<Tuple> findPhotoListBySpec(Specification<Photo> spec, Pageable pageable);
}

