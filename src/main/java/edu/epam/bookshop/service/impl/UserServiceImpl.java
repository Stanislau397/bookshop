package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.exception.*;
import edu.epam.bookshop.repository.RoleRepository;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.service.UserService;
import edu.epam.bookshop.util.ImageUploaderUtil;
import edu.epam.bookshop.util.PasswordEncoder;
import edu.epam.bookshop.validator.ImageValidator;
import edu.epam.bookshop.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.epam.bookshop.constant.ExceptionMessage.USER_NAME_TAKEN;
import static edu.epam.bookshop.constant.ExceptionMessage.EMAIL_TAKEN;
import static edu.epam.bookshop.constant.ExceptionMessage.INVALID_USER_NAME_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.INVALID_EMAIL_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.INVALID_PASSWORD_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_NAME_NOT_FOUND_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.NOTHING_WAS_FOUND_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.FILE_IS_EMPTY_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.IMAGE_IS_NOT_VALID_MSG;
import static edu.epam.bookshop.constant.ExceptionMessage.CURRENT_PASSWORD_IS_INVALID;
import static edu.epam.bookshop.constant.ExceptionMessage.ROLE_WITH_GIVEN_NAME_DOESNT_EXIST;
import static edu.epam.bookshop.constant.ExceptionMessage.ROLE_WITH_GIVEN_ID_NOT_FOUND;
import static edu.epam.bookshop.constant.ExceptionMessage.USER_WITH_GIVEN_ID_NOT_FOUND;

import static edu.epam.bookshop.constant.ImageStoragePath.AVATARS_LOCALHOST_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.AVATARS_DIRECTORY_PATH;
import static edu.epam.bookshop.constant.ImageStoragePath.DEFAULT_AVATAR_PATH;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "USER";
    private static final int AMOUNT_OF_USERS_PER_PAGE = 6;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private UserValidator userValidator;
    private ImageValidator imageValidator;

    @Override
    public void addUser(User user) { //todo test willAdd
        boolean usernameIsTaken = userExistsByUserName(user.getUserName());
        if (usernameIsTaken) {
            log.info(USER_NAME_TAKEN);
            throw new EntityAlreadyExistsException(USER_NAME_TAKEN);
        }

        boolean emailIsTaken = userRepository.existsByEmail(user.getEmail());
        if (emailIsTaken) {
            log.info(EMAIL_TAKEN);
            throw new EntityAlreadyExistsException(EMAIL_TAKEN);
        }

        boolean usernameValid = userValidator.isUsernameValid(user.getUserName());
        if (!usernameValid) {
            log.info(INVALID_USER_NAME_MSG);
            throw new InvalidInputException(INVALID_USER_NAME_MSG);
        }

        boolean emailValid = userValidator.isEmailValid(user.getEmail());
        if (!emailValid) {
            log.info(INVALID_EMAIL_MSG);
            throw new InvalidInputException(INVALID_EMAIL_MSG);
        }

        boolean passwordValid = userValidator.isPasswordValid(user.getPassword());
        if (!passwordValid) {
            log.info(INVALID_PASSWORD_MSG);
            throw new InvalidInputException(INVALID_PASSWORD_MSG);
        }

        Role role = findRoleByName(ROLE_USER);
        String encodedPassword = PasswordEncoder.encode(user.getPassword());
        User userToSave = User.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .avatarName(DEFAULT_AVATAR_PATH)
                .roles(new HashSet<>(List.of(role)))
                .locked(false)
                .build();
        userRepository.save(userToSave);
    }

    @Override
    public void updateUserStatusByUsername(String userName) { //todo return false
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName)
                ));
        boolean userLocked = user.getLocked();
        if (!userLocked) {
            userRepository.updateStatusByUsernameAndStatus(true, userName);
        } else {
            userRepository.updateStatusByUsernameAndStatus(false, userName);
        }
    }

    @Override
    public void updateUserAvatarByUsername(MultipartFile avatar, String userName) { //todo test
        boolean userExistsByUsername = userRepository.existsByUserName(userName);
        if (!userExistsByUsername) {
            throw new EntityNotFoundException(
                    String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName)
            );
        }
        if (!nonNull(avatar)) {
            throw new FileIsEmptyException(FILE_IS_EMPTY_MSG);
        }
        String imageName = avatar.getOriginalFilename();
        if (nonNull(imageName)) {
            boolean imageValid = imageValidator.isImageValid(imageName);
            if (!imageValid) {
                throw new InvalidInputException(IMAGE_IS_NOT_VALID_MSG);
            }
        }
        String avatarPath = AVATARS_LOCALHOST_PATH.concat(
                ImageUploaderUtil.save(avatar, AVATARS_DIRECTORY_PATH)
        );
        userRepository.updateAvatarByUsername(avatarPath, userName);
    }

    @Override
    public void updateUserRoleByUserIdAndRoleId(Long userId, Long roleId) {
        boolean userExistsByUserId = userRepository.existsById(userId);
        if (!userExistsByUserId) {
            log.info(String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId));
            throw new EntityNotFoundException(
                    String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId)
            );
        }
        boolean roleExistsById = roleRepository.existsById(roleId);
        if (!roleExistsById) {
            log.info(String.format(ROLE_WITH_GIVEN_ID_NOT_FOUND, roleId));
            throw new EntityNotFoundException(
                    String.format(ROLE_WITH_GIVEN_ID_NOT_FOUND, roleId)
            );
        }
        userRepository.updateRoleByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public void updateUserPasswordByUsernameAndOldPassword(String newPassword,
                                                           String oldPassword,
                                                           String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName)
                ));
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidInputException(CURRENT_PASSWORD_IS_INVALID);
        }
        boolean isNewPasswordValid = userValidator.isPasswordValid(newPassword);
        if (!isNewPasswordValid) {
            throw new InvalidInputException(INVALID_PASSWORD_MSG);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        userRepository.updatePasswordByUsername(encodedPassword, userName);
    }

    @Override
    public boolean userExistsByUserName(String userName) {
        boolean usernameExists = userRepository.existsByUserName(userName);
        if (usernameExists) {
            log.info(USER_NAME_TAKEN);
        }
        return usernameExists;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            log.info(EMAIL_TAKEN);
        }
        return emailExists;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with user_id: %d not found", userId))
                );
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName))
                );
    }

    @Override
    public User findUserByBookReviewId(Long bookReviewId) { //todo test
        return userRepository.selectUserByReviewId(bookReviewId)
                .orElseThrow(() -> {
                    log.info("asd");
                    return new EntityNotFoundException("asd");
                });
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new ServiceException(NOTHING_WAS_FOUND_MSG);
        }
        return allUsers;
    }

    @Override
    public Page<User> findUsersWithPagination(int page) {
        Pageable pageWithUsers = PageRequest.of(page - 1, AMOUNT_OF_USERS_PER_PAGE);
        Page<User> usersWithPagination = userRepository.findAll(pageWithUsers);
        if (usersWithPagination.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return usersWithPagination;
    }

    @Override
    public List<User> findUsersByKeyword(String keyword) {
        List<User> usersByKeyword = userRepository.findAll()
                .stream()
                .filter(o -> o.getUserName().toLowerCase().contains(keyword.toLowerCase()))
                .limit(AMOUNT_OF_USERS_PER_PAGE)
                .toList();
        if (usersByKeyword.isEmpty()) {
            throw new NothingFoundException(
                    String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, keyword)
            );
        }
        return usersByKeyword;
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ROLE_WITH_GIVEN_NAME_DOESNT_EXIST, roleName))
                );
    }

    @Override
    public Set<Role> findAllRoles() {
        List<Role> foundRoles = roleRepository.findAll();
        if (foundRoles.isEmpty()) {
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return new HashSet<>(foundRoles);
    }
}
