package edu.epam.bookshop.entity;

import lombok.*;

import javax.persistence.*;

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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GENRE_ID,
            unique = true,
            nullable = false)
    private Long genreId;

    @Column(name = TITLE,
            nullable = false,
            unique = true)
    private String title;
}
