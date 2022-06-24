package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
