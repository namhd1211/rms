package com.mitrais.rms.service.impl;

import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.repository.AccountRepository;
import com.mitrais.rms.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Account> listAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByAccNo(String accNo) {
        return accountRepository.findAccountByAccNo(accNo);
    }

    @Override
    public Account save(AccountDTO accountDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setAccPin(passwordEncoder.encode(account.getAccPin()));
        return accountRepository.save(account);
    }
}
