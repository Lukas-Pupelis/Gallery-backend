package lt.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
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
    public Page<Tuple> findPhotoListBySpec(Specification<Photo> spec, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Photo> root = cq.from(Photo.class);

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);
            if (predicate != null) {
                cq.where(predicate);
            }
        }

        cq.multiselect(
            root.get(Photo_.id).alias("id"),
            root.get(Photo_.name).alias("name"),
            root.get(Photo_.description).alias("description"),
            root.get(Photo_.thumbnail).alias("thumbnail"),
            root.get(Photo_.createdAt).alias("createdAt")
        ).distinct(true);

        if (pageable.getSort().isSorted()) {
            applySorting(pageable, root, cq, cb);
        }

        return getPageResultsAndTotal(cq, spec, pageable, cb);
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
        List<Order> orders = new ArrayList<>();
        for (Sort.Order sortOrder : pageable.getSort()) {
            orders.add(sortOrder.isAscending()
                ? cb.asc(root.get(sortOrder.getProperty()))
                : cb.desc(root.get(sortOrder.getProperty()))
            );
        }
        cq.orderBy(orders);
    }
}
