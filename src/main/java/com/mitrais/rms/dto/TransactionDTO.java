package com.mitrais.rms.dto;

import com.mitrais.rms.utils.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private TransactionType type;
    @DecimalMin(value = "1.00", inclusive = false, message = "{amount.min}")
    @DecimalMax(value = "1000.00", inclusive = false, message = "{amount.max}")
    private BigDecimal amount;
    private String srcAcc;
//    @NotEmpty(message = "{account.empty}")
    @Length(min = 6, max = 6, message = "{account.length}")
    @Pattern(regexp = "^[0-9]*$", message = "{account.format}")
    private String destAcc;
    private BigDecimal balance;
    private String createdDate;
//    @NotEmpty(message = "{refNo.empty}")
    @Length(min = 6, max = 6, message = "{refNo.length}")
    @Pattern(regexp = "^[0-9]*$", message = "{refNo.format}")
    private String refNo;
}
