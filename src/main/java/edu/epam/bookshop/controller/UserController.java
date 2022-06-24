package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static edu.epam.bookshop.controller.constant.GetMappingURN.CHECK_IF_USER_NAME_TAKEN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.CHECK_IF_EMAIL_TAKEN_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_USERS_WITH_PAGINATION_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_USERS_BY_KEYWORD;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_USER_BY_USERNAME;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_ALL_ROLES;

import static edu.epam.bookshop.controller.constant.PostMappingURN.REGISTER_USER_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_STATUS_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_AVATAR_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_PASSWORD_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_USER_ROLE_URN;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER_USER_URN)
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_USER_STATUS_URN)
    public ResponseEntity<Void> changeUserStatusByUsername(@RequestParam String userName) {
        userService.updateUserStatusByUsername(userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_USER_AVATAR_URN)
    public ResponseEntity<Void> changeUserAvatarByUsername(@RequestParam MultipartFile avatar,
                                                           @RequestParam String userName) {
        userService.updateUserAvatarByUsername(avatar, userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_USER_ROLE_URN)
    public ResponseEntity<Void> changeUserRole(@RequestParam long userId,
                                               @RequestParam long roleId) {
        userService.updateUserRoleByUserIdAndRoleId(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_USER_PASSWORD_URN)
    public ResponseEntity<Void> changeUserPassword(@RequestParam String oldPassword,
                                                   @RequestParam String newPassword,
                                                   @RequestParam String userName) {
        userService.updateUserPasswordByUsernameAndOldPassword(newPassword, oldPassword, userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping(CHECK_IF_USER_NAME_TAKEN_URN)
    public ResponseEntity<Boolean> isUserExistsByUsername(@RequestParam String username) {
        boolean isUsernameTaken = userService.userExistsByUserName(username);
        return ResponseEntity.ok(isUsernameTaken);
    }

    @GetMapping(CHECK_IF_EMAIL_TAKEN_URN)
    public ResponseEntity<Boolean> isUserExistsByEmail(@RequestParam String email) {
        boolean isEmailTaken = userService.userExistsByEmail(email);
        return ResponseEntity.ok(isEmailTaken);
    }

    @GetMapping(FIND_USERS_WITH_PAGINATION_URN)
    public ResponseEntity<Page<User>> getUsersWithPagination(@RequestParam int page) {
        Page<User> usersWithPagination = userService.findUsersWithPagination(page);
        return ResponseEntity.ok(usersWithPagination);
    }

    @GetMapping(FIND_USERS_BY_KEYWORD)
    public ResponseEntity<List<User>> getUsersByKeyword(@RequestParam String keyword) {
        List<User> usersByKeyword = userService.findUsersByKeyword(keyword);
        return ResponseEntity.ok(usersByKeyword);
    }

    @GetMapping(FIND_USER_BY_USERNAME)
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User userByUsername = userService.findUserByUserName(username);
        return ResponseEntity.ok(userByUsername);
    }

    @GetMapping(FIND_ALL_ROLES)
    public ResponseEntity<Set<Role>> getAllRoles() {
        Set<Role> allRoles = userService.findAllRoles();
        return ResponseEntity.ok(allRoles);
    }
}
