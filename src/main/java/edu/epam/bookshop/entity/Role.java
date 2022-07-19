package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.List;

import static edu.epam.bookshop.entity.constant.PropertyId.AUTHOR_ID_PROPERTY;
import static edu.epam.bookshop.entity.constant.TableName.ROLES;

import static edu.epam.bookshop.entity.constant.TableColumn.ROLE_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.ROLE_ID;

@Entity
@Table(name = ROLES)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "roleId")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ROLE_ID,
            unique = true,
            nullable = false)
    private Long roleId;

    @Column(name = ROLE_NAME,
            nullable = false,
            unique = true)
    private String roleName;

    @ManyToMany(mappedBy = "roles",
            targetEntity = User.class,
            cascade = CascadeType.MERGE)
    List<User> users;
}
