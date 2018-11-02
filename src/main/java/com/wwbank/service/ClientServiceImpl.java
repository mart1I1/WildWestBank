package com.wwbank.service;

import com.wwbank.dao.ClientDao;
import com.wwbank.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientDao clientDao;

    @Autowired
    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }


    @Override
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    @Override
    public Client findById(Integer id) {
        return null;
    }

    @Override
    public void addClient(Client client) {

    }
}
