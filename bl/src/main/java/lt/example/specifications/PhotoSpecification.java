package lt.example.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lt.example.criteria.PhotoSearchCriteria;
import lt.example.entities.Photo;
import lt.example.entities.Tag;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class PhotoSpecification implements Specification<Photo> {

    private final PhotoSearchCriteria criteria;

    public Predicate toPredicate(@NonNull Root<Photo> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        Objects.requireNonNull(query).distinct(true);

        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            predicates.add(builder.like(
            builder.lower(root.get("name")),
            trimString(criteria.getName())
            ));
        }

        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            predicates.add(builder.like(
            builder.lower(root.get("description")),
            trimString(criteria.getDescription())
            ));
        }

        if (criteria.getCreatedAt() != null) {
            LocalDate date = criteria.getCreatedAt();
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime startOfNextDay = date.plusDays(1).atStartOfDay();
            predicates.add(builder.between(root.get("createdAt"), startOfDay, startOfNextDay));
        }

        if (criteria.getTag() != null && !criteria.getTag().trim().isEmpty()) {
            String[] tagsArray = criteria.getTag().split(",");
            List<Predicate> tagPredicates = new ArrayList<>();

            Join<Photo, Tag> tagJoin = root.join("tags", JoinType.INNER);

            for (String rawTag : tagsArray) {
                String cleanedTag = rawTag
                .replace("\"", "")
                .replace("'", "")
                .trim()
                .toLowerCase();

                if (!cleanedTag.isEmpty()) {
                    tagPredicates.add(
                    builder.like(builder.lower(tagJoin.get("name")), "%" + cleanedTag + "%")
                    );
                }
            }

            if (!tagPredicates.isEmpty()) {
                predicates.add(builder.or(tagPredicates.toArray(new Predicate[0])));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public String trimString(String string) {
        return "%" + string.toLowerCase().trim() + "%";
    }
}
