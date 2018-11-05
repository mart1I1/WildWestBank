package com.wwbank.controller;

import com.wwbank.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findAllClients() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("clients", clientService.findAll());
        modelAndView.setViewName("clients");
        return modelAndView;
    }



}
