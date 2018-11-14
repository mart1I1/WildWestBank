package com.wwbank.service.account;

import com.wwbank.dao.account.AccountDAO;
import com.wwbank.dao.account.AccountDAOImpl;
import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.client.ClientService;
import com.wwbank.service.client.ClientServiceImpl;
import com.wwbank.service.transaction.TransactionService;
import com.wwbank.service.transaction.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.zip.DeflaterOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountDAO accountDAO;
    private ClientService clientService;

    private AccountService accountService;
    private TransactionService transactionService;

    private Client defaultClient = new Client(1,"BANK", "Wild West", 25);
    private Account defaultAccount = new Account(1,1, 99999.0);

    private Account account11 = new Account(1, 1, 10.0);
    private Account account12 = new Account(2, 1, 20.0);

    private Date date1 = new Date(2000, 1, 1);

    private Transaction transaction12 = new Transaction(1, 2, date1, 100.0);
    private Transaction transaction21 = new Transaction(2, 1, date1, 10.0);

    @BeforeEach
    void setUp() {
        accountDAO = mock(AccountDAOImpl.class);
        clientService = mock(ClientServiceImpl.class);
        transactionService = mock(TransactionServiceImpl.class);

        AccountServiceImpl accountServiceImpl = new AccountServiceImpl();
        accountServiceImpl.setAccountDAO(accountDAO);
        accountServiceImpl.setClientService(clientService);
        accountServiceImpl.setTransactionService(transactionService);

        accountService = accountServiceImpl;
    }

    @Test
    void findAllByClientId() {
        accountService.findAllByClientId(defaultClient.getId());
        verify(accountDAO, times(1)).findAllByClientId(defaultClient.getId());
    }

    @Test
    void findById() {
        when(accountDAO.findById(defaultAccount.getId())).thenReturn(Optional.ofNullable(defaultAccount));
        assertDoesNotThrow(() -> accountService.findById(defaultAccount.getId()));
        verify(accountDAO, times(1)).findById(defaultAccount.getId());
    }

    @Test
    void findByIdNotFound() {
        when(accountDAO.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.findById(anyInt()));
        verify(accountDAO, times(0)).findById(defaultAccount.getId());
    }

    @Test
    void createAccountForClientId() throws ClientNotFoundException {
        when(clientService.findById(anyInt())).thenReturn(defaultClient);
        assertDoesNotThrow(() -> accountService.createAccountForClientId(defaultClient.getId()));
        verify(accountDAO, times(1)).save(new Account(defaultClient.getId(), 0.0));
    }

    @Test
    void createAccountForClientIdClientNotFound() throws ClientNotFoundException {
        when(clientService.findById(anyInt())).thenThrow(ClientNotFoundException.class);
        assertThrows(ClientNotFoundException.class, () -> accountService.createAccountForClientId(defaultClient.getId()));
        verify(accountDAO, times(0)).save(new Account(defaultClient.getId(), 0.0));
    }

    @Test
    void putMoneyById() throws AccountNotFoundException {
        Double delta = 1234.0;
        Double money = defaultAccount.getMoney();

        when(accountDAO.findById(defaultAccount.getId())).thenReturn(Optional.ofNullable(defaultAccount));

        accountService.putMoneyById(defaultAccount.getId(), delta);
        money += delta;

        verify(accountDAO, times(1)).update(defaultAccount);
        assertEquals(money, defaultAccount.getMoney());
    }

    @Test
    void putMoneyByIdNotFound() {
        when(accountDAO.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.putMoneyById(defaultAccount.getId(), anyDouble()));
    }

    @Test
    void withdrawMoneyById() throws AccountNotFoundException, AccountNotEnoughMoneyException {
        Double delta = 1234.0;
        Double money = defaultAccount.getMoney();

        when(accountDAO.findById(defaultAccount.getId())).thenReturn(Optional.ofNullable(defaultAccount));

        accountService.withdrawMoneyById(defaultAccount.getId(), delta);
        money -= delta;

        verify(accountDAO, times(1)).update(defaultAccount);
        assertEquals(money, defaultAccount.getMoney());
    }

    @Test
    void withdrawMoneyByIdNotEnoughMoney() {
        Double delta = 1234.0;
        Double money = defaultAccount.getMoney();

        when(accountDAO.findById(defaultAccount.getId())).thenReturn(Optional.ofNullable(defaultAccount));
        assertThrows(
                AccountNotEnoughMoneyException.class,
                () -> accountService.withdrawMoneyById(defaultAccount.getId(), money + delta)
        );
    }

    @Test
    void withdrawMoneyByIdNotFound() {
        when(accountDAO.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.withdrawMoneyById(defaultAccount.getId(), anyDouble())
        );
    }

    @Test
    void findBalanceForClientId() {
        List<Account> list = Arrays.asList(defaultAccount);
        Double balance = list.stream().mapToDouble(Account::getMoney).sum();
        when(accountDAO.findAllByClientId(defaultClient.getId())).thenReturn(list);
        assertEquals(balance, accountService.findBalanceForClientId(defaultClient.getId()));
    }

    @Test
    void transferMoney() throws AccountNotEnoughMoneyException, AccountNotFoundException {
        Double money11 = account11.getMoney() + transaction21.getMoney();
        Double money12 = account12.getMoney() - transaction21.getMoney();
        when(accountDAO.findById(account11.getId())).thenReturn(Optional.ofNullable(account11));
        when(accountDAO.findById(account12.getId())).thenReturn(Optional.ofNullable(account12));

        accountService.transferMoney(transaction21);
        verify(accountDAO, times(2)).findById(transaction12.getIdAccSender());
        verify(accountDAO, times(2)).findById(transaction12.getIdAccReceiver());
        verify(accountDAO, times(1)).update(account11);
        verify(accountDAO, times(1)).update(account12);
        verify(transactionService, times(1)).save(transaction21);
        assertAll(() -> assertEquals(money11, account11.getMoney()),
                () -> assertEquals(money12, account12.getMoney()));
    }

    @Test
    void transferMoneyAccountNotFound() throws AccountNotFoundException {
        when(accountDAO.findById(account11.getId())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.transferMoney(transaction12));
    }

    @Test
    void transferMoneyAccountNotFound2() {
        when(accountDAO.findById(account11.getId())).thenReturn(Optional.ofNullable(account11));
        when(accountDAO.findById(account12.getId())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.transferMoney(transaction12));
    }

    @Test
    void transferMoneyNotEnough() {
        when(accountDAO.findById(account11.getId())).thenReturn(Optional.ofNullable(account11));
        when(accountDAO.findById(account12.getId())).thenReturn(Optional.ofNullable(account12));

        assertThrows(AccountNotEnoughMoneyException.class, () -> accountService.transferMoney(transaction12));
    }

}