package com.wwbank.dao.transaction;

import com.wwbank.entity.Account;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.util.CriteriaHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class TransactionDAOImpl implements TransactionDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> findAll() {
        CriteriaHelper criteriaHelper = new CriteriaHelper<>(getCurrentSession(), Transaction.class);
        return criteriaHelper.getResultList();
    }


    @Override
    public List<Transaction> findAllBetweenDates(Date from, Date to) {
        CriteriaHelper criteriaHelper = new CriteriaHelper<>(getCurrentSession(), Transaction.class);
        Predicate predicate = criteriaHelper.createBetweenDatePredicate("date", from, to);
        criteriaHelper.addAndPredicates(predicate);
        return criteriaHelper.getResultList();
    }

//    @Override
//    public List<Transaction> findAllForSenderAcc(Integer id_acc_sender) {
//        CriteriaHelper criteriaHelper = new CriteriaHelper<>(getCurrentSession(), Transaction.class);
//        Predicate predicate = criteriaHelper.createEqualPredicate("id_acc_sender", id_acc_sender);
//        criteriaHelper.addAndPredicates(predicate);
//        return criteriaHelper.getResultList();
//    }
//
//    @Override
//    public List<Transaction> findAllForReceiverAcc(Integer id_acc_receiver) {
//        CriteriaHelper criteriaHelper = new CriteriaHelper<>(getCurrentSession(), Transaction.class);
//        Predicate predicate = criteriaHelper.createEqualPredicate("id_acc_receiver", from, to);
//        criteriaHelper.addAndPredicates(predicate);
//        return criteriaHelper.getResultList();
//    }

    @Override
    public Optional<Transaction> findById(Integer id) {
        return Optional.ofNullable(getCurrentSession().get(Transaction.class, id));
    }

    @Override
    public void save(Transaction transaction) {
        getCurrentSession().save(transaction);
    }


    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
