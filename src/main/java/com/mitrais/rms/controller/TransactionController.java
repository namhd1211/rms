package com.mitrais.rms.controller;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class TransactionController {
    private static final String TRANSACTION_TRANSFER = "transaction/transfer";
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/withdraw")
    public String withDraw(Model model) {
        TransactionDTO transaction = new TransactionDTO();
        model.addAttribute("transaction", transaction);
        return "transaction/withdraw";
    }

    @GetMapping("/otherWithdraw")
    public String otherWithDraw(Model model) {
        TransactionDTO transaction = new TransactionDTO();
        model.addAttribute("transaction", transaction);
        return "transaction/otherWithdraw";
    }

    @GetMapping("/transaction")
    public String transactionList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("transactions", transactionService.listAllTransactionHistory(authentication.getName(), authentication.getName()));
        return "transaction/transaction-history";
    }

    @PostMapping("/otherWithdraw")
    public String otherWithDrawSummary(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO, BindingResult bindingResult, Model model) {
        String url = "transaction/otherWithdraw";
        if (bindingResult.hasErrors()) {
            return url;
        }
        return withDrawProcess(transactionDTO, model, url);
    }

    @PostMapping("/withdraw")
        public String withDrawSummary(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO, BindingResult bindingResult, Model model) {
        String url = "transaction/withdraw";
        if (bindingResult.hasErrors()) {
            return url;
        }
        return withDrawProcess(transactionDTO, model, url);
    }

    private String withDrawProcess(TransactionDTO transactionDTO, Model model, String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        transactionDTO.setSrcAcc(authentication.getName());
        TransactionDTO transaction;
        try {
            transaction = transactionService.withDraw(transactionDTO);
        } catch (Exception e) {
            model.addAttribute("err", e.getMessage());
            return url;
        }
        model.addAttribute("transaction", transaction);
        return "summary/summary";
    }

    @GetMapping("/transfer")
    public String transfer(Model model) {
        TransactionDTO transactionDTO = new TransactionDTO();
        model.addAttribute("transaction", transactionDTO);
        return TRANSACTION_TRANSFER;
    }

    @PostMapping("/transfer")
    public String transferSummary(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO, BindingResult bindingResult, Model model, Authentication authentication) {
        transactionDTO.setSrcAcc(authentication.getName());
        if (bindingResult.hasErrors()) {
            return TRANSACTION_TRANSFER;
        }
        try {
            transactionService.transfer(transactionDTO);
        } catch (Exception e) {
            model.addAttribute("err", e.getMessage());
            return TRANSACTION_TRANSFER;
        }
        return "summary/summary";
    }

}
