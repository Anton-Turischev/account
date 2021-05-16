package com.fxclub.account;

import com.fxclub.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
public class Controller {
    private final AccountService accountService;

    public Controller(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("create")
    public CreateAccountResponse createAccount() {
        return accountService.createAccount();
    }

    @PostMapping("info")
    public AccountInfoResponse getAccountInfo(@RequestBody @Valid AccountInfoRequest request) {
        return accountService.getAccountInfo(request);
    }

    @PostMapping("deposit")
    public void deposit(@RequestBody @Valid AccountDepositRequest request) {
        accountService.deposit(request);
    }

    @PostMapping("withdraw")
    public void withdraw(@RequestBody @Valid AccountDepositRequest request) {
            accountService.withdraw(request);
    }

}
