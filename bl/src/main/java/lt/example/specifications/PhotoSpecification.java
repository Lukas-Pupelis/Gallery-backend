package lt.example.specifications;

import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import lt.example.entities.Tag;
import lt.example.entities.Tag_;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhotoSpecification {

    public static Specification<Photo> nameContains(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Photo_.name)),
                        trimString(name));
    }

    public static Specification<Photo> descriptionContains(String description) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Photo_.description)),
                        trimString(description));
    }

    public static Specification<Photo> createdAtOn(LocalDate date) {
        return (root, query, builder) -> {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime startOfNextDay = date.plusDays(1).atStartOfDay();
            return builder.between(root.get(Photo_.createdAt), startOfDay, startOfNextDay);
        };
    }

    public static Specification<Photo> tagContains(String tag) {
        return (root, query, builder) -> {
            Join<Photo, Tag> tagJoin = root.join(Photo_.tags, JoinType.INNER);
            return builder.like(builder.lower(tagJoin.get(Tag_.name)),
                    trimString(tag));
        };
    }

    public static Specification<Photo> buildSpecification(PhotoSearchCriteria criteria) {
        Specification<Photo> spec = Specification.where(null);

        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            spec = spec.and(nameContains(criteria.getName()));
        }
        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            spec = spec.and(descriptionContains(criteria.getDescription()));
        }
        if (criteria.getCreatedAt() != null) {
            spec = spec.and(createdAtOn(criteria.getCreatedAt()));
        }
        if (criteria.getTag() != null && !criteria.getTag().trim().isEmpty()) {
            String[] tagsArray = criteria.getTag().split(",");
            Specification<Photo> tagSpec = Specification.where(null);
            for (String rawTag : tagsArray) {
                String cleanedTag = rawTag.replace("\"", "").replace("'", "").trim();
                if (!cleanedTag.isEmpty()) {
                    tagSpec = tagSpec.or(tagContains(cleanedTag));
                }
            }
            spec = spec.and(tagSpec);
        }
        return spec;
    }

    public static String trimString(String string) {
        return "%" + string.toLowerCase().trim() + "%";
    }
}
