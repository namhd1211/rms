package com.mitrais.rms.controller;

import com.mitrais.rms.dto.TransactionDTO;
import com.mitrais.rms.service.TransactionService;
import com.mitrais.rms.utils.TransactionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TransactionControllerTest {
    private MockMvc mvc;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    private final TransactionDTO transactionDTO = new TransactionDTO(TransactionType.WITHDRAW, BigDecimal.valueOf(100), "123456", "121192", BigDecimal.valueOf(1000), "12-11-2019", "123456");
    private final TransactionDTO transactionDTO1 = new TransactionDTO(BigDecimal.valueOf(100));

    @Test
    @WithMockUser(username = "123456", roles = {"USER", "ADMIN"})
    public void withDrawSummary() throws Exception {
        when(transactionService.withDraw(transactionDTO)).thenReturn(transactionDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/withdraw").flashAttr("transaction", transactionDTO1);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.forwardedUrl("summary/summary"));
    }

    @Test
    @WithMockUser(username = "123456", roles = {"USER", "ADMIN"})
    public void withDrawSummaryException() throws Exception {
        when(transactionService.withDraw(any())).thenThrow(new Exception("Invalid"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/withdraw").flashAttr("transaction", transactionDTO1);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.forwardedUrl("transaction/withdraw"));
    }
}
