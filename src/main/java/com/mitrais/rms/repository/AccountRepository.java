package com.mitrais.rms.repository;

import com.mitrais.rms.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsAccountByAccNo(String accNo);

    boolean existsAccountByAccPin(String accPin);

    Optional<Account> findAccountByAccNo(String accNo);
}
