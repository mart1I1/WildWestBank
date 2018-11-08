package com.wwbank.service.transaction;

import com.wwbank.dao.transaction.TransactionDAO;
import com.wwbank.entity.Account;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionDAO transactionDAO;
    private ClientService clientService;
    private AccountService accountService;

    @Autowired
    public void setTransactionDAO(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionDAO.findAll();
    }

    @Override
    public List<Transaction> findAllByName(String name) {
        List<Integer> accounts = clientService.findByName(name)
                .stream()
                .map((it) -> accountService.findAllByClientId(it.getId()))
                .flatMap(List::stream)
                .map(Account::getId)
                .collect(Collectors.toList());
        return transactionDAO.findAll()
                .stream()
                .filter((it) -> accounts.contains(it.getIdAccReceiver()) || accounts.contains(it.getIdAccSender()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAllByDate(Date from, Date to) {
        return transactionDAO.findAllBetweenDates(from, to);
    }

    @Override
    public Transaction findById(Integer id) throws AccountNotFoundException {
        return transactionDAO.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    @Transactional(rollbackOn = {AccountNotFoundException.class, AccountNotEnoughMoneyException.class})
    public void transferMoney(Integer id_acc_sender, Integer id_acc_receiver, Double money)
            throws AccountNotFoundException, AccountNotEnoughMoneyException {
        Account accountSender = accountService.findById(id_acc_sender);
        Account accountReceiver = accountService.findById(id_acc_receiver);
        if (accountSender.getMoney() - money < 0)
            throw new AccountNotEnoughMoneyException();
        accountSender.setMoney(accountSender.getMoney() - money);
        accountReceiver.setMoney(accountReceiver.getMoney() + money);
        transactionDAO.save(new Transaction(accountSender.getId(), accountReceiver.getId(), new Date(), money));
    }
}
