package com.mitrais.rms.service.impl;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.entity.Transaction;
import com.mitrais.rms.exception.AccountNotFoundException;
import com.mitrais.rms.exception.InsufficientBalanceException;
import com.mitrais.rms.exception.InvalidAmountException;
import com.mitrais.rms.repository.AccountRepository;
import com.mitrais.rms.repository.TransactionRepository;
import com.mitrais.rms.service.TransactionService;
import com.mitrais.rms.utils.TransactionType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionServiceImpl implements TransactionService {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public TransactionDTO withDraw(TransactionDTO transactionDTO) throws Exception {
        Account account = accountRepository.findAccountByAccNo(transactionDTO.getSrcAcc());
        BigDecimal newBalance = account.getBalance().subtract(transactionDTO.getAmount());
        // validate
        validateTransactionInput(transactionDTO.getAmount(), newBalance);
        //save transaction
        Transaction transaction = saveTransaction(transactionDTO, TransactionType.WITHDRAW);
        //update balance account
        updateAccount(transactionDTO, TransactionType.WITHDRAW);
        //build dto to display at summary screen
        transactionDTO.setCreatedDate(transaction.getCreatedDate().format(formatter));
        transactionDTO.setBalance(newBalance);
        return transactionDTO;
    }

    @Override
    @Transactional
    public TransactionDTO transfer(TransactionDTO transactionDTO) throws Exception {
        Account account = accountRepository.findAccountByAccNo(transactionDTO.getSrcAcc());
        BigDecimal newBalance = account.getBalance().subtract(transactionDTO.getAmount());
        validateTransactionInput(transactionDTO.getAmount(), newBalance);
        if (accountRepository.findAccountByAccNo(transactionDTO.getDestAcc()) == null) {
            throw new AccountNotFoundException("Account number not found");
        }
        if (transactionDTO.getSrcAcc().equals(transactionDTO.getDestAcc().trim())) {
            throw new AccountNotFoundException("Valid account number");
        }
        Transaction transaction = saveTransaction(transactionDTO, TransactionType.TRANSFER);
        updateAccount(transactionDTO, TransactionType.TRANSFER);
        transactionDTO.setCreatedDate(transaction.getCreatedDate().format(formatter));
        transactionDTO.setBalance(newBalance);
        return transactionDTO;
    }

    private void validateTransactionInput(BigDecimal inputAmount, BigDecimal newBalance) throws Exception {
        if (inputAmount.remainder(new BigDecimal(10)).intValue() != 0) {
            throw new InvalidAmountException("Amount is not multiple of $10");
        }
        if (newBalance.intValue() < 0) {
            throw new InsufficientBalanceException("Insufficient balance $" + inputAmount);
        }
    }

    private Transaction saveTransaction(TransactionDTO transactionDTO, TransactionType type) {
        ModelMapper modelMapper = new ModelMapper();
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setType(type);
        return transactionRepository.save(transaction);
    }

    private void updateAccount(TransactionDTO transactionDTO, TransactionType type) {
        Account srcAccount = accountRepository.findAccountByAccNo(transactionDTO.getSrcAcc());
        srcAccount.setBalance(srcAccount.getBalance().subtract(transactionDTO.getAmount()));
        accountRepository.save(srcAccount);
        if (type == TransactionType.TRANSFER) {
            Account destAccount = accountRepository.findAccountByAccNo(transactionDTO.getDestAcc());
            destAccount.setBalance(destAccount.getBalance().add(transactionDTO.getAmount()));
            accountRepository.save(destAccount);
        }
    }
}
