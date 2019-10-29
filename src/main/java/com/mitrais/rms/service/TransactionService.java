package com.mitrais.rms.service;

import com.mitrais.rms.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO withDraw(TransactionDTO transactionDTO) throws Exception;
}
