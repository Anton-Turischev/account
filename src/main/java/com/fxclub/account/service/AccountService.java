package com.fxclub.account.service;

import com.fxclub.account.AccountDepositRequest;
import com.fxclub.account.AccountInfoRequest;
import com.fxclub.account.AccountInfoResponse;
import com.fxclub.account.CreateAccountResponse;
import com.fxclub.account.model.Account;
import com.fxclub.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public CreateAccountResponse createAccount() {
        Account account = Account.builder().balance(BigDecimal.ZERO).build();
        accountRepository.save(account);
        return new CreateAccountResponse(account.getId());
    }

    public AccountInfoResponse getAccountInfo(AccountInfoRequest request) {
        Account account = accountRepository.findById(request.getId().longValue())
                .orElseThrow(RuntimeException::new);
        return new AccountInfoResponse(account.getId(), account.getBalance());
    }

    @Transactional
    public void deposit(AccountDepositRequest request) {
        Account account = accountRepository.findById(request.getId().longValue())
                .orElseThrow(RuntimeException::new);
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(request.getAmount().doubleValue())));
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(AccountDepositRequest request) {
        Account account = accountRepository.findById(request.getId().longValue())
                .orElseThrow(IllegalArgumentException::new);
        BigDecimal amount = BigDecimal.valueOf(request.getAmount().doubleValue());
        if(!(account.getBalance().compareTo(amount)<0)) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
        } else {
            throw new RuntimeException("Not enough money");
        }
    }
}
