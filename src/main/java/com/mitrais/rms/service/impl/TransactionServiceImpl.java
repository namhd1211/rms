package com.mitrais.rms.service.impl;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.entity.Transaction;
import com.mitrais.rms.exception.InsufficientBalanceException;
import com.mitrais.rms.exception.InvalidAmountException;
import com.mitrais.rms.repository.AccountRepository;
import com.mitrais.rms.repository.TransactionRepository;
import com.mitrais.rms.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionDTO withDraw(TransactionDTO transactionDTO) throws Exception{
        if (transactionDTO.getAmount().remainder(new BigDecimal(10)).intValue() != 0) {
            throw new InvalidAmountException("Invalid amount");
        }
        Account account = accountRepository.findAccountByAccNo(transactionDTO.getSrcAcc());
        BigDecimal newBalance = account.getBalance().subtract(transactionDTO.getAmount());
        if (newBalance.intValue() < 0) {
            throw new InsufficientBalanceException("Insufficient balance $" + transactionDTO.getAmount());
        }
        ModelMapper modelMapper = new ModelMapper();
        transactionDTO.setBalance(newBalance);
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setCreatedDate(LocalDate.now());
        transactionRepository.save(transaction);
        account.setBalance(newBalance);
        accountRepository.save(account);
        return transactionDTO;
    }
}
