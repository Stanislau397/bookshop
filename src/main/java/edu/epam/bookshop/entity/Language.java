package edu.epam.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static edu.epam.bookshop.entity.constant.TableColumn.IMAGE_PATH;
import static edu.epam.bookshop.entity.constant.TableColumn.LANGUAGE_ID;
import static edu.epam.bookshop.entity.constant.TableColumn.SHORT_NAME;
import static edu.epam.bookshop.entity.constant.TableName.LANGUAGES;

@Entity
@Table(name = LANGUAGES)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = LANGUAGE_ID,
            nullable = false,
            unique = true)
    private Long languageId;

    @Column(name = SHORT_NAME,
            nullable = false,
            unique = true)
    private String name;

    @Column(name = IMAGE_PATH)
    private String imagePath;
}
