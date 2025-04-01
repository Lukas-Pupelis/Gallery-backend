package lt.example.repositories;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import lt.example.entities.Tag;
import lt.example.entities.Tag_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Repository
public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tuple> findPhotoTags(Set<Long> photoIds) {
        if (CollectionUtils.isEmpty(photoIds)) {
            return List.of();
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Photo> photoRoot = cq.from(Photo.class);
        Join<Photo, Tag> tagJoin = photoRoot.join(Photo_.tags, JoinType.INNER);

        cq.multiselect(
            photoRoot.get(Photo_.id).alias(Photo_.id.getName()),
            tagJoin.get(Tag_.name).alias(Tag_.name.getName())
        );
        cq.where(photoRoot.get(Photo_.id).in(photoIds));
        cq.distinct(true);

        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
