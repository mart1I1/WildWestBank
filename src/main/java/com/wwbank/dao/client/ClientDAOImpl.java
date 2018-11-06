package com.wwbank.dao.client;

import com.wwbank.entity.Client;
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
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
        Root<Client> root = criteria.from(Client.class);
        criteria.select(root);
        return getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return Optional.ofNullable(getCurrentSession().get(Client.class, id));
    }

    @Override
    public List<Client> findByName(String name) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
        Root<Client> root = criteria.from(Client.class);

        Predicate predicate = builder.equal(root.get("name"), name);

        criteria.select(root).where(predicate);

        return getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Client> findByClient(Client client) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
        Root<Client> root = criteria.from(Client.class);

        Predicate predicateName = builder.equal(root.get("name"), client.getName());
        Predicate predicateAddress = builder.equal(root.get("address"), client.getAddress());
        Predicate predicateAge = builder.equal(root.get("age"), client.getAge());

        criteria.select(root).where(builder.and(predicateName, predicateAddress, predicateAge));

        return getCurrentSession().createQuery(criteria).uniqueResultOptional();
    }

    @Override
    public void save(Client client) {
        getCurrentSession().save(client);
    }



    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
