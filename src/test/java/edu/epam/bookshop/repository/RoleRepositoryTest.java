package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Role;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByRoleName() {
        //given
        Role role = Role.builder()
                .roleName("USER")
                .roleId(1L)
                .build();
        roleRepository.save(role);

        //when
        Optional<Role> expectedRole = Optional.of(role);
        Optional<Role> result = roleRepository.findByRoleName("USER");

        //then
        assertThat(result).isEqualTo(expectedRole);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }
}