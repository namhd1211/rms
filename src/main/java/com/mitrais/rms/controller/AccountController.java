package com.mitrais.rms.controller;

import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.repository.RoleRepository;
import com.mitrais.rms.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AccountController {
    private static final String ACCOUNT_SAVE = "account/save";
    private final AccountService accountService;
    private final RoleRepository roleRepository;

    public AccountController(AccountService accountService, RoleRepository roleRepository) {
        this.accountService = accountService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("logout", "You have been logged out");
        return "redirect:/login";
    }

    @GetMapping("/accounts")
    public String listUser(Model model) {
        model.addAttribute("accounts", accountService.listAccount());
        return "account/account-list";
    }

    @PostMapping("/admin/save")
    public String create(@Valid @ModelAttribute("account") AccountDTO accountDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return ACCOUNT_SAVE;
        }
        if (accountService.findByAccNo(accountDTO.getAccNo()) != null) {
            model.addAttribute("existed", "Account Number is existed");
            return ACCOUNT_SAVE;
        }
        accountService.save(accountDTO);
        return "redirect:/accounts";
    }

    @GetMapping("/admin/save")
    public String create(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("roles", roleRepository.findAll());
        return ACCOUNT_SAVE;
    }

    @GetMapping("/403")
    public String error() {
        return "error/403";
    }

}