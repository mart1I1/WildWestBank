package com.wwbank.service;

import com.wwbank.entity.Account;
import com.wwbank.entity.Client;

import java.util.List;

public interface AccountService {

    List<Account> findAllByClient(Client client);
    Account findById(Integer id);
    void createAccount(Client client);


}
