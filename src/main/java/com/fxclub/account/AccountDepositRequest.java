package com.fxclub.account;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AccountDepositRequest {

    @NotNull
    @PositiveOrZero
    private Number id;

    @NotNull
    @PositiveOrZero
    private Number amount;

}
