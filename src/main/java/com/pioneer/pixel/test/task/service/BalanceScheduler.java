package com.pioneer.pixel.test.task.service;

import com.pioneer.pixel.test.task.entity.Account;
import com.pioneer.pixel.test.task.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BalanceScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BalanceScheduler.class);

    private final AccountRepository accountRepository;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void updateBalances() {
        logger.info("Scheduled balance update started");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal current = account.getBalance();
            BigDecimal initial = account.getInitialBalance();
            BigDecimal max = initial.multiply(new BigDecimal("2.07"));
            BigDecimal updated = current.multiply(new BigDecimal("1.10"));
            if (updated.compareTo(max) > 0) {
                updated = max;
            }
            account.setBalance(updated);
            logger.debug("Updated balance for account {}: {}", account.getId(), updated);
        }
        accountRepository.saveAll(accounts);
        logger.info("Scheduled balance update finished");
    }
}