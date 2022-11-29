package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

import static edu.epam.bookshop.entity.constant.PropertyId.GENRE_ID_PROPERTY;

import static edu.epam.bookshop.entity.constant.TableColumn.GENRE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.TITLE;

import static edu.epam.bookshop.entity.constant.TableName.GENRES;

@Entity
@Table(name = GENRES)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = GENRE_ID_PROPERTY)
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GENRE_ID,
            unique = true,
            nullable = false)
    private Long genreId;

//    @Column(name = TITLE,
//            nullable = false,
//            unique = true)
//    private String title;

    @ManyToMany(
            mappedBy = "genres",
            targetEntity = Book.class,
            cascade = CascadeType.MERGE
    )
    private List<Book> books;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "genre_id_fk", referencedColumnName = "genre_id")
    private List<LocalizedGenre> localizedGenres;

}
