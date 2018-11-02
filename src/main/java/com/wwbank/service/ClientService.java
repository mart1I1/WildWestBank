package com.wwbank.service;

import com.wwbank.entity.Client;

import java.util.List;

public interface ClientService {

    List<Client> findAll();
    Client findById(Integer id);
    void addClient(Client client);


}
