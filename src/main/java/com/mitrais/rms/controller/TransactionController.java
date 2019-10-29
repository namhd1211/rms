package com.mitrais.rms.controller;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class TransactionController {
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

    @PostMapping("/withdraw")
    public String withDrawSummary(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO,BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "transaction/withdraw";
        }
        transactionDTO.setSrcAcc(authentication.getName());
        TransactionDTO transaction = new TransactionDTO();
        try {
            transaction = transactionService.withDraw(transactionDTO);
        } catch (Exception e) {
            model.addAttribute("err", e.getMessage());
        }
        model.addAttribute("transaction", transaction);
        return "summary/summary";
    }

    @GetMapping("/transfer")
    public String transfer() {
        return "transaction/transfer";
    }

    @PostMapping("/transfer")
    public String transferSummary() {
        return "summary/transfer";
    }

}
