package com.wwbank.service.client;

import com.wwbank.dao.client.ClientDAO;
import com.wwbank.dao.client.ClientDAOImpl;
import com.wwbank.entity.Client;
import com.wwbank.exception.client.ClientAlreadyExistException;
import com.wwbank.exception.client.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientDAO clientDAO;
    @InjectMocks
    private ClientServiceImpl clientService;

    private Client defaultClient = new Client(1,"BANK", "Wild West", 25);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll() {
        clientService.findAll();
        verify(clientDAO, times(1)).findAll();
    }

    @Test
    void findById() throws ClientNotFoundException {
        when(clientDAO.findById(defaultClient.getId())).thenReturn(Optional.ofNullable(defaultClient));
        assertDoesNotThrow(() -> clientService.findById(defaultClient.getId()));
        assertEquals(defaultClient, clientService.findById(defaultClient.getId()));
    }

    @Test
    void findByIdNotFound() {
        when(clientDAO.findById(defaultClient.getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.findById(defaultClient.getId()));
    }

    @Test
    void findByName() {
        clientService.findByName(defaultClient.getName());
        verify(clientDAO, times(1)).findByName(defaultClient.getName());
    }


    @Test
    void findByClient() throws ClientNotFoundException {
        when(clientDAO.findByClient(defaultClient)).thenReturn(Optional.ofNullable(defaultClient));
        assertDoesNotThrow(() -> clientService.findByClient(defaultClient));
        assertEquals(defaultClient, clientService.findByClient(defaultClient));
    }

    @Test
    void findByClientNotFound() {
        when(clientDAO.findByClient(defaultClient)).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.findByClient(defaultClient));
    }

    @Test
    void addClient() throws ClientAlreadyExistException {
        when(clientDAO.findByClient(defaultClient)).thenReturn(Optional.empty());
        clientService.addClient(defaultClient);
        verify(clientDAO, times(1)).save(defaultClient);
    }

    @Test
    void addClientAlreadyExist() {
        when(clientDAO.findByClient(defaultClient)).thenReturn(Optional.ofNullable(defaultClient));
        assertThrows(ClientAlreadyExistException.class, () -> clientService.addClient(defaultClient));
    }
}