package com.mitrais.rms.service.impl;

import com.mitrais.rms.constant.DataTest;
import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.entity.Role;
import com.mitrais.rms.entity.Transaction;
import com.mitrais.rms.exception.AccountNotFoundException;
import com.mitrais.rms.exception.InsufficientBalanceException;
import com.mitrais.rms.exception.InvalidAmountException;
import com.mitrais.rms.repository.AccountRepository;
import com.mitrais.rms.repository.TransactionRepository;
import com.mitrais.rms.utils.TransactionType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    private final TransactionDTO transactionDTO1 = new TransactionDTO(BigDecimal.valueOf(2));
    private final TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.valueOf(100));
    private final Transaction transaction = new Transaction(1l, TransactionType.WITHDRAW, BigDecimal.valueOf(100), LocalDateTime.now(), "123456", "121192", "123456");
    private final Role role = new Role(DataTest.ROLE_ADMIN);
    private final List<Role> roles = Collections.singletonList(role);
    private final Account account = new Account(DataTest.ID, DataTest.ACC_NO, DataTest.ACC_NAME, DataTest.ACC_PIN, BigDecimal.valueOf(90), roles);
    private final Account account1 = new Account(DataTest.ID, DataTest.ACC_NO, DataTest.ACC_NAME, DataTest.ACC_PIN, BigDecimal.valueOf(900), roles);

    @Test(expected = InvalidAmountException.class)
    public void withDrawWithException() throws Exception {
        Mockito.when(accountRepository.findAccountByAccNo(any())).thenReturn(account);
        transactionService.withDraw(transactionDTO1);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void withDrawWithInsufficientBalanceException() throws Exception {
        Mockito.when(accountRepository.findAccountByAccNo(any())).thenReturn(account);
        transactionService.withDraw(transactionDTO);
    }

    @Test
    public void withDraw() throws Exception {
        Mockito.when(accountRepository.findAccountByAccNo(any())).thenReturn(account1);
        Mockito.when(transactionRepository.save(any())).thenReturn(transaction);
        TransactionDTO transactionDTO = transactionService.withDraw(this.transactionDTO);
        Assert.assertEquals(BigDecimal.valueOf(800), transactionDTO.getBalance());
    }
}
