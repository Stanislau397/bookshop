package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static edu.epam.bookshop.entity.constant.TableColumn.GENRE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.GENRE_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.LANGUAGE_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.LOCALIZED_GENRE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.TITLE;
import static edu.epam.bookshop.entity.constant.TableName.LOCALIZED_GENRES;

@Entity
@Table(name = LOCALIZED_GENRES)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocalizedGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = LOCALIZED_GENRE_ID,
            nullable = false,
            unique = true)
    private Long localizedGenreId;

    @Column(name = TITLE,
            unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = GENRE_ID_FK, referencedColumnName = GENRE_ID,
            nullable = false)
    @JsonIgnoreProperties("localizedGenres")
    private Genre genre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = LANGUAGE_ID_FK)
    private Language language;
}
