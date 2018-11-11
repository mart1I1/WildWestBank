package com.wwbank.service.transaction;

import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    List<Transaction> findAll();
    List<Transaction> findAllByName(String name);
    List<Transaction> findAllByDate(Date from, Date to);
    List<Transaction> findAllIncludingAccId(Integer accId);
    Transaction findById(Integer id) throws AccountNotFoundException;
    void transferMoney(Transaction transaction) throws AccountNotFoundException, AccountNotEnoughMoneyException;

}
