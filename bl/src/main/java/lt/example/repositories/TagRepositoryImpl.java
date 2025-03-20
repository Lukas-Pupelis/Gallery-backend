package lt.example.repositories;

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

import java.util.List;
import java.util.Set;

@Repository
public class TagRepositoryImpl implements TagRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> findPhotoTags(Set<Long> photoIds) {
        if (photoIds == null || photoIds.isEmpty()) {
            return List.of();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<Photo> photoRoot = cq.from(Photo.class);
        Join<Photo, Tag> tagJoin = photoRoot.join(Photo_.tags, JoinType.INNER);
        cq.multiselect(photoRoot.get(Photo_.id), tagJoin.get(Tag_.name));
        cq.where(photoRoot.get(Photo_.id).in(photoIds));
        cq.distinct(true);

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
