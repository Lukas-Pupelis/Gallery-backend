package lt.example.repositories;

import lt.example.entities.Photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query(value = "SELECT DISTINCT p FROM Photo p LEFT JOIN FETCH p.tags",
            countQuery = "SELECT count(p) FROM Photo p")
    Page<Photo> findAllWithTags(Pageable pageable);
}
