package com.wwbank.dao.client;

import com.wwbank.entity.Client;
import com.wwbank.service.client.ClientServiceImpl;
import com.wwbank.util.CriteriaHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class ClientDAOImpl implements ClientDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Client> findAll() {
        CriteriaHelper criteriaHelper = new CriteriaHelper(getCurrentSession(), Client.class);
        return criteriaHelper.getResultList();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return Optional.ofNullable(getCurrentSession().get(Client.class, id));
    }

    @Override
    public List<Client> findByName(String name) {
        CriteriaHelper criteriaHelper = new CriteriaHelper(getCurrentSession(), Client.class);
        Predicate predicate = criteriaHelper.createEqualPredicate("name", name);
        criteriaHelper.addAndPredicates(predicate);
        return criteriaHelper.getResultList();
    }

    @Override
    public Optional<Client> findByClient(Client client) {
        CriteriaHelper criteriaHelper = new CriteriaHelper(getCurrentSession(), Client.class);
        Predicate predicateName = criteriaHelper.createEqualPredicate("name", client.getName());
        Predicate predicateAddress = criteriaHelper.createEqualPredicate("address", client.getAddress());
        Predicate predicateAge = criteriaHelper.createEqualPredicate("age", client.getAge());
        criteriaHelper.addAndPredicates(predicateName, predicateAddress, predicateAge);
        return criteriaHelper.getUniqueResultOptional();
    }

    @Override
    public void save(Client client) {
        getCurrentSession().save(client);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
