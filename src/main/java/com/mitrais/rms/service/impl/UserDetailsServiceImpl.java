package com.mitrais.rms.service.impl;

import com.mitrais.rms.entity.Account;
import com.mitrais.rms.entity.Role;
import com.mitrais.rms.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service("userDetail")
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String accNo) {
        Account account = accountRepository.findAccountByAccNo(accNo);
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(account.getAccNo(), account.getAccPin(), authorities);
    }
}
