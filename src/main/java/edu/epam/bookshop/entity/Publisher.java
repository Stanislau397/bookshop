package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

import static edu.epam.bookshop.entity.constant.PropertyId.PUBLISHER_ID_PROPERTY;
import static edu.epam.bookshop.entity.constant.TableName.PUBLISHERS;

import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISHER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.DESCRIPTION;
import static edu.epam.bookshop.entity.constant.TableColumn.NAME;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = PUBLISHERS)
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
            cascade = CascadeType.MERGE
    )
    @JsonIgnoreProperties("publishers")
    private List<Book> publishedBooks;
}
