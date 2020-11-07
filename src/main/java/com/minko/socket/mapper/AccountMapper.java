package com.minko.socket.mapper;

import com.minko.socket.dto.RegistrationRequestDto;
import com.minko.socket.dto.RegistrationResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import com.minko.socket.repository.RoleRepository;
import com.minko.socket.service.impl.SocketException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "password", expression = "java(getPasswordEncoder().encode(registrationRequestDto.getPassword()))")
    @Mapping(target = "enabled", expression = "java(false)")
    @Mapping(target = "roles", expression = "java(getRoles())")
    public abstract Account mapDtoToAccount(RegistrationRequestDto registrationRequestDto);


    public abstract RegistrationResponseDto mapFromAccountToDto(Account account);

    PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    List<Role> getRoles() {
        Role roleUser = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new SocketException("Role not found with role type - " + RoleType.ROLE_USER));
        return Arrays.asList(roleUser);
    }
}
