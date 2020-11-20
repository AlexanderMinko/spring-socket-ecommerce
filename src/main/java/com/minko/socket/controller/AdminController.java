package com.minko.socket.controller;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminController {

    private final AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountAdminResponse>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAccounts(), HttpStatus.OK);
    }

}
