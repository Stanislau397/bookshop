package edu.epam.bookshop.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static edu.epam.bookshop.entity.constant.TableColumn.AUTHOR_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.FIRST_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.LAST_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.BIRTH_DATE;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;
import static edu.epam.bookshop.entity.constant.TableColumn.BIOGRAPHY;
import static edu.epam.bookshop.entity.constant.TableColumn.AUTHOR_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;

import static edu.epam.bookshop.entity.constant.TableName.AUTHORS;
import static edu.epam.bookshop.entity.constant.TableName.AUTHOR_BOOKS;

@Entity
@Table(name = AUTHORS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AUTHOR_ID)
    private Long authorId;

    @Column(name = IMAGE_PATH)
    private String imagePath;

    @Column(name = FIRST_NAME)
    private String firstName;

    @Column(name = LAST_NAME)
    private String lastName;

    @Column(name = BIOGRAPHY)
    private String biography;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = BIRTH_DATE)
    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = AUTHOR_BOOKS,
            joinColumns = @JoinColumn(name = AUTHOR_ID_FK),
            inverseJoinColumns = @JoinColumn(name = BOOK_ID_FK))
    private List<Book> books;
}
