package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDate;

import static edu.epam.bookshop.entity.constant.PropertyId.REVIEW_ID_PROPERTY;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_REVIEW_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.PUBLISH_DATE;
import static edu.epam.bookshop.entity.constant.TableColumn.REVIEW_TEXT;
import static edu.epam.bookshop.entity.constant.TableColumn.SCORE;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.USER_ID_FK;
import static edu.epam.bookshop.entity.constant.TableName.BOOK_REVIEWS;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = REVIEW_ID_PROPERTY)
@Table(name = BOOK_REVIEWS)
@Entity
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BOOK_REVIEW_ID)
    private Long bookReviewId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(
            name = PUBLISH_DATE,
            nullable = false
    )
    private LocalDate publishDate;

    @Column(
            name = REVIEW_TEXT,
            nullable = false
    )
    private String reviewText;

    @Column(
            name = SCORE,
            nullable = false
    )
    private Integer score;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = BOOK_ID_FK,
            referencedColumnName = BOOK_ID
    )
    private Book reviewedBook;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = USER_ID_FK,
            referencedColumnName = USER_ID
    )
    private User user;
}
