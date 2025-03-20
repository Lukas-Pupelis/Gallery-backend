package lt.example.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lt.example.entities.Photo;
import lt.example.entities.Photo_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PhotoRepositoryImpl implements PhotoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Photo> findAllWithTags(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Photo> photoCq = cb.createQuery(Photo.class);
        Root<Photo> root = photoCq.from(Photo.class);
        root.fetch(Photo_.tags, JoinType.LEFT);
        photoCq.select(root).distinct(true);

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order sortOrder : pageable.getSort()) {
                orders.add(
                    sortOrder.isAscending() ? cb.asc(root.get(sortOrder.getProperty()))
                    : cb.desc(root.get(sortOrder.getProperty()))
                );
            }
            photoCq.orderBy(orders);
        }

        TypedQuery<Photo> typedQuery = entityManager.createQuery(photoCq);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Photo> photos = typedQuery.getResultList();

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Photo> countRoot = countCq.from(Photo.class);
        countCq.select(cb.count(countRoot));
        Long total = entityManager.createQuery(countCq).getSingleResult();

        return new PageImpl<>(photos, pageable, total);
    }
}
