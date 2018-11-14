package com.wwbank.controller;

import com.wwbank.config.PersistenceConfig;
import com.wwbank.config.WebConfig;
import com.wwbank.entity.Transaction;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionController transactionController;

    private Date date1 = new Date(2000, 1, 1);
    private Date date2 = new Date(2010, 1, 1);
    private Date date3 = new Date(2015, 1, 1);

    private Transaction transaction1 = new Transaction(1, 2, date1, 100.0);
    private Transaction transaction2 = new Transaction(1, 2, date2, 200.0);
    private Transaction transaction3 = new Transaction(2, 3, date3, 300.0);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        when(transactionService.findAll()).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));
    }

    @Test
    void searchWithoutFilter() throws Exception {
        mockMvc.perform(get("/transactions"))
                .andExpect(view().name("transactionsView"))
                .andExpect(model().attribute("transactions", Arrays.asList(transaction1, transaction2, transaction3)));
        verify(transactionService, times(1)).findAll();
        verify(transactionService, times(0)).findAllByName(anyString());
        verify(transactionService, times(0)).findAllIncludingAccId(anyInt());
        verify(transactionService, times(0)).findAllByDate(any(Date.class), any(Date.class));
    }

    @Test
    void searchAccIdFilter() throws Exception {
        Integer accId = 1;
        when(transactionService.findAllIncludingAccId(accId)).thenReturn(Arrays.asList(transaction1, transaction2));
        mockMvc.perform(get("/transactions").param("acc_id", accId.toString()))
                .andExpect(view().name("transactionsView"))
                .andExpect(model().attribute("transactions", Arrays.asList(transaction1, transaction2)))
                .andExpect(model().attribute("acc_id", accId));
        verify(transactionService, times(1)).findAll();
        verify(transactionService, times(0)).findAllByName(anyString());
        verify(transactionService, times(1)).findAllIncludingAccId(anyInt());
        verify(transactionService, times(0)).findAllByDate(any(Date.class), any(Date.class));
    }

    @Test
    void searchNameFilter() throws Exception {
        String name = "Test";
        when(transactionService.findAllByName(name)).thenReturn(Arrays.asList(transaction1, transaction2));
        mockMvc.perform(get("/transactions").param("name", name))
                .andExpect(view().name("transactionsView"))
                .andExpect(model().attribute("transactions", Arrays.asList(transaction1, transaction2)))
                .andExpect(model().attribute("name", name));
        verify(transactionService, times(1)).findAll();
        verify(transactionService, times(1)).findAllByName(anyString());
        verify(transactionService, times(0)).findAllIncludingAccId(anyInt());
        verify(transactionService, times(0)).findAllByDate(any(Date.class), any(Date.class));
    }

    @Test
    void searchDateFilter() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        when(transactionService.findAllByDate(date1, date2)).thenReturn(Arrays.asList(transaction1, transaction2));
        mockMvc.perform(get("/transactions")
                .param("date_from", df.format(date1))
                .param("date_to", df.format(date2))
        )
                .andExpect(view().name("transactionsView"))
                .andExpect(model().attribute("transactions", Arrays.asList(transaction1, transaction2)))
                .andExpect(model().attribute("date_from", df.format(date1)))
                .andExpect(model().attribute("date_to", df.format(date2)));
        verify(transactionService, times(1)).findAll();
        verify(transactionService, times(0)).findAllByName(anyString());
        verify(transactionService, times(0)).findAllIncludingAccId(anyInt());
        verify(transactionService, times(1)).findAllByDate(any(Date.class), any(Date.class));
    }
}