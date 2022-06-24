package edu.epam.bookshop.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserValidatorTest {


    private UserValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserValidator();
    }

    @Test
    void isUsernameValidTrue() {
        //given
        String userName = "Stanislau";
        //when
        boolean condition = underTest.isUsernameValid(userName);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isUsernameFalse() {
        //given
        String userName = "St";
        //when
        boolean condition = underTest.isUsernameValid(userName);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void isEmailValidTrue() {
        //given
        String email = "Stanislau@gmail.com";
        //when
        boolean condition = underTest.isEmailValid(email);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isEmailValidFalse() {
        //given
        String email = "Stanislau@gmail.";
        //when
        boolean condition = underTest.isEmailValid(email);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void isPasswordValidTrue() {
        //given
        String password = "Stanislau123";
        //when
        boolean condition = underTest.isPasswordValid(password);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void isPasswordValidFalse() {
        //given
        String password = "stanislau123";
        //when
        boolean condition = underTest.isPasswordValid(password);
        //then
        assertThat(condition).isFalse();
    }

    @AfterEach
    void tearDown() {
        underTest = null;
    }
}