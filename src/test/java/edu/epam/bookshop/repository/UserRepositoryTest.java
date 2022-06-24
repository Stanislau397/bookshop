package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
    }


    @Test
    void itShouldCheckIfUserExistsByEmail() {
        //given
        Role role = Role.builder()
                .roleName("ADMIN")
                .build();
        User user = User.builder()
                .userName("Stanislau")
                .email("lancer397@gmail.com")
                .password("12345")
                .avatarName("123.jpg")
                .locked(true)
                .roles(new HashSet<>(List.of(role)))
                .build();

        underTest.save(user);

        //when
        String expectedEmail = "lancer397@gmail.com";
        boolean exists = underTest.existsByEmail(expectedEmail);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfUserNotExistsByEmail() {
        //given
        String expectedEmail = "lancer397@gmail.com";

        //when
        boolean exists = underTest.existsByEmail(expectedEmail);

        //then
        assertThat(exists).isFalse();
    }

    @Test
    void findByUserName() {
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
}