package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import edu.epam.bookshop.exception.*;
import edu.epam.bookshop.repository.RoleRepository;
import edu.epam.bookshop.repository.UserRepository;
import edu.epam.bookshop.service.impl.UserServiceImpl;
import edu.epam.bookshop.util.PasswordEncoder;
import edu.epam.bookshop.validator.ImageValidator;
import edu.epam.bookshop.validator.UserValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static edu.epam.bookshop.constant.ExceptionMessage.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;
    @Mock
    private ImageValidator imageValidator;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(passwordEncoder,
                userRepository,
                roleRepository,
                userValidator,
                imageValidator);
    }

//    @Test
//    void willAddUser() {
//        //given
//        Role role = Role.builder()
//                .roleName("USER")
//                .build();
//        Set<Role> userRoles = new HashSet<>(List.of(role));
//        User givenUser = User.builder()
//                .userName("Stas")
//                .password(("1234"))
//                .email("Lanret@gmai.com")
//                .avatarName("123")
//                .roles(userRoles)
//                .locked(false)
//                .build();
//
//        //when
//        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(role));
//        userService.addUser(givenUser);
//
//        User expectedUser = User.builder()
//                .userName("Stas")
//                .password(PasswordEncoder.encode("Ldkjs397"))
//                .email("Lanret@gmai.com")
//                .avatarName("123")
//                .roles(userRoles)
//                .locked(false)
//                .build();
//
//        //then
//        ArgumentCaptor<User> userArgumentCaptor =
//                ArgumentCaptor.forClass(User.class);
//        verify(userRepository).save(userArgumentCaptor.capture());
//
//        User capturedUser = userArgumentCaptor.getValue();
//        assertThat(capturedUser).isEqualTo(expectedUser);
//    }

    @Test
    void addUserWillThrowExceptionWhenPasswordIsNotValid() {
        //given
        String givenPassword = "123";
        String givenUsername = "Stanislau";
        String givenEmail = "Stanislau@gmail.com";
        User givenUser = User.builder()
                .userName(givenUsername)
                .password(givenPassword)
                .email(givenEmail)
                .build();
        //when
        when(userValidator.isUsernameValid(givenUsername)).thenReturn(true);
        when(userValidator.isEmailValid(givenEmail)).thenReturn(true);
        when(userValidator.isPasswordValid(givenPassword)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.addUser(givenUser))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(INVALID_PASSWORD_MSG);
    }

    @Test
    void addUserWillThrowExceptionWhenUsernameIsNotValid() {
        //given
        String givenPassword = "123";
        String givenUsername = "Stanislau";
        String givenEmail = "Stanislau@gmail.com";
        User givenUser = User.builder()
                .userName(givenUsername)
                .password(givenPassword)
                .email(givenEmail)
                .build();
        //when
        when(userValidator.isUsernameValid(givenUsername)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.addUser(givenUser))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(INVALID_USER_NAME_MSG);
    }

    @Test
    void addUserWillThrowExceptionWhenEmailIsNotValid() {
        //given
        String givenPassword = "123";
        String givenUsername = "Stanislau";
        String givenEmail = "222";
        User givenUser = User.builder()
                .userName(givenUsername)
                .password(givenPassword)
                .email(givenEmail)
                .build();
        //when
        when(userValidator.isUsernameValid(givenUsername)).thenReturn(true);
        when(userValidator.isEmailValid(givenEmail)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.addUser(givenUser))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(INVALID_EMAIL_MSG);
    }

    @Test
    void addUserWillThrowExceptionWhenUsernameTaken() {
        //given
        Role role = Role.builder()
                .roleName("USER")
                .build();
        Set<Role> userRoles = new HashSet<>(List.of(role));
        User givenUser = User.builder()
                .userName("Stas")
                .password(("1234"))
                .email("Lanret@gmai.com")
                .avatarName("123")
                .roles(userRoles)
                .locked(false)
                .build();
        //when
        when(userRepository.existsByUserName("Stas")).thenReturn(true);
        //then
        assertThatThrownBy(() -> userService.addUser(givenUser))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage(USER_NAME_TAKEN);

    }

    @Test
    void addUserWillThrowExceptionWhenEmailTaken() {
        //given
        Role role = Role.builder()
                .roleName("USER")
                .build();
        Set<Role> userRoles = new HashSet<>(List.of(role));
        User givenUser = User.builder()
                .userName("Stas")
                .password(("Ldkjs397"))
                .email("Lanret@gmail.com")
                .avatarName("123")
                .roles(userRoles)
                .locked(false)
                .build();
        //when
        when(userRepository.existsByEmail("Lanret@gmail.com")).thenReturn(true);
        //then
        assertThatThrownBy(() -> userService.addUser(givenUser))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage(EMAIL_TAKEN);

    }

    @Test
    void willUpdateUserStatusByUsername() {
        //given
        String username = "Stanislau";
        User user = User.builder()
                .userName(username)
                .locked(false)
                .build();
        //when
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        userService.updateUserStatusByUsername(username);
        //then
        verify(userRepository, times(1)).updateStatusByUsernameAndStatus(true, username);
    }

    @Test
    void updateUserStatusByUsernameWillThrowExceptionWhenUserNotFound() {
        //given
        String userName = "Stanislau";
        //when
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.updateUserStatusByUsername(userName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName);
    }

//    @Test
//    void updateUserAvatarByUsernameTrue() {
//        //given
//        String userName = "Stanislau";
//        MockMultipartFile avatar = new MockMultipartFile("data", "filename.kml", "text/plain", "some kml".getBytes());
//        //when
//        when(userRepository.existsByUserName(userName)).thenReturn(true);
//        when(avatar.getOriginalFilename()).thenReturn("image.jpg");
//        when(userValidator.isImageValid(avatar.getOriginalFilename())).thenReturn(true);
//        userService.updateUserAvatarByUsername(avatar, userName);
//        //then
//        verify(userRepository, times(1)).updateAvatarByUsername(avatar.getOriginalFilename(), userName);
//    }

    @Test
    void willUpdateUserRole() {
        //given
        long userId = 2;
        long roleId = 2;
        //when
        when(userRepository.existsById(userId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        userService.updateUserRoleByUserIdAndRoleId(userId, roleId);
        //then
        verify(userRepository, times(1)).updateRoleByUserIdAndRoleId(userId, roleId);
    }

    @Test
    void updateUserRoleWillThrowExceptionWhenUserDoesNotExist() {
        //given
        long userId = 2;
        long roleId = 2;
        //when
        when(userRepository.existsById(userId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.updateUserRoleByUserIdAndRoleId(userId, roleId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(USER_WITH_GIVEN_ID_NOT_FOUND, userId)
                );
    }

    @Test
    void updateUserRoleWillThrowExceptionWhenRoleDoesNotExist() {
        //given
        long userId = 2;
        long roleId = 2;
        //when
        when(userRepository.existsById(userId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.updateUserRoleByUserIdAndRoleId(userId, roleId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(
                        String.format(ROLE_WITH_GIVEN_ID_NOT_FOUND, roleId)
                );
    }

    @Test
    void findUserByUserNameWillThrowExceptionWhenUserNotFound() {
        //given
        String userName = "Stanislau";
        Optional<User> emptyUserOptional = Optional.empty();
        //when
        when(userRepository.findByUserName(userName))
                .thenReturn(emptyUserOptional);
        //then
        assertThatThrownBy(() -> userService.findUserByUserName(userName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName));
    }

    @Test
    void findUserByUserNameWillReturnUser() {
        //given
        String userName = "Stanislau";
        User givenUser = User.builder()
                .userName(userName)
                .build();
        //when
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(givenUser));
        User expectedUser = userService.findUserByUserName(userName);
        //then
        assertThat(givenUser).isEqualTo(expectedUser);
    }

    @Test
    void findUserByIdWillReturnUser() {
        //given
        long userId = 1;
        Role role = Role.builder()
                .roleName("USER")
                .build();
        Set<Role> userRoles = new HashSet<>(List.of(role));
        User givenUser = User.builder()
                .userId(userId)
                .userName("Stas")
                .password(("Ldkjs397"))
                .email("Lanret@gmail.com")
                .avatarName("123")
                .roles(userRoles)
                .locked(false)
                .build();
        //when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(givenUser));
        User expectedUser = userService.findUserById(userId);
        //then
        assertThat(expectedUser).isEqualTo(givenUser);
    }

    @Test
    void findUserByIdWillThrowExceptionWhenUserDoesNotExists() {
        //given
        long userId = 1;
        //when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.findUserById(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("User with user_id: %d not found", userId));
    }

    @Test
    void findAllUsersWillThrowExceptionWhenListIsEmpty() {
        //given
        List<User> emptyList = new ArrayList<>();
        given(userRepository.findAll()).willReturn(emptyList);
        //then
        assertThatThrownBy(() -> userService.findAllUsers())
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    @SneakyThrows
    void willReturnAllUser() {
        //given
        List<User> allUsers = List.of(new User());
        given(userRepository.findAll()).willReturn(allUsers);
        //when
        List<User> expected = userService.findAllUsers();
        //then
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected).isNotNull();
    }

    @Test
    void willReturnRoleByName() {
        //given
        String roleName = "USER";
        Role givenRole = Role.builder()
                .roleId(1L)
                .roleName(roleName)
                .build();
        //when
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(givenRole));
        Role expectedRole = userService.findRoleByName(roleName);
        //then
        assertThat(expectedRole).isEqualTo(givenRole);
    }

    @Test
    void findRoleByNameWillThrowExceptionWhenRoleDoesNotExist() {
        //given
        String roleName = "USER";
        //when
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.findRoleByName(roleName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(ROLE_WITH_GIVEN_NAME_DOESNT_EXIST, roleName));
    }

    @Test
    void userExistsByUserNameWillReturnTrue() {
        //given
        String userName = "Stanislau";
        //when
        when(userRepository.existsByUserName(userName)).thenReturn(true);
        boolean condition = userService.userExistsByUserName(userName);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void userExistsByUserNameWillReturnFalse() {
        //given
        String userName = "Stanislau";
        //when
        when(userRepository.existsByUserName(userName)).thenReturn(false);
        boolean condition = userService.userExistsByUserName(userName);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void userExistsByEmailWillReturnTrue() {
        //given
        String email = "Stanislau@gmail.com";
        //when
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean condition = userService.userExistsByEmail(email);
        //then
        assertThat(condition).isTrue();
    }

    @Test
    void userExistsByEmailWillReturnFalse() {
        //given
        String email = "Stanislau@gmail.com";
        //when
        when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean condition = userService.userExistsByEmail(email);
        //then
        assertThat(condition).isFalse();
    }

    @Test
    void willReturnUsersByKeyword() {
        //given
        User firstUser = User.builder().userName("Stanislau").build();
        User secondUser = User.builder().userName("Ilya").build();
        List<User> givenUsersList = List.of(firstUser, secondUser);
        //when
        when(userRepository.findAll()).thenReturn(givenUsersList);
        List<User> expectedUsersList = userService.findUsersByKeyword("Stan");
        //then
        assertThat(expectedUsersList).isNotNull();
        assertThat(expectedUsersList.size()).isEqualTo(1);
        assertThat(expectedUsersList.get(0).getUserName()).isEqualTo("Stanislau");
    }

    @Test
    void findUsersByKeywordWillThrowNothingFoundException() {
        //given
        User firstUser = User.builder().userName("Stanislau").build();
        User secondUser = User.builder().userName("Ilya").build();
        List<User> givenUsersList = List.of(firstUser, secondUser);
        //when
        when(userRepository.findAll()).thenReturn(givenUsersList);
        String keyword = "Hanna";
        //then
        assertThatThrownBy(() -> userService.findUsersByKeyword(keyword))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, keyword));
    }

    @Test
    void willFindUsersWithPagination() {
        //given
        Pageable pageable = PageRequest.of(0, 6);
        User firstUser = User.builder().userName("Stanislau").build();
        User secondUser = User.builder().userName("Ilya").build();
        List<User> users = List.of(firstUser, secondUser);
        Page<User> usersWithPagination = new PageImpl<>(users);
        //when
        when(userRepository.findAll(pageable)).thenReturn(usersWithPagination);
        Page<User> expectedUsersWithPagination = userService.findUsersWithPagination(1);
        //then
        assertThat(expectedUsersWithPagination.getNumberOfElements()).isEqualTo(2);
        assertThat(expectedUsersWithPagination.getNumber()).isEqualTo(0);
        assertThat(expectedUsersWithPagination.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findUsersWithPaginationWillThrowNothingFoundException() {
        //given
        Pageable pageable = PageRequest.of(1, 6);
        List<User> users = new ArrayList<>();
        Page<User> usersWithPagination = new PageImpl<>(users);
        //when
        when(userRepository.findAll(pageable)).thenReturn(usersWithPagination);
        //then
        assertThatThrownBy(() -> userService.findUsersWithPagination(2))
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }

    @Test
    void willUpdateUserPassword() {
        //given
        String userName = "Stanislau";
        String oldPassword = "12345";
        String newPassword = "12345";
        String encodedPassword = passwordEncoder.encode(newPassword);
        User user = User.builder()
                .userName(userName)
                .password(encodedPassword)
                .build();
        //when
        when(userRepository.findByUserName(userName))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, encodedPassword))
                .thenReturn(true);
        when(userValidator.isPasswordValid(newPassword))
                .thenReturn(true);
        when(passwordEncoder.encode(newPassword))
                .thenReturn(encodedPassword);
        userService.updateUserPasswordByUsernameAndOldPassword(oldPassword, newPassword, userName);
        //then
        verify(userRepository, times(1))
                .updatePasswordByUsername(encodedPassword, userName);
    }

    @Test
    void updateUserPasswordWillThrowExceptionWhenUserNotFound() {
        //given
        String userName = "Stanislau";
        String oldPassword = "Ldkowerq";
        String newPassword = "Ldkowqwe";
        //when
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.updateUserPasswordByUsernameAndOldPassword(newPassword, oldPassword, userName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(USER_WITH_GIVEN_NAME_NOT_FOUND_MSG, userName));
    }

    @Test
    void updateUserPasswordWillThrowExceptionWhenOldPasswordIsInvalid() {
        //given
        String userName = "Stanislau";
        String oldPassword = "Ldkowerq";
        String newPassword = "Ldkowqwe";
        String encodedPassword = passwordEncoder.encode(oldPassword);
        User user = User.builder()
                .userName(userName)
                .password(encodedPassword)
                .build();
        //when
        when(userRepository.findByUserName(userName))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, encodedPassword)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.updateUserPasswordByUsernameAndOldPassword(
                newPassword,
                oldPassword,
                userName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(CURRENT_PASSWORD_IS_INVALID);
    }

    @Test
    void updateUserPasswordWillThrowExceptionWhenNewPasswordIsINotValid() {
        //given
        String userName = "Stanislau";
        String oldPassword = "Ldkowerq";
        String newPassword = "Ldkowqwe";
        String encodedPassword = passwordEncoder.encode(oldPassword);
        User user = User.builder()
                .userName(userName)
                .password(encodedPassword)
                .build();
        //when
        when(userRepository.findByUserName(userName))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, encodedPassword)).thenReturn(true);
        when(userValidator.isPasswordValid(newPassword)).thenReturn(false);
        //then
        assertThatThrownBy(() -> userService.updateUserPasswordByUsernameAndOldPassword(
                newPassword,
                oldPassword,
                userName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(INVALID_PASSWORD_MSG);
    }

    @Test
    void willFindAllRoles() {
        //given
        Role givenRole = Role.builder()
                .roleName("USER")
                .build();
        List<Role> givenRoles = List.of(givenRole);
        //when
        when(roleRepository.findAll()).thenReturn(givenRoles);
        Set<Role> expectedRoles = userService.findAllRoles();
        //then
        assertThat(expectedRoles).isNotNull();
        assertThat(expectedRoles.size()).isEqualTo(1);
    }

    @Test
    void findAllRolesWillThrowExceptionWhenRolesNotFound() {
        //given
        List<Role> emptyRolesList = new ArrayList<>();
        //when
        when(roleRepository.findAll()).thenReturn(emptyRolesList);
        //then
        assertThatThrownBy(() -> userService.findAllRoles())
                .isInstanceOf(NothingFoundException.class)
                .hasMessageContaining(NOTHING_WAS_FOUND_MSG);
    }


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}