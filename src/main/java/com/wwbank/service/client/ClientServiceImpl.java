package com.wwbank.service.client;

import com.wwbank.dao.client.ClientDAO;
import com.wwbank.entity.Client;
import com.wwbank.exception.client.ClientAlreadyExistException;
import com.wwbank.exception.client.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientDAO clientDao;

    @Autowired
    public void setClientDao(ClientDAO clientDao) {
        this.clientDao = clientDao;
    }


    @Override
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    @Override
    public Client findById(Integer id) throws ClientNotFoundException {
        return clientDao.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public List<Client> findByName(String name) {
        return clientDao.findByName(name);
    }

    @Override
    public Client findByClient(Client client) throws ClientNotFoundException {
        return clientDao.findByClient(client).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public void addClient(Client client) throws ClientAlreadyExistException {
        if (clientDao.findByClient(client).isPresent())
            throw new ClientAlreadyExistException();
        clientDao.save(client);
    }
}
