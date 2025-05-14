package com.pioneer.pixel.test.task.service.impl;

import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.repository.AccountRepository;
import com.pioneer.pixel.test.task.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        logger.info("Initiating transfer of {} from user {} to user {}", amount, fromUserId, toUserId);
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        Account fromAccount = accountRepository.findByUserIdForUpdate(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account toAccount = accountRepository.findByUserIdForUpdate(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));
        BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
        if (newFromBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        fromAccount.setBalance(newFromBalance);
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        logger.info("Transfer complete. New balances - fromUser {}: {}, toUser {}: {}", fromUserId, newFromBalance,
                toUserId, toAccount.getBalance());
    }
}