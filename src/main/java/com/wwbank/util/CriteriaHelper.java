package com.wwbank.util;

import com.wwbank.dao.client.ClientDAO;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CriteriaHelper<T extends Criteriable> {

    private Class<T> clazz;
    private Session session;
    private CriteriaBuilder builder;
    private CriteriaQuery<T> criteriaQuery;
    private Root<T> root;

    public CriteriaQuery<T> getCriteriaQuery() {
        return criteriaQuery;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Session getSession() {
        return session;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public CriteriaHelper(Session session, Class<T> clazz) {
        this.session = session;
        this.clazz = clazz;
        this.builder = session.getCriteriaBuilder();
        createCriteriaQuery();
    }

    private void createCriteriaQuery() {
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        root = criteria.from(clazz);
        criteria.select(root);
        criteriaQuery = criteria;
    }

    public Predicate createEqualPredicate(String field, Object value) {
        return builder.equal(root.get(field), value);
    }

    public void addAndPredicates(Predicate ... predicates) {
        criteriaQuery.where(builder.and(predicates));
    }

    public Optional<T> getUniqueResultOptional() {
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    public List<T> getResultList() {
        return session.createQuery(criteriaQuery).getResultList();
    }


}
