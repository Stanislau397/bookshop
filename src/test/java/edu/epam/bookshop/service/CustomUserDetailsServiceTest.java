package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_NAME_NOT_FOUND_MSG;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsernameWillReturnUser() {
        //given
        String givenUsername = "Stanislau";
        User user = User.builder()
                .userName(givenUsername)
                .build();
        //when
        when(userRepository.findByUserName(givenUsername)).thenReturn(Optional.of(user));
        //
        UserDetails expected = customUserDetailsService.loadUserByUsername(givenUsername);
        assertThat(expected).isNotNull();
        assertThat(expected.getUsername()).isEqualTo(givenUsername);
    }

    @Test
    void loadUserByUsernameWillThrowUsernameNotFoundException() {
        //given
        String givenUsername = "Stanislau";
        //when
        when(userRepository.findByUserName(givenUsername)).thenReturn(Optional.empty());
        //
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(givenUsername))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, givenUsername));
    }
}