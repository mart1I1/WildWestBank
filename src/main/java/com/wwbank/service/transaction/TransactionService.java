package com.wwbank.service.transaction;

import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.transaction.TransactionNotFoundException;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    void save(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findAllByName(String name);
    List<Transaction> findAllByDate(Date from, Date to);
    List<Transaction> findAllIncludingAccId(Integer accId);
    Transaction findById(Integer id) throws TransactionNotFoundException;

}
