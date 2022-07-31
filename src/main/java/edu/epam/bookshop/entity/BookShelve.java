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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import java.util.List;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_STATUS;
import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID_FK;
import static edu.epam.bookshop.entity.constant.TableName.BOOK_SHELVE;
import static edu.epam.bookshop.entity.constant.TableName.SHELVE_BOOKS;

@Table(name = BOOK_SHELVE)
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@SecondaryTable(name = SHELVE_BOOKS, pkJoinColumns = @PrimaryKeyJoinColumn(
        name = SHELVE_ID_FK,
        referencedColumnName = SHELVE_ID))
public class BookShelve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SHELVE_ID,
            nullable = false,
            unique = true)
    private Long bookShelveId;

    @Column(name = BOOK_STATUS, table = SHELVE_BOOKS)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private BookStatus bookStatus;

    @ManyToMany(mappedBy = "bookShelves")
    @JsonIgnore
    private List<Book> shelveBooks;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = USER_ID_FK, referencedColumnName = USER_ID)
    @JsonIgnore
    private User userRelatedToShelve;
}
