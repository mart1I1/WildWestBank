package com.wwbank.service.account;

import com.wwbank.entity.Account;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface AccountService {

    List<Account> findAllByClientId(Integer idClient);
    Account findById(Integer id) throws AccountNotFoundException;
    Double findBalanceForClientId(Integer idClient);
    void createAccountForClientId(Integer idClient) throws ClientNotFoundException;
    void putMoneyById(Integer idAcc, Double money) throws AccountNotFoundException;
    void withdrawMoneyById(Integer idAcc, Double money) throws AccountNotFoundException, AccountNotEnoughMoneyException;
    void transferMoney(Transaction transaction) throws AccountNotFoundException, AccountNotEnoughMoneyException;

}
