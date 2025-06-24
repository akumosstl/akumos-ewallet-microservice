package io.github.akumosstl.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.account.dto.AccountRequestDto;
import io.github.akumosstl.account.dto.ApiResponseDto;
import io.github.akumosstl.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class)
@ContextConfiguration(classes = { AccountController.class, SecurityBypassConfig.class })
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.mock.mockito.MockBean
    private AccountService accountService;

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        AccountRequestDto request = new AccountRequestDto();
        request.setAccountId(2L);
        request.setEmail("john@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");
        request.setSurname("Smith");

        ApiResponseDto mockResponse = new ApiResponseDto();
        mockResponse.setResponseCode(201);
        mockResponse.setResponseMessage("Account created");

        Mockito.when(accountService.createAccount(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/v1/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(201))
                .andExpect(jsonPath("$.responseMessage").value("Account created"));
    }

    @Test
    void shouldUpdateAccountSuccessfully() throws Exception {
        AccountRequestDto request = new AccountRequestDto();
        request.setAccountId(1L);
        request.setEmail("john.updated@example.com");
        request.setFirstName("Johnny");
        request.setLastName("Doe");
        request.setPhone("0987654321");
        request.setSurname("Smith");

        ApiResponseDto mockResponse = new ApiResponseDto();
        mockResponse.setResponseCode(200);
        mockResponse.setResponseMessage("Account updated");

        Mockito.when(accountService.updateAccount(any())).thenReturn(mockResponse);

        mockMvc.perform(put("/v1/account/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("Account updated"));
    }
}
