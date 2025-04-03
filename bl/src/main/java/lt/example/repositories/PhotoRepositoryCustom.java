package lt.example.repositories;

import lt.example.entities.Photo;
import lt.example.model.PhotoListModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PhotoRepositoryCustom {
    Page<PhotoListModel> findPhotoListBySpec(Specification<Photo> spec, Pageable pageable);
}

