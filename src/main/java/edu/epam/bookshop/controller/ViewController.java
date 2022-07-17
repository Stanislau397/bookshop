package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_BOOKS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_GENRES_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_PUBLISHERS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.AUTHOR_DETAILS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.BOOKS_BY_GENRE_TITLE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.BOOKS_BY_YEAR_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.BOOK_DETAILS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.EDIT_BOOK_URN;
import static edu.epam.bookshop.controller.constant.HtmlPage.ALL_BOOKS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ALL_GENRES_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ALL_PUBLISHERS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.AUTHOR_DETAILS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.BOOKS_BY_GENRE_TITLE_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.BOOKS_BY_YEAR_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.BOOK_DETAILS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.EDIT_BOOK_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.SIGN_UP_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.SIGN_IN_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.HOME_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ADMIN_CABINET_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ALL_USERS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ACCOUNT_SETTING_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.ALL_AUTHORS_HTML;
import static edu.epam.bookshop.controller.constant.HtmlPage.EDIT_AUTHOR_HTML;

import static edu.epam.bookshop.controller.constant.AttributeName.USER_ATTRIBUTE;
import static edu.epam.bookshop.controller.constant.GetMappingURN.MAIN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.REGISTRATION_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.LOGIN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ADMIN_CABINET_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_USERS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ACCOUNT_SETTINGS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.ALL_AUTHORS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.EDIT_AUTHOR_URN;

@Controller
@AllArgsConstructor
public class ViewController {

    @GetMapping(MAIN_URN)
    public ModelAndView welcome(ModelAndView homeView) {
        homeView.setViewName(HOME_HTML);
        return homeView;
    }

    @GetMapping(REGISTRATION_URN)
    public ModelAndView registrationPage(ModelAndView registrationView) {
        User user = User.builder().build();
        registrationView.addObject(USER_ATTRIBUTE, user);
        registrationView.setViewName(SIGN_UP_HTML);
        return registrationView;
    }

    @GetMapping(LOGIN_URN)
    public ModelAndView loginPage(ModelAndView loginView) {
        loginView.setViewName(SIGN_IN_HTML);
        return loginView;
    }

    @GetMapping(ADMIN_CABINET_URN)
    public ModelAndView cabinetPage(ModelAndView adminCabinetView) {
        adminCabinetView.setViewName(ADMIN_CABINET_HTML);
        return adminCabinetView;
    }

    @GetMapping(ALL_USERS_URN)
    public ModelAndView allUsersPage(ModelAndView allUsersView) {
        allUsersView.setViewName(ALL_USERS_HTML);
        return allUsersView;
    }

    @GetMapping(ACCOUNT_SETTINGS_URN)
    public ModelAndView accountSettingsPage(ModelAndView accountSettingsView) {
        accountSettingsView.setViewName(ACCOUNT_SETTING_HTML);
        return accountSettingsView;
    }

    @GetMapping(ALL_AUTHORS_URN)
    public ModelAndView allAuthorsPage(ModelAndView allAuthorsView) {
        allAuthorsView.setViewName(ALL_AUTHORS_HTML);
        return allAuthorsView;
    }

    @GetMapping(EDIT_AUTHOR_URN)
    public ModelAndView editAuthorPage(ModelAndView editAuthorView) {
        editAuthorView.setViewName(EDIT_AUTHOR_HTML);
        return editAuthorView;
    }

    @GetMapping(EDIT_BOOK_URN)
    public ModelAndView editBookPage(ModelAndView editBookView) {
        editBookView.setViewName(EDIT_BOOK_HTML);
        return editBookView;
    }

    @GetMapping(ALL_GENRES_URN)
    public ModelAndView allGenresPage(ModelAndView allGenresView) {
        allGenresView.setViewName(ALL_GENRES_HTML);
        return allGenresView;
    }

    @GetMapping(ALL_PUBLISHERS_URN)
    public ModelAndView allPublishersPage(ModelAndView allPublishersView) {
        allPublishersView.setViewName(ALL_PUBLISHERS_HTML);
        return allPublishersView;
    }

    @GetMapping(ALL_BOOKS_URN)
    public ModelAndView allBooksPage(ModelAndView allBooksView) {
        allBooksView.setViewName(ALL_BOOKS_HTML);
        return allBooksView;
    }

    @GetMapping(AUTHOR_DETAILS_URN)
    public ModelAndView authorDetailsPage(ModelAndView authorDetailsView) {
        authorDetailsView.setViewName(AUTHOR_DETAILS_HTML);
        return authorDetailsView;
    }

    @GetMapping(BOOK_DETAILS_URN)
    public ModelAndView bookDetailsPage(ModelAndView bookDetailsView) {
        bookDetailsView.setViewName(BOOK_DETAILS_HTML);
        return bookDetailsView;
    }

    @GetMapping(BOOKS_BY_GENRE_TITLE_URN)
    public ModelAndView booksByGenreTitlePage(ModelAndView booksByGenreTitleView) {
        booksByGenreTitleView.setViewName(BOOKS_BY_GENRE_TITLE_HTML);
        return booksByGenreTitleView;
    }

    @GetMapping(BOOKS_BY_YEAR_URN)
    public ModelAndView booksByYearPage(ModelAndView booksByYearView) {
        booksByYearView.setViewName(BOOKS_BY_YEAR_HTML);
        return booksByYearView;
    }
}
