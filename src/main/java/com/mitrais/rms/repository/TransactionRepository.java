package com.mitrais.rms.repository;

import com.mitrais.rms.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> getAllBySrcAccOrDestAcc(String acc1, String acc2);
}
