package com.pioneer.pixel.test.task.service;

import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.repository.AccountRepository;
import com.pioneer.pixel.test.task.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transfer_successful() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(50);

        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100));
        fromAccount.setInitialBalance(BigDecimal.valueOf(100));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(30));
        toAccount.setInitialBalance(BigDecimal.valueOf(30));

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(toAccount));

        transferService.transfer(fromUserId, toUserId, amount);

        assertEquals(BigDecimal.valueOf(50), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(80), toAccount.getBalance());

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void transfer_insufficientFunds() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(150);

        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100));
        fromAccount.setInitialBalance(BigDecimal.valueOf(100));
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50));
        toAccount.setInitialBalance(BigDecimal.valueOf(50));

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(toAccount));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(fromUserId, toUserId, amount));
        assertEquals("Insufficient funds", ex.getMessage());
    }

    @Test
    void transfer_negativeAmount() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(-10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(fromUserId, toUserId, amount));
        assertEquals("Transfer amount must be positive", ex.getMessage());
    }

    @Test
    void transfer_sameUser() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(userId, userId, amount));
        assertEquals("Cannot transfer to the same user", ex.getMessage());
    }
}