package com.wwbank.util;

import com.wwbank.dao.client.ClientDAO;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CriteriaHelper {

    private Class clazz;
    private Session session;
    private CriteriaBuilder builder;
    private CriteriaQuery criteriaQuery;
    private Root root;

    public CriteriaQuery getCriteriaQuery() {
        return criteriaQuery;
    }

    public Class getClazz() {
        return clazz;
    }

    public Session getSession() {
        return session;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public CriteriaHelper(Session session, Class<? extends Criteriable> clazz) {
        this.session = session;
        this.clazz = clazz;
        this.builder = session.getCriteriaBuilder();
        createCriteriaQuery();
    }

    private void createCriteriaQuery() {
        CriteriaQuery criteria = builder.createQuery(clazz);
        root = criteria.from(clazz);
        criteria.select(root);
        criteriaQuery = criteria;
    }

    public Predicate createEqualPredicate(String field, Object value) {
        return builder.equal(root.get(field), value);
    }

    public Predicate createBetweenDatePredicate(String field, Date from, Date to) {
        return builder.between(root.get(field), from, to);
    }

    public void addAndPredicates(Predicate ... predicates) {
        criteriaQuery.where(builder.and(predicates));
    }

    public Optional getUniqueResultOptional() {
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    public List getResultList() {
        return session.createQuery(criteriaQuery).getResultList();
    }


}
