package com.mitrais.rms.service.impl;

import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.exception.UserNotFoundException;
import com.mitrais.rms.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> listAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByAccNo(String accNo) {
        return accountRepository.findAccountByAccNo(accNo).orElseThrow(() -> new UserNotFoundException("not found"));
    }

    @Override
    public Account save(AccountDTO accountDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDTO, Account.class);
        return accountRepository.save(account);
    }
}
