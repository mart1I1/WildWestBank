package com.wwbank.dao.client;

import com.wwbank.config.PersistenceConfig;
import com.wwbank.entity.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("unit-test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {PersistenceConfig.class, ClientDAOImpl.class},
        loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientDAOImplTest {

    @Autowired
    private ClientDAO clientDAO;
    private Client client = new Client(2,"Test", "test", 1);;
    private Client defaultClient = new Client(1,"BANK", "Wild West", 25);

    @Test
    void findAll() {
        assertEquals(Arrays.asList(defaultClient, client), clientDAO.findAll());
    }

    @Test
    void findById() {
        assertTrue(clientDAO.findById(client.getId()).isPresent());
        assertEquals(client, clientDAO.findById(client.getId()).get());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(clientDAO.findById(client.getId() + 1).isPresent());
    }

    @Test
    void findByName() {
        Client clientWithSameName = new Client(client.getName(), "", 0);
        clientDAO.save(clientWithSameName);
        assertEquals(Arrays.asList(client, clientWithSameName), clientDAO.findByName(clientWithSameName.getName()));
    }

    @Test
    void findByNameNotFound() {
        assertEquals(Collections.emptyList(), clientDAO.findByName("Not found"));
    }

    @Test
    void findByClient() {
        assertTrue(clientDAO.findByClient(client).isPresent());
        assertEquals(client, clientDAO.findByClient(client).get());
    }

    @Test
    void findByClientNotFound() {
        Client clientNotFound = new Client("Not found", "", 0);
        assertFalse(clientDAO.findByClient(clientNotFound).isPresent());
    }

    @Test
    void save() {
        Client newClient = new Client("New", "Somewhere", 10);
        clientDAO.save(newClient);
        assertTrue(clientDAO.findByClient(newClient).isPresent());
    }
}