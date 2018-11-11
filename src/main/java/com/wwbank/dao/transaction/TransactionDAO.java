package com.wwbank.dao.transaction;

import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionDAO {

    List<Transaction> findAll();
    List<Transaction> findAllBetweenDates(Date from, Date to);
    List<Transaction> findAllBySenderId(Integer id_acc_sender);
    List<Transaction> findAllByReceiverId(Integer id_acc_receiver);
    Optional<Transaction> findById(Integer id);
    void save(Transaction transaction);

}
