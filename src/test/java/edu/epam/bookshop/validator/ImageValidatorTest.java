package edu.epam.bookshop.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImageValidatorTest {

    private ImageValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new ImageValidator();
    }

    @Test
    void isImageValidTrue() {
        //given
        String givenImage = "123.jpg";
        //when
        boolean condition = underTest.isImageValid(givenImage);
        //then
        assertThat(condition).isTrue();

    }

    @Test
    void isImageValidFalse() {
        //given
        String givenImage = "123.extension";
        //when
        boolean condition = underTest.isImageValid(givenImage);
        //then
        assertThat(condition).isFalse();
    }

    @AfterEach
    void tearDown() {
        underTest = null;
    }
}