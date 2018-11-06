package com.wwbank.service.account;

import com.wwbank.dao.account.AccountDAO;
import com.wwbank.dao.account.AccountDAOImpl;
import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.client.ClientService;
import com.wwbank.service.client.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountDAO accountDAO;
    private ClientService clientService;

    private AccountService accountService;

    private Client defaultClient = new Client(1,"BANK", "Wild West", 25);
    private Account defaultAccount = new Account(1,1, 99999.0);

    @BeforeEach
    void setUp() {
        accountDAO = mock(AccountDAOImpl.class);
        clientService = mock(ClientServiceImpl.class);

        AccountServiceImpl accountServiceImpl = new AccountServiceImpl();
        accountServiceImpl.setAccountDAO(accountDAO);
        accountServiceImpl.setClientService(clientService);

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
}