package com.wwbank.controller;

import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.exception.client.ClientAlreadyExistException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.client.ClientService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private ClientService clientService;
    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findAllClientsAndBalance(@Valid @ModelAttribute("clientModel") Client client, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        List<Pair<Client, Double>> clientAndBalance = clientService.findAll()
                .stream()
                .map(it -> new Pair<>(
                                it,
                                accountService.findBalanceForClientId(it.getId())
                        )
                )
                .collect(Collectors.toList());
        modelAndView.getModel().put("clientAndBalance", clientAndBalance);
        modelAndView.setViewName("clients");
        return modelAndView;
    }

    @RequestMapping(value = "/add" , method = RequestMethod.GET)
    public String getAddClient() {
        return "addClient";
    }

    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public String addClient(@Valid @ModelAttribute("clientModel") Client client, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "addClient";
            }
            clientService.addClient(client);
        } catch (ClientAlreadyExistException e) {
            result.rejectValue("", "error.client.exist");
            return "addClient";
        }
        return "redirect:/clients";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView clientInfo(@PathVariable("id") Integer id) throws ClientNotFoundException {
        ModelAndView modelAndView = new ModelAndView();
        Client client = clientService.findById(id);
        modelAndView.getModel().put("client", client);
        modelAndView.getModel().put("balance",accountService.findBalanceForClientId(client.getId()));
        modelAndView.getModel().put("accounts", accountService.findAllByClientId(client.getId()));
        modelAndView.setViewName("clientInfo");
        return modelAndView;
    }

    @ModelAttribute("clientModel")
    public Client client() {
        return new Client();
    }



}
