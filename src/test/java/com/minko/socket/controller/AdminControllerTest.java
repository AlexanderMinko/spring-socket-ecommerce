package com.minko.socket.controller;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.entity.RoleType;
import com.minko.socket.security.AccountDetailsServiceImpl;
import com.minko.socket.security.jwt.JwtEntryPoint;
import com.minko.socket.security.jwt.JwtProvider;
import com.minko.socket.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountDetailsServiceImpl accountDetailsService;

    @MockBean
    private JwtEntryPoint jwtEntryPoint;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="alexander_ming@i.ua",roles={"USER", "ADMIN"})
    @DisplayName("Should List All Accounts When making GET request to endpoint - /api/admin/accounts")
    void getAllAccountsTest() throws Exception {
        final String METHOD_ENDPOINT = "/api/admin/accounts";
        List<RoleType> roles = Collections.singletonList(RoleType.ROLE_USER);
        AccountAdminResponse accountAdminResponseExp1 = new AccountAdminResponse(1L, "firstName1", "lastName1",
                "sobaka@email.ua", Date.from(Instant.now()), "url1", true, roles);
        AccountAdminResponse accountAdminResponseExp2 = new AccountAdminResponse(2L, "firstName2", "lastName2",
                "sobaka2@email.ua", Date.from(Instant.now()), "url2", true, roles);
        Mockito.when(accountService.getAccounts()).thenReturn(Arrays.asList(accountAdminResponseExp1, accountAdminResponseExp2));

        mockMvc.perform(get(METHOD_ENDPOINT))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].email", Matchers.is("sobaka@email.ua")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].email", Matchers.is("sobaka2@email.ua")));
    }

    @Test
    @WithMockUser(username="alexander_ming@i.ua")
    @DisplayName("Should Response 403 status When making GET request to endpoint - /api/admin/accounts")
    void getAllAccountsTest2() throws Exception {
        final String METHOD_ENDPOINT = "/api/admin/accounts";
        mockMvc.perform(get(METHOD_ENDPOINT))
                .andExpect(status().is(403));
    }

}