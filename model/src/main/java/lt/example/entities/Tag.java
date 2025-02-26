package lt.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Ensure the tag name is unique and not null.
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    // Mapped relationship; not strictly needed for the insert operation, but useful for bidirectional access.
    @ManyToMany(mappedBy = "tags")
    private Set<Photo> photos;

}
