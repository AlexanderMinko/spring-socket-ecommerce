package com.minko.socket.repository;

import com.minko.socket.entity.Role;
import com.minko.socket.entity.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private Role savedRole;

    @BeforeEach
    void setUp() {
        role = new Role(null, RoleType.ROLE_USER);
        savedRole = roleRepository.save(role);
    }

    @Test
    void findByRoleType() {
        Optional<Role> roleAct = roleRepository.findByRoleType(RoleType.ROLE_USER);
        assertThat(roleAct).isEqualTo(Optional.of(savedRole));
    }

    @Test
    void existsByRoleType() {
        boolean isExist = roleRepository.existsByRoleType(RoleType.ROLE_USER);
        assertThat(isExist).isTrue();
    }
}