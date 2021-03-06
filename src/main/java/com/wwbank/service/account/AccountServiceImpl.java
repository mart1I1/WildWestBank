package com.wwbank.service.account;

import com.wwbank.dao.account.AccountDAO;
import com.wwbank.entity.Account;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.client.ClientService;
import com.wwbank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountDAO accountDAO;
    private ClientService clientService;
    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public List<Account> findAllByClientId(Integer idClient) {
        return accountDAO.findAllByClientId(idClient);
    }

    @Override
    public Account findById(Integer id) throws AccountNotFoundException {
        return accountDAO.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public Double findBalanceForClientId(Integer idClient) {
        return findAllByClientId(idClient).stream().mapToDouble(Account::getMoney).sum();
    }

    @Override
    public void createAccountForClientId(Integer idClient) throws ClientNotFoundException {
        clientService.findById(idClient);
        accountDAO.save(new Account(idClient, 0.0));
    }

    @Override
    public void putMoneyById(Integer idAcc, Double money) throws AccountNotFoundException {
        Account account = findById(idAcc);
        account.setMoney(account.getMoney() + money);
        accountDAO.update(account);
    }

    @Override
    public void withdrawMoneyById(Integer idAcc, Double money)
            throws AccountNotFoundException, AccountNotEnoughMoneyException {
        Account account = findById(idAcc);
        if (account.getMoney() - money < 0) throw new AccountNotEnoughMoneyException();
        account.setMoney(account.getMoney() - money);
        accountDAO.update(account);
    }

    //TODO: добавить тест rollback
    @Override
    @Transactional(rollbackOn = {AccountNotFoundException.class, AccountNotEnoughMoneyException.class})
    public void transferMoney(Transaction transaction)
            throws AccountNotFoundException, AccountNotEnoughMoneyException {
        Account accountSender = findById(transaction.getIdAccSender());
        Account accountReceiver = findById(transaction.getIdAccReceiver());
        withdrawMoneyById(accountSender.getId(), transaction.getMoney());
        putMoneyById(accountReceiver.getId(), transaction.getMoney());
        transactionService.save(transaction);
    }
}
