package lt.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import lt.example.projections.PhotoListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PhotoRepositoryCustomImpl implements PhotoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PhotoListProjection> findPhotoListProjectionsBySpec(Specification<Photo> spec, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PhotoListProjection> cq = cb.createQuery(PhotoListProjection.class);
        Root<Photo> root = cq.from(Photo.class);

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        cq.select(cb.construct(
            PhotoListProjection.class,
            root.get(Photo_.id),
            root.get(Photo_.name),
            root.get(Photo_.description),
            root.get(Photo_.thumbnail),
            root.get(Photo_.createdAt)
        )).distinct(true);

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order sortOrder : pageable.getSort()) {
                orders.add(sortOrder.isAscending()
                    ? cb.asc(root.get(sortOrder.getProperty()))
                    : cb.desc(root.get(sortOrder.getProperty()))
                );
            }
            cq.orderBy(orders);
        }

        TypedQuery<PhotoListProjection> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<PhotoListProjection> projections = query.getResultList();

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

        return new PageImpl<>(projections, pageable, total);
    }
}
