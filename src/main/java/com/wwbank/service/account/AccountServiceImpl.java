package com.wwbank.service.account;

import com.wwbank.dao.account.AccountDAO;
import com.wwbank.entity.Account;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private AccountDAO accountDAO;
    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public List<Account> findAllByClientId(Integer id_client) {
        return accountDAO.findAllByClientId(id_client);
    }

    @Override
    public Account findById(Integer id) throws AccountNotFoundException {
        return accountDAO.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public void createAccountForClientId(Integer id_client) throws ClientNotFoundException {
        clientService.findById(id_client);
        accountDAO.save(new Account(id_client, 0.0));
    }
}
