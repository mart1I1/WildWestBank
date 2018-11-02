package com.wwbank.service;

import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    List<Transaction> findAll();
    List<Transaction> findAllByName(String name);
    List<Transaction> findAllByDate(Date from, Date to);
    Transaction findById(Integer id);
    void transferMoney(Client sender, Client receiver, Double money);

}
