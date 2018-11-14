package com.wwbank.dao.account;

import com.wwbank.dao.client.ClientDAOImpl;
import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.util.CriteriaHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class AccountDAOImpl implements AccountDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Account> findAllByClientId(Integer id_client) {
        CriteriaHelper criteriaHelper = new CriteriaHelper(getCurrentSession(), Account.class);
        Predicate predicate = criteriaHelper.createEqualPredicate("idClient", id_client);
        criteriaHelper.addAndPredicates(predicate);
        return criteriaHelper.getResultList();
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return Optional.ofNullable(getCurrentSession().get(Account.class, id));
    }

    @Override
    public void save(Account account) {
        getCurrentSession().save(account);
    }

    @Override
    public void update(Account account) {
        getCurrentSession().update(account);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
