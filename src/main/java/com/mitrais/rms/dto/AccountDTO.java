package com.mitrais.rms.dto;

import com.mitrais.rms.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String accName;
    @NotEmpty
    @Length(min = 6, max = 6, message = "Account Number should have 6 digits length")
    @Pattern(regexp = "^[0-9]*$", message = "Account Number should only contains numbers")
    private String accNo;
    @NotEmpty
    @Length(min = 6, max = 6, message = "PIN should have 6 digits length")
    @Pattern(regexp = "^[0-9]*$", message = "PIN should only contains numbers")
    private String accPin;
    private BigDecimal balance;
    private List<Role> roles;
}
