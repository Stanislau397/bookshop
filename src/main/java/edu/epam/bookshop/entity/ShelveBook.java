package edu.epam.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_STATUS;
import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_ID_FK;
import static edu.epam.bookshop.entity.constant.TableName.SHELVE_BOOKS;

@Entity
@Table(name = SHELVE_BOOKS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ShelveBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SHELVE_BOOK_ID)
    private Long shelveBookId;

    @ManyToOne
    @JoinColumn(name = BOOK_ID_FK)
    private Book book;

    @ManyToOne
    @JoinColumn(name = SHELVE_ID_FK)
    private BookShelve bookShelve;

    @Column(name = BOOK_STATUS)
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
}
