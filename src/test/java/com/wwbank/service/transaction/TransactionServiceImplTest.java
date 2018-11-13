package com.wwbank.service.transaction;

import com.wwbank.dao.transaction.TransactionDAO;
import com.wwbank.dao.transaction.TransactionDAOImpl;
import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.transaction.TransactionNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.account.AccountServiceImpl;
import com.wwbank.service.client.ClientService;
import com.wwbank.service.client.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private ClientService clientService;
    private AccountService accountService;
    private TransactionDAO transactionDAO;

    private TransactionService transactionService;

    private Client client1 = new Client(1,"client1", "address1", 1);
    private Client client2 = new Client(2,"client2", "address2", 2);

    private Account account11 = new Account(1, 1, 10.0);
    private Account account12 = new Account(2, 1, 20.0);
    private Account account31 = new Account(3, 2, 30.0);

    private Date date1 = new Date(2000, 1, 1);
    private Date date2 = new Date();

    private Transaction transaction12 = new Transaction(1, 2, date1, 10.0);
    private Transaction transaction23 = new Transaction(2, 3, date2, 10.0);

    @BeforeEach
    void setUp() {
        clientService = mock(ClientServiceImpl.class);
        accountService = mock(AccountServiceImpl.class);
        transactionDAO = mock(TransactionDAOImpl.class);

        when(clientService.findByName(client1.getName())).thenReturn(Collections.singletonList(client1));
        when(clientService.findByName(client2.getName())).thenReturn(Collections.singletonList(client2));
        when(accountService.findAllByClientId(client1.getId())).thenReturn(Arrays.asList(account11, account12));
        when(accountService.findAllByClientId(client2.getId())).thenReturn(Collections.singletonList(account31));
        when(transactionDAO.findAll()).thenReturn(Arrays.asList(transaction12, transaction23));

        TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
        transactionServiceImpl.setAccountService(accountService);
        transactionServiceImpl.setClientService(clientService);
        transactionServiceImpl.setTransactionDAO(transactionDAO);

        transactionService = transactionServiceImpl;
    }

    @Test
    void findAll() {
        assertEquals(Arrays.asList(transaction12, transaction23), transactionService.findAll());
    }

    @Test
    void findAllByName() {
        assertAll(() -> assertEquals(Arrays.asList(transaction12, transaction23), transactionService.findAllByName(client1.getName())),
                () -> assertEquals(Collections.singletonList(transaction23), transactionService.findAllByName(client2.getName())));
    }

    @Test
    void findAllByDate() {
        transactionService.findAllByDate(date1, date2);
        verify(transactionDAO, times(1)).findAllBetweenDates(date1, date2);
    }

    @Test
    void findAllIncludingAccId() {
        Integer accId = account12.getId();
        when(transactionDAO.findAllByReceiverId(accId)).thenReturn(new ArrayList<>(Arrays.asList(transaction12)));
        when(transactionDAO.findAllBySenderId(accId)).thenReturn(Arrays.asList(transaction23));
        assertEquals(Arrays.asList(transaction12, transaction23), transactionService.findAllIncludingAccId(accId));
    }

    @Test
    void findById() throws TransactionNotFoundException {
        when(transactionDAO.findById(transaction12.getId())).thenReturn(Optional.ofNullable(transaction12));
        assertDoesNotThrow(() -> transactionService.findById(transaction12.getId()));
        assertEquals(transaction12, transactionService.findById(transaction12.getId()));

    }

    @Test
    void findByIdNotFound() {
        when(transactionDAO.findById(transaction12.getId())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.findById(transaction12.getId()));
    }

    @Test
    void transferMoney() throws AccountNotEnoughMoneyException, AccountNotFoundException {
        when(accountService.findById(account11.getId())).thenReturn(account11);
        when(accountService.findById(account12.getId())).thenReturn(account12);

        transactionService.transferMoney(transaction12);
        verify(accountService, times(1)).findById(transaction12.getIdAccSender());
        verify(accountService, times(1)).findById(transaction12.getIdAccReceiver());
        verify(accountService, times(1)).withdrawMoneyById(account11.getId(), transaction12.getMoney());
        verify(accountService, times(1)).putMoneyById(account12.getId(), transaction12.getMoney());
        verify(transactionDAO, times(1)).save(transaction12);
    }

    @Test
    void transferMoneyAccountNotFound() throws AccountNotFoundException {
        when(accountService.findById(account11.getId())).thenThrow(AccountNotFoundException.class);
        assertThrows(AccountNotFoundException.class, () -> transactionService.transferMoney(transaction12));
    }

    @Test
    void transferMoneyAccountNotFound2() throws AccountNotFoundException {
        when(accountService.findById(account11.getId())).thenReturn(account11);
        when(accountService.findById(account12.getId())).thenThrow(AccountNotFoundException.class);
        assertThrows(AccountNotFoundException.class, () -> transactionService.transferMoney(transaction12));
    }

    @Test
    void transferMoneyNotEnough() throws AccountNotFoundException, AccountNotEnoughMoneyException {
        when(accountService.findById(account11.getId())).thenReturn(account11);
        when(accountService.findById(account12.getId())).thenReturn(account12);
        doThrow(AccountNotEnoughMoneyException.class).when(accountService).withdrawMoneyById(account11.getId(), account11.getMoney());

        assertThrows(AccountNotEnoughMoneyException.class, () -> transactionService.transferMoney(transaction12));
    }
}