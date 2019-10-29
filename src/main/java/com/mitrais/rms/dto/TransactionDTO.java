package com.mitrais.rms.dto;

import com.mitrais.rms.utils.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private TransactionType type;
    @DecimalMin(value = "1.00", inclusive = false, message = "{amount.min}")
    @DecimalMax(value = "1000.00", inclusive = false, message = "{amount.max}")
    @Digits(integer = 3, fraction = 2, message = "Invalid amount")
    private BigDecimal amount;
    private String srcAcc;
    private String destAcc;
    private BigDecimal balance;
}
