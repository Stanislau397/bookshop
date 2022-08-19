package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static edu.epam.bookshop.constant.GetMappingURN.ALL_BOOKS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ALL_GENRES_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ALL_PUBLISHERS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.AUTHOR_DETAILS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.BOOKS_BY_GENRE_TITLE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.BOOKS_BY_KEYWORD_PAGE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.BOOKS_BY_YEAR_URN;
import static edu.epam.bookshop.constant.GetMappingURN.BOOKS_WITH_HIGH_SCORE_PAGE_URN;
import static edu.epam.bookshop.constant.GetMappingURN.BOOK_DETAILS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.EDIT_BOOK_URN;
import static edu.epam.bookshop.constant.GetMappingURN.USER_PROFILE_PAGE_URN;
import static edu.epam.bookshop.constant.HtmlPage.ALL_BOOKS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ALL_GENRES_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ALL_PUBLISHERS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.AUTHOR_DETAILS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.BOOKS_BY_GENRE_TITLE_HTML;
import static edu.epam.bookshop.constant.HtmlPage.BOOKS_BY_KEYWORD_HTML;
import static edu.epam.bookshop.constant.HtmlPage.BOOKS_BY_YEAR_HTML;
import static edu.epam.bookshop.constant.HtmlPage.BOOKS_WITH_HIGH_SCORE_HTML;
import static edu.epam.bookshop.constant.HtmlPage.BOOK_DETAILS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.EDIT_BOOK_HTML;
import static edu.epam.bookshop.constant.HtmlPage.PROFILE_HTML;
import static edu.epam.bookshop.constant.HtmlPage.SIGN_UP_HTML;
import static edu.epam.bookshop.constant.HtmlPage.SIGN_IN_HTML;
import static edu.epam.bookshop.constant.HtmlPage.HOME_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ADMIN_CABINET_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ALL_USERS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ACCOUNT_SETTING_HTML;
import static edu.epam.bookshop.constant.HtmlPage.ALL_AUTHORS_HTML;
import static edu.epam.bookshop.constant.HtmlPage.EDIT_AUTHOR_HTML;

import static edu.epam.bookshop.constant.AttributeName.USER_ATTRIBUTE;
import static edu.epam.bookshop.constant.GetMappingURN.MAIN_URN;
import static edu.epam.bookshop.constant.GetMappingURN.REGISTRATION_URN;
import static edu.epam.bookshop.constant.GetMappingURN.LOGIN_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ADMIN_CABINET_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ALL_USERS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ACCOUNT_SETTINGS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.ALL_AUTHORS_URN;
import static edu.epam.bookshop.constant.GetMappingURN.EDIT_AUTHOR_URN;

@Controller
@AllArgsConstructor
public class ViewController {

    @GetMapping(MAIN_URN)
    public ModelAndView openHomePage(ModelAndView homeView) {
        homeView.setViewName(HOME_HTML);
        return homeView;
    }

    @GetMapping(REGISTRATION_URN)
    public ModelAndView openRegistrationPage(ModelAndView registrationView) {
        User user = User.builder().build();
        registrationView.addObject(USER_ATTRIBUTE, user);
        registrationView.setViewName(SIGN_UP_HTML);
        return registrationView;
    }

    @GetMapping(LOGIN_URN)
    public ModelAndView openLoginPage(ModelAndView loginView) {
        loginView.setViewName(SIGN_IN_HTML);
        return loginView;
    }

    @GetMapping(ADMIN_CABINET_URN)
    public ModelAndView openAdminCabinetPage(ModelAndView adminCabinetView) {
        adminCabinetView.setViewName(ADMIN_CABINET_HTML);
        return adminCabinetView;
    }

    @GetMapping(ALL_USERS_URN)
    public ModelAndView openAllUsersPage(ModelAndView allUsersView) {
        allUsersView.setViewName(ALL_USERS_HTML);
        return allUsersView;
    }

    @GetMapping(ACCOUNT_SETTINGS_URN)
    public ModelAndView openAccountSettingsPage(ModelAndView accountSettingsView) {
        accountSettingsView.setViewName(ACCOUNT_SETTING_HTML);
        return accountSettingsView;
    }

    @GetMapping(ALL_AUTHORS_URN)
    public ModelAndView openAllAuthorsPage(ModelAndView allAuthorsView) {
        allAuthorsView.setViewName(ALL_AUTHORS_HTML);
        return allAuthorsView;
    }

    @GetMapping(EDIT_AUTHOR_URN)
    public ModelAndView openEditAuthorPage(ModelAndView editAuthorView) {
        editAuthorView.setViewName(EDIT_AUTHOR_HTML);
        return editAuthorView;
    }

    @GetMapping(EDIT_BOOK_URN)
    public ModelAndView openEditBookPage(ModelAndView editBookView) {
        editBookView.setViewName(EDIT_BOOK_HTML);
        return editBookView;
    }

    @GetMapping(ALL_GENRES_URN)
    public ModelAndView openAllGenresPage(ModelAndView allGenresView) {
        allGenresView.setViewName(ALL_GENRES_HTML);
        return allGenresView;
    }

    @GetMapping(ALL_PUBLISHERS_URN)
    public ModelAndView openAllPublishersPage(ModelAndView allPublishersView) {
        allPublishersView.setViewName(ALL_PUBLISHERS_HTML);
        return allPublishersView;
    }

    @GetMapping(ALL_BOOKS_URN)
    public ModelAndView openAllBooksPage(ModelAndView allBooksView) {
        allBooksView.setViewName(ALL_BOOKS_HTML);
        return allBooksView;
    }

    @GetMapping(AUTHOR_DETAILS_URN)
    public ModelAndView openAuthorDetailsPage(ModelAndView authorDetailsView) {
        authorDetailsView.setViewName(AUTHOR_DETAILS_HTML);
        return authorDetailsView;
    }

    @GetMapping(BOOK_DETAILS_URN)
    public ModelAndView openBookDetailsPage(ModelAndView bookDetailsView) {
        bookDetailsView.setViewName(BOOK_DETAILS_HTML);
        return bookDetailsView;
    }

    @GetMapping(BOOKS_BY_GENRE_TITLE_URN)
    public ModelAndView openBooksByGenreTitlePage(ModelAndView booksByGenreTitleView) {
        booksByGenreTitleView.setViewName(BOOKS_BY_GENRE_TITLE_HTML);
        return booksByGenreTitleView;
    }

    @GetMapping(BOOKS_BY_YEAR_URN)
    public ModelAndView openBooksByYearPage(ModelAndView booksByYearView) {
        booksByYearView.setViewName(BOOKS_BY_YEAR_HTML);
        return booksByYearView;
    }

    @GetMapping(BOOKS_BY_KEYWORD_PAGE_URN)
    public ModelAndView openBooksByKeyWordAndPage(ModelAndView booksByKeyWordAndPageView) {
        booksByKeyWordAndPageView.setViewName(BOOKS_BY_KEYWORD_HTML);
        return booksByKeyWordAndPageView;
    }

    @GetMapping(BOOKS_WITH_HIGH_SCORE_PAGE_URN)
    public ModelAndView openBooksWithHighScorePage(ModelAndView booksWithHighScoreView) {
        booksWithHighScoreView.setViewName(BOOKS_WITH_HIGH_SCORE_HTML);
        return booksWithHighScoreView;
    }

    @GetMapping(USER_PROFILE_PAGE_URN)
    public ModelAndView openUserProfilePage(ModelAndView userProfileView) {
        userProfileView.setViewName(PROFILE_HTML);
        return userProfileView;
    }
}
