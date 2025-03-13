package lt.example.repositories;

import java.util.List;
import java.util.Optional;

import lt.example.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> save(List<Tag> tags);
}
