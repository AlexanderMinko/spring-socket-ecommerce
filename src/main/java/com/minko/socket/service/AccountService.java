package com.minko.socket.service;

import com.minko.socket.dto.AccountAdminResponse;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.RoleType;

import java.util.List;

public interface AccountService {

    boolean existsByEmail(String email);

    Account getByEmail(String email);

    Account getById(Long id);

    List<AccountAdminResponse> getAccounts();

    List<RoleType> getListRolesByAccountEmail(String email);
}
