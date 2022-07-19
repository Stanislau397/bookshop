package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Role;
import edu.epam.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_USER_BY_REVIEW_ID;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_USER_STATUS_BY_USERNAME;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_USER_ROLES_BY_USERNAME;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_USER_AVATAR_BY_USERNAME;
import static edu.epam.bookshop.repository.SqlQuery.UPDATE_USER_PASSWORD_BY_USERNAME;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    @Transactional
    @Modifying
    @Query(UPDATE_USER_STATUS_BY_USERNAME)
    void updateStatusByUsernameAndStatus(boolean status, String userName);

    @Transactional
    @Modifying
    @Query(UPDATE_USER_AVATAR_BY_USERNAME)
    void updateAvatarByUsername(String avatarPath, String userName);

//    @Transactional
//    @Modifying
//    @Query(value = UPDATE_USER_ROLES_BY_USERNAME, nativeQuery = true)
//    void updateRoleByUsername(String userName, Set<Role> roles);

    @Transactional
    @Modifying
    @Query(value = UPDATE_USER_ROLES_BY_USERNAME, nativeQuery = true)
    void updateRoleByUserIdAndRoleId(Long userId, Long roleId);

    @Transactional
    @Modifying
    @Query(UPDATE_USER_PASSWORD_BY_USERNAME)
    void updatePasswordByUsername(String password, String userName);

    @Query(SELECT_USER_BY_REVIEW_ID)
    Optional<User> selectUserByReviewId(Long reviewId);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Long userId);
}
