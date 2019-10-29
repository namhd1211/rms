package com.mitrais.rms.repository;

import com.mitrais.rms.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByAccNo(String accNo);
}
