package com.mitrais.rms.service;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDTO withDraw(TransactionDTO transactionDTO) throws Exception;

    TransactionDTO transfer(TransactionDTO transactionDTO) throws Exception;

    List<Transaction> listAllTransactionHistory(String srcAcc, String destAcc);
}
