package com.wwbank.service.client;

import com.wwbank.entity.Client;
import com.wwbank.exception.client.ClientAlreadyExistException;
import com.wwbank.exception.client.ClientNotFoundException;

import java.util.List;

public interface ClientService {

    List<Client> findAll();
    List<Client> findByName(String name);
    Client findById(Integer id) throws ClientNotFoundException;
    Client findByClient(Client client) throws ClientNotFoundException;
    void addClient(Client client) throws ClientAlreadyExistException;

}
