package com.mitrais.rms.service;

import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> listAccount();

    Account findByAccNo(String accNo);

    Account save(AccountDTO accountDTO);
}
