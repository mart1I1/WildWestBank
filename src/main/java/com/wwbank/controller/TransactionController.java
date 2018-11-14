package com.wwbank.controller;

import com.wwbank.entity.Transaction;
import com.wwbank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //TODO: не запоминается дата
    @RequestMapping
    public ModelAndView search(@RequestParam(value = "acc_id", required = false) Integer acc_id,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date_from,
                               @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date_to) {
        List<Transaction> transactions = transactionService.findAll();
        if (acc_id != null) {
            List<Transaction> result = transactionService.findAllIncludingAccId(acc_id);
            transactions = transactions.stream().filter(result::contains).collect(Collectors.toList());
        }
        if (name != null) {
            List<Transaction> result = transactionService.findAllByName(name);
            transactions = transactions.stream().filter(result::contains).collect(Collectors.toList());
        }
        if (date_from != null && date_to != null) {
            List<Transaction> result = transactionService.findAllByDate(date_from, date_to);
            transactions = transactions.stream().filter(result::contains).collect(Collectors.toList());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transactionsView");
        modelAndView.getModel().put("transactions", transactions);
        modelAndView.getModel().put("acc_id", acc_id);
        modelAndView.getModel().put("name", name);
        modelAndView.getModel().put("date_from", prepareDateForOutput(date_from));
        modelAndView.getModel().put("date_to", prepareDateForOutput(date_to));
        return modelAndView;
    }

    private String prepareDateForOutput(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return date != null ? simpleDateFormat.format(date) : null;
    }



}
