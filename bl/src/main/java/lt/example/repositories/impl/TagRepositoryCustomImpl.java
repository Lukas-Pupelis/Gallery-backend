package lt.example.repositories.impl;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import lt.example.entities.Tag;
import lt.example.entities.Tag_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lt.example.repositories.TagRepositoryCustom;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<Long, List<String>> findPhotoTags(Set<Long> photoIds) {
        if (CollectionUtils.isEmpty(photoIds)) {
            return Map.of();
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Photo> photoRoot = cq.from(Photo.class);
        Join<Photo, Tag> tagJoin = photoRoot.join(Photo_.tags, JoinType.INNER);

        Path<Long> photoIdPath = photoRoot.get(Photo_.id);
        Path<String> tagNamePath = tagJoin.get(Tag_.name);
        cq.multiselect(photoIdPath, tagNamePath);
        cq.where(photoRoot.get(Photo_.id).in(photoIds));
        cq.distinct(true);

        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        List<Tuple> tuples = query.getResultList();

        return tuples.stream()
        .collect(Collectors.groupingBy(
            tuple -> tuple.get(photoIdPath),
            Collectors.mapping(tuple -> tuple.get(tagNamePath), Collectors.toList())
        ));
    }
}
