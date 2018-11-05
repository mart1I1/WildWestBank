package com.wwbank.dao;

import com.wwbank.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDAO {

    List<Client> findAll();
    Optional<Client> findById(Integer id);
    List<Client> findByName(String name);
    Optional<Client> findByClient(Client client);
    void save(Client client);

}
