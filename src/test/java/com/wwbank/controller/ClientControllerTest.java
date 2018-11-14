package com.wwbank.controller;

import com.wwbank.config.PersistenceConfigTest;
import com.wwbank.config.WebConfig;
import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.exception.client.ClientAlreadyExistException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.client.ClientService;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: разобрать с тестированием валидации @Valid

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigTest.class, ClientController.class, ExceptionHandlingController.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private ClientController clientController;

    @Autowired
    private ExceptionHandlingController exController;

    private Client defaultClient = new Client(1,"BANK", "Wild West", 25);
    private Client client = new Client(2,"test", "testAddress", 1);

    private Account defaultAccount = new Account(1, 1, 10.0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController, exController).build();
    }

    @Test
    void findAllClientsAndBalance() throws Exception {
        when(clientService.findAll()).thenReturn(Arrays.asList(defaultClient, client));
        when(accountService.findBalanceForClientId(defaultAccount.getId())).thenReturn(defaultAccount.getMoney());
        when(accountService.findBalanceForClientId(client.getId())).thenReturn(0.0);
        List<Pair<Client, Double>> clientAndBalance = new ArrayList<>();
        clientAndBalance.add(new Pair<>(defaultClient, defaultAccount.getMoney()));
        clientAndBalance.add(new Pair<>(client, 0.0));
        mockMvc.perform(get("/clients"))
                .andExpect(view().name("clientsView"))
                .andExpect(model().attribute("clientAndBalance", clientAndBalance));
    }

    @Test
    void getAddClient() throws Exception {
        mockMvc.perform(get("/clients/add"))
                .andExpect(view().name("addClientView"));
    }

    @Test
    void addClient() throws Exception {
        mockMvc.perform(post("/clients/add").flashAttr("clientModel", client))
                .andExpect(redirectedUrl("/clients"));
        verify(clientService, times(1)).addClient(client);
    }

    @Test
    void addClientAlreadyExist() throws Exception {
        doThrow(ClientAlreadyExistException.class).when(clientService).addClient(defaultClient);
        mockMvc.perform(post("/clients/add").flashAttr("clientModel", defaultClient))
                .andExpect(view().name("addClientView"))
                .andExpect(model().attributeHasFieldErrors("clientModel"));
        verify(clientService, times(1)).addClient(defaultClient);
    }

    @Test
    void clientInfo() throws Exception {
        Double balance = 10.0;
        List<Account> accounts = Collections.singletonList(defaultAccount);
        when(clientService.findById(defaultClient.getId())).thenReturn(defaultClient);
        when(accountService.findBalanceForClientId(defaultClient.getId())).thenReturn(balance);
        when(accountService.findAllByClientId(defaultClient.getId())).thenReturn(accounts);

        mockMvc.perform(get("/clients/" + defaultAccount.getId()))
                .andExpect(view().name("clientInfoView"))
                .andExpect(model().attribute("client", defaultClient))
                .andExpect(model().attribute("balance", balance))
                .andExpect(model().attribute("accounts", accounts));
    }

    @Test
    void clientInfoClientNotFound() throws Exception {
        when(clientService.findById(anyInt())).thenThrow(ClientNotFoundException.class);
        mockMvc.perform(get("/clients/" + anyInt()))
                .andExpect(status().isNotFound());
    }
}