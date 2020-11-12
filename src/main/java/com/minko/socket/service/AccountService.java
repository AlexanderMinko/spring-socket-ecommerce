package com.minko.socket.service;

import com.minko.socket.entity.Account;

public interface AccountService {

    boolean existsByEmail(String email);

    Account getByEmail(String email);

    Account getById(Long id);
}
