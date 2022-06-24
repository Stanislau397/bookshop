package edu.epam.bookshop.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.TITLE;
import static edu.epam.bookshop.entity.constant.TableColumn.DESCRIPTION;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;
import static edu.epam.bookshop.entity.constant.TableColumn.ISBN;
import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISH_DATE;
import static edu.epam.bookshop.entity.constant.TableColumn.PRICE;
import static edu.epam.bookshop.entity.constant.TableColumn.PAGES;
import static edu.epam.bookshop.entity.constant.TableColumn.COVER_TYPE;
import static edu.epam.bookshop.entity.constant.TableColumn.AUTHOR_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.GENRE_ID_FK;

import static edu.epam.bookshop.entity.constant.TableName.BOOKS;
import static edu.epam.bookshop.entity.constant.TableName.AUTHOR_BOOKS;
import static edu.epam.bookshop.entity.constant.TableName.BOOK_GENRES;

@Entity
@Table(name = BOOKS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BOOK_ID,
            nullable = false,
            unique = true)
    private Long bookId;

    @Column(name = TITLE,
            nullable = false)
    private String title;

    @Column(name = PRICE)
    private BigDecimal price;

    @Column(name = IMAGE_PATH)
    private String imagePath;

    @Column(name = PUBLISH_DATE)
    private LocalDate publishDate;

    @Column(name = ISBN)
    private String isbn;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = PAGES)
    private Integer pages;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = COVER_TYPE)
    private CoverType coverType;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = BOOK_GENRES,
            joinColumns = @JoinColumn(name = GENRE_ID_FK),
            inverseJoinColumns = @JoinColumn(name = BOOK_ID_FK))
    private Set<Genre> genres;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = AUTHOR_BOOKS,
            joinColumns = @JoinColumn(name = BOOK_ID_FK),
            inverseJoinColumns = @JoinColumn(name = AUTHOR_ID_FK))
    private Author author;
}
