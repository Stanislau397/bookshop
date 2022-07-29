package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;

import java.util.List;
import java.util.Set;

import static edu.epam.bookshop.entity.constant.PropertyId.USER_ID_PROPERTY;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.EMAIL;
import static edu.epam.bookshop.entity.constant.TableColumn.PASSWORD;
import static edu.epam.bookshop.entity.constant.TableColumn.AVATAR;
import static edu.epam.bookshop.entity.constant.TableColumn.LOCKED;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.ROLE_ID_FK;

import static edu.epam.bookshop.entity.constant.TableName.USERS_TABLE;
import static edu.epam.bookshop.entity.constant.TableName.USERS_ROLES;

@Entity
@Table(name = USERS_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = USER_ID_PROPERTY)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USER_ID,
            nullable = false,
            unique = true)
    private Long userId;

    @Column(name = USER_NAME,
            unique = true,
            nullable = false)
    private String userName;

    @Column(name = EMAIL,
            unique = true,
            nullable = false)
    private String email;

    @Column(name = PASSWORD,
            unique = true,
            nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = AVATAR)
    private String avatarName;

    @Column(name = LOCKED,
            nullable = false)
    private Boolean locked;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = USERS_ROLES,
            joinColumns = @JoinColumn(name = USER_ID_FK),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID_FK))
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<BookReview> bookReviews;

    @OneToOne(mappedBy = "userRelatedToShelve")
    private BookShelve bookShelve;
}
