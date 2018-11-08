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
    Transaction findById(Integer id) throws AccountNotFoundException;
    void transferMoney(Integer id_acc_sender, Integer id_acc_receiver, Double money) throws AccountNotFoundException, AccountNotEnoughMoneyException;

}
