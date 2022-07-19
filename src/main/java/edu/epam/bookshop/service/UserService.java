package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

    void addUser(User user);

    void updateUserStatusByUsername(String userName);

    void updateUserAvatarByUsername(MultipartFile avatar, String userName);

    void updateUserRoleByUserIdAndRoleId(Long userId, Long roleId);

    void updateUserPasswordByUsernameAndOldPassword(String newPassword, String oldPassword, String userName);

    boolean userExistsByUserName(String userName);

    boolean userExistsByEmail(String email);

    User findUserById(Long userId);

    User findUserByUserName(String userName);

    User findUserByBookReviewId(Long bookReviewId);

    List<User> findAllUsers() throws ServiceException;

    Page<User> findUsersWithPagination(int page);

    List<User> findUsersByKeyword(String keyword);

    Role findRoleByName(String roleName);

    Set<Role> findAllRoles();
}
