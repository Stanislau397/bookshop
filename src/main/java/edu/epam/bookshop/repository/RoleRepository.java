package edu.epam.bookshop.repository;

import edu.epam.bookshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static edu.epam.bookshop.repository.SqlQuery.SELECT_ROLES_BY_USERNAME;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

    @Query(value = SELECT_ROLES_BY_USERNAME)
    List<Role> selectByUserName(String userName);
}
