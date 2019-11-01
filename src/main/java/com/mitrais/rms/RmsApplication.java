package com.mitrais.rms;

import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Role;
import com.mitrais.rms.service.AccountService;
import com.mitrais.rms.service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class RmsApplication {
    private final AccountService accountService;
    private final RoleService roleService;

    public RmsApplication(AccountService accountService, RoleService roleService) {
        this.accountService = accountService;
        this.roleService = roleService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RmsApplication.class, args);
    }

    @Bean
    InitializingBean saveData() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(roleAdmin);
        roles.add(roleUser);
        AccountDTO accountDTO = new AccountDTO("Eric", "123456", "123456", BigDecimal.valueOf(1000), roles);
        return () -> {
            accountService.save(accountDTO);
        };
    }

}
