package com.wwbank.dao;

import com.wwbank.entity.Client;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class ClientDaoImpl implements ClientDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Client> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
        return getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    public void save(Client client) {
        getCurrentSession().save(client);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
