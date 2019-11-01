package com.mitrais.rms.controller;

import com.mitrais.rms.constant.DataTest;
import com.mitrais.rms.dto.AccountDTO;
import com.mitrais.rms.entity.Account;
import com.mitrais.rms.entity.Role;
import com.mitrais.rms.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    private MockMvc mvc;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    private final Role role = new Role(DataTest.ROLE_ADMIN);
    private final List<Role> roles = Collections.singletonList(role);
    private final Account account = new Account(DataTest.ID, DataTest.ACC_NO, DataTest.ACC_NAME, DataTest.ACC_PIN, BigDecimal.valueOf(1000), roles);
    private final AccountDTO accountDTORequest = new AccountDTO(DataTest.ACC_NAME, DataTest.ACC_NO, DataTest.ACC_PIN, BigDecimal.valueOf(1000), roles);

    @Test
    public void createWithExistedAccNo() throws Exception {
        when(accountService.findByAccNo(DataTest.ACC_NO)).thenReturn(account);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/save").flashAttr("account", accountDTORequest);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.forwardedUrl(DataTest.ACCOUNT_SAVE_URL));
    }

    @Test
    public void createSuccessful() throws Exception {
        when(accountService.findByAccNo(DataTest.ACC_NO)).thenReturn(null);
        when(accountService.save(accountDTORequest)).thenReturn(account);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/save").flashAttr("account", accountDTORequest);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl(DataTest.REDIRECT_ACCOUNTS_URL));
    }
}
