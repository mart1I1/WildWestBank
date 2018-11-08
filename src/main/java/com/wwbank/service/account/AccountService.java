package com.wwbank.service.account;

import com.wwbank.entity.Account;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface AccountService {

    List<Account> findAllByClientId(Integer id_client);
    Account findById(Integer id) throws AccountNotFoundException;
    void createAccountForClientId(Integer id_client) throws ClientNotFoundException;
    void putMoneyById(Integer id_acc, Double money) throws AccountNotFoundException;
    void withdrawMoneyById(Integer id_acc, Double money) throws AccountNotFoundException, AccountNotEnoughMoneyException;

}
