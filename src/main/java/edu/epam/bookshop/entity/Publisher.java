package edu.epam.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

import static edu.epam.bookshop.entity.constant.TableName.PUBLISHERS;

import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISHER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.DESCRIPTION;
import static edu.epam.bookshop.entity.constant.TableColumn.NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;

@Entity
@Table(name = PUBLISHERS)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
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

    @ManyToMany(
            mappedBy = "publishers",
            targetEntity = Book.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    private List<Book> publishedBooks;
}
