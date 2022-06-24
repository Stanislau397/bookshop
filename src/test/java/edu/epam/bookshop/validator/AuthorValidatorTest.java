package edu.epam.bookshop.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorValidatorTest {

    private AuthorValidator authorValidator;

    @BeforeEach
    void setUp() {
        authorValidator = new AuthorValidator();
    }

    @Test
    void isFirstnameValidTrue() {
        //given
        String firstname = "Stanislau";
        //when
        boolean firstnameValid = authorValidator.isFirstnameValid(firstname);
        //then
        assertThat(firstnameValid).isTrue();
    }

    @Test
    void isFirstnameValidFalse() {
        //given
        String firstname = "1Stanislau";
        //when
        boolean firstnameValid = authorValidator.isFirstnameValid(firstname);
        //then
        assertThat(firstnameValid).isFalse();
    }

    @Test
    void isLastnameValidTrue() {
        //given
        String lastname = "Kachan";
        //when
        boolean lastnameValid = authorValidator.isLastnameValid(lastname);
        //then
        assertThat(lastnameValid).isTrue();
    }

    @Test
    void isLastnameValidFalse() {
        //given
        String lastname = "1Kachan";
        //when
        boolean lastnameValid = authorValidator.isLastnameValid(lastname);
        //then
        assertThat(lastnameValid).isFalse();
    }

    @AfterEach
    void tearDown() {
        authorValidator = null;
    }
}