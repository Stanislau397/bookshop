package edu.epam.bookshop.config.security;

import edu.epam.bookshop.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static edu.epam.bookshop.controller.constant.GetMappingURN.MAIN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.REGISTRATION_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.LOGIN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ADMIN_CABINET_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_USERS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_USERS_WITH_PAGINATION_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ACCOUNT_SETTINGS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_AUTHORS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHOR_INFO_BY_ID_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_KEYWORD_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_AUTHORS_BY_PAGE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.EDIT_AUTHOR_URN;

import static edu.epam.bookshop.controller.constant.PostMappingURN.REGISTER_USER_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_STATUS_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_AVATAR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_PASSWORD_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_ROLE_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_AUTHOR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_AUTHOR_INFO_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.DELETE_AUTHOR_URN;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests()
                .and()
                .formLogin()
                .loginPage(LOGIN_URN)
                .defaultSuccessUrl(MAIN_URN)
                .and()
                .logout()
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(MAIN_URN)
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(REGISTRATION_URN)
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(REGISTER_USER_URN)
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(ADMIN_CABINET_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(UPDATE_USER_STATUS_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(ALL_USERS_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(FIND_USERS_WITH_PAGINATION_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(ACCOUNT_SETTINGS_URN)
                .hasAnyAuthority(ROLE_ADMIN, ROLE_USER);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(UPDATE_USER_PASSWORD_URN)
                .hasAnyAuthority(ROLE_USER, ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(UPDATE_USER_AVATAR_URN)
                .hasAnyAuthority(ROLE_ADMIN, ROLE_USER);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(UPDATE_USER_ROLE_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(ALL_AUTHORS_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(ADD_AUTHOR_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(DELETE_AUTHOR_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(UPDATE_AUTHOR_INFO_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(FIND_AUTHOR_INFO_BY_ID_URN)
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(FIND_AUTHORS_BY_KEYWORD_URN)
                .permitAll();

        httpSecurity.authorizeHttpRequests()
                .antMatchers(FIND_AUTHORS_BY_PAGE_URN)
                .hasAuthority(ROLE_ADMIN);

        httpSecurity.authorizeHttpRequests()
                .antMatchers(FIND_AUTHORS_BY_PAGE_URN)
                .hasAuthority(EDIT_AUTHOR_URN);
    }
}
