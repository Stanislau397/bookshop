package edu.epam.bookshop.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;

import static edu.epam.bookshop.entity.constant.TableName.PUBLISHER_BOOKS;
import static edu.epam.bookshop.entity.constant.TableName.PUBLISHERS;

import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISHER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISHER_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.DESCRIPTION;
import static edu.epam.bookshop.entity.constant.TableColumn.NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;

@Entity
@Table(name = PUBLISHERS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PUBLISHER_ID,
            nullable = false,
            unique = true)
    private Long publisherId;

    @Column(name = NAME,
            nullable = false,
            unique = true)
    private String name;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = IMAGE_PATH)
    private String imagePath;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = PUBLISHER_BOOKS,
            joinColumns = @JoinColumn(name = PUBLISHER_ID_FK),
            inverseJoinColumns = @JoinColumn(name = BOOK_ID_FK))
    private List<Book> publishedBooks;
}
