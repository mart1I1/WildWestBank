package com.wwbank.controller;

import com.wwbank.config.PersistenceConfigTest;
import com.wwbank.config.WebConfig;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//TODO: проверка @Valid

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigTest.class, WebConfig.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    private Transaction transaction = new Transaction(1, 2, new Date(), 10.0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void getTransferMoney() throws Exception {
        mockMvc.perform(get("/accounts/transfer"))
                .andExpect(view().name("transferView"));
    }

    @Test
    void transferMoney() throws Exception {
        mockMvc.perform(post("/accounts/transfer").flashAttr("transaction", transaction))
                .andExpect(redirectedUrl("/transfer"));
        verify(accountService, times(1)).transferMoney(transaction);
    }

    @Test
    void transferMoneyAccountNotEnoughMoney() throws Exception {
        doThrow(AccountNotEnoughMoneyException.class).when(accountService).transferMoney(transaction);
        mockMvc.perform(post("/accounts/transfer").flashAttr("transaction", transaction))
                .andExpect(model().attributeHasFieldErrors("transaction","money"))
                .andExpect(view().name("transferView"));
    }

    @Test
    void transferMoneyNotValidId() throws Exception {
        mockMvc.perform(post("/accounts/transfer").flashAttr("transaction", transaction))
                .andExpect(model().attributeHasFieldErrors("transaction","money"))
                .andExpect(view().name("transferView"));
    }

    @Test
    void addAccount() throws Exception {
        Integer idClient = 1;
        mockMvc.perform(post("/accounts/add").param("idClient", idClient.toString()))
                .andExpect(redirectedUrl("/clients/" + idClient));
        verify(accountService, times(1)).createAccountForClientId(idClient);
    }
}