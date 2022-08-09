package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Set;

import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID_FK;
import static edu.epam.bookshop.entity.constant.TableName.BOOK_SHELVE;

@Entity
@Table(name = BOOK_SHELVE)
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookShelve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SHELVE_ID,
            nullable = false,
            unique = true)
    private Long bookShelveId;

    @OneToMany(mappedBy = "bookShelve", cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<ShelveBook> shelveBooks;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = USER_ID_FK, referencedColumnName = USER_ID)
    @JsonIgnore
    private User userRelatedToShelve;
}
