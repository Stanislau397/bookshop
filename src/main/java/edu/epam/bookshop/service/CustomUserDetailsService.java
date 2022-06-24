package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.config.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_NAME_NOT_FOUND_MSG;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, username)
                ));
        return new CustomUserDetails(user);
    }
}
