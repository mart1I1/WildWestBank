package com.wwbank.controller;

import com.wwbank.entity.Account;
import com.wwbank.entity.Client;
import com.wwbank.entity.Transaction;
import com.wwbank.exception.account.AccountNotEnoughMoneyException;
import com.wwbank.exception.account.AccountNotFoundException;
import com.wwbank.exception.client.ClientNotFoundException;
import com.wwbank.service.account.AccountService;
import com.wwbank.service.client.ClientService;
import com.wwbank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public String getTransferMoney() {
        return "transferView";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transferMoney(@Valid @ModelAttribute("transaction") Transaction transaction, BindingResult result) {
        try {
            if (result.hasErrors())
                return "transferView";
            accountService.transferMoney(transaction);
        } catch (AccountNotFoundException e) {
            result.rejectValue("", "error.account.id");
            return "transferView";
        } catch (AccountNotEnoughMoneyException e) {
            result.rejectValue("money", "error.money.notenough");
            return "transferView";
        }
        return "redirect:/transfer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addAccount(@RequestParam("idClient") Integer idClient) throws ClientNotFoundException {
        accountService.createAccountForClientId(idClient);
        return "redirect:/clients/" + idClient;
    }

    @ModelAttribute("transaction")
    public Transaction transaction() {
        return new Transaction();
    }
}
