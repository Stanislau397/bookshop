package edu.epam.bookshop.entity;

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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

import static edu.epam.bookshop.entity.constant.TableColumn.AUTHOR_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;
import static edu.epam.bookshop.entity.constant.TableColumn.FIRST_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.LAST_NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.BIOGRAPHY;
import static edu.epam.bookshop.entity.constant.TableColumn.BIRTH_DATE;

import static edu.epam.bookshop.entity.constant.TableName.AUTHORS;

@Entity
@Table(name = AUTHORS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
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

    @ManyToMany(
            mappedBy = "authors",
            targetEntity = Book.class,
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    private List<Book> books;
}
