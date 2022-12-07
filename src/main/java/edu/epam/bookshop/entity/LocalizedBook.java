package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.epam.bookshop.annotation.ValidateBookDescription;
import edu.epam.bookshop.annotation.ValidateBookTitle;
import edu.epam.bookshop.annotation.ValidateImage;
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

import java.util.Objects;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.DESCRIPTION;
import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;
import static edu.epam.bookshop.entity.constant.TableColumn.LANGUAGE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.LANGUAGE_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.LOCALIZED_BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.TITLE;
import static edu.epam.bookshop.entity.constant.TableName.LOCALIZED_BOOKS;

@Entity
@Table(name = LOCALIZED_BOOKS)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocalizedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = LOCALIZED_BOOK_ID,
            unique = true)
    private Long localizedBookId;

    @Column(name = TITLE)
    @ValidateBookTitle
    private String title;

    @Column(name = DESCRIPTION)
    @ValidateBookDescription
    private String description;

    @Column(name = IMAGE_PATH)
    @ValidateImage
    private String imagePath;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = LANGUAGE_ID_FK, referencedColumnName = LANGUAGE_ID)
    private Language language;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = BOOK_ID_FK, referencedColumnName = BOOK_ID)
    @JsonIgnoreProperties("localizedBooks")
    private Book book;
}
