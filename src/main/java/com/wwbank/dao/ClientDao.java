package com.wwbank.dao;

import com.wwbank.entity.Client;

import java.util.List;

public interface ClientDao {

    List<Client> findAll();
    void save(Client client);

}
