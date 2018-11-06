package com.wwbank.service.account;

import com.wwbank.entity.Account;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;

import java.util.List;

public interface AccountService {

    List<Account> findAllByClientId(Integer id_client);
    Account findById(Integer id) throws AccountNotFoundException;
    void createAccountForClientId(Integer id_client) throws ClientNotFoundException;

}
