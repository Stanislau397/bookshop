package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISHER_ID_FK;
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
import static edu.epam.bookshop.entity.constant.TableName.PUBLISHER_BOOKS;

@Entity
@Table(name = BOOKS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BOOK_ID)
    private Long bookId;

    @Column(name = TITLE)
    private String title;

    @Column(name = PRICE)
    private BigDecimal price;

    @Column(name = IMAGE_PATH)
    private String imagePath;

    @Column(name = PUBLISH_DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @Column(name = ISBN)
    private String isbn;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = PAGES)
    private Integer pages;

    @Column(name = COVER_TYPE)
    @Enumerated(EnumType.STRING)
    private CoverType coverType;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = BOOK_ID_FK,
            referencedColumnName = BOOK_ID)
    @JsonIgnoreProperties("book")
    private List<LocalizedBook> localizedBooks;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = BOOK_GENRES,
            joinColumns = @JoinColumn(name = BOOK_ID_FK),
            inverseJoinColumns = @JoinColumn(name = GENRE_ID_FK))
    @JsonIgnoreProperties("books")
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = PUBLISHER_BOOKS,
            joinColumns = @JoinColumn(name = BOOK_ID_FK),
            inverseJoinColumns = @JoinColumn(name = PUBLISHER_ID_FK))
    @JsonIgnoreProperties("publishedBooks")
    private List<Publisher> publishers;

    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name = AUTHOR_BOOKS,
            joinColumns = @JoinColumn(name = BOOK_ID_FK),
            inverseJoinColumns = @JoinColumn(name = AUTHOR_ID_FK))
    @JsonIgnoreProperties("books")
    private List<Author> authors;

    @OneToMany(mappedBy = "reviewedBook")
    @JsonIgnore
    private List<BookReview> bookReviews;
}
