package lt.example.repositories;

import java.util.List;
import lt.example.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> save(List<Photo> photos);
}
