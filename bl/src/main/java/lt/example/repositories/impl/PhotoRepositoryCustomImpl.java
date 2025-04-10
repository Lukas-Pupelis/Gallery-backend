package lt.example.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import lt.example.model.PhotoListModel;
import lt.example.repositories.PhotoRepositoryCustom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PhotoRepositoryCustomImpl implements PhotoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PhotoListModel> findPhotoListBySpec(Specification<Photo> spec, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Photo> root = cq.from(Photo.class);

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        Path<Long> idPath = root.get(Photo_.id);
        Path<String> namePath = root.get(Photo_.name);
        Path<String> descriptionPath = root.get(Photo_.description);
        Path<String> thumbnailPath = root.get(Photo_.thumbnail);
        Path<LocalDateTime> createdAtPath = root.get(Photo_.createdAt);
        cq.multiselect(
            namePath,
            idPath,
            descriptionPath,
            thumbnailPath,
            createdAtPath
        ).distinct(true);

        if (pageable.getSort().isSorted()) {
            applySorting(pageable, root, cq, cb);
        }

        Page<Tuple> queryResult = getPageResultsAndTotal(cq, spec, pageable, cb);
        return queryResult.map(tuple ->
            PhotoListModel.builder()
            .id(tuple.get(idPath))
            .name(tuple.get(namePath))
            .description(tuple.get(descriptionPath))
            .thumbnail(tuple.get(thumbnailPath))
            .createdAt(tuple.get(createdAtPath))
            .build()
        );
    }

    private Page<Tuple> getPageResultsAndTotal(
        CriteriaQuery<Tuple> cq,
        Specification<Photo> spec,
        Pageable pageable,
        CriteriaBuilder cb
    ) {
        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Tuple> tuples = query.getResultList();

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Photo> countRoot = countCq.from(Photo.class);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(countRoot, countCq, cb);
            if (predicate != null) {
                countCq.where(predicate);
            }
        }
        countCq.select(cb.countDistinct(countRoot));
        Long total = entityManager.createQuery(countCq).getSingleResult();

        return new PageImpl<>(tuples, pageable, total);
    }

    private void applySorting(Pageable pageable, Root<Photo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Order> orders = pageable.getSort().stream()
        .map(sortOrder -> sortOrder.isAscending()
            ? cb.asc(root.get(sortOrder.getProperty()))
            : cb.desc(root.get(sortOrder.getProperty())))
        .collect(Collectors.toList());
        cq.orderBy(orders);
    }
}
