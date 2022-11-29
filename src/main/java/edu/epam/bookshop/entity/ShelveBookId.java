package edu.epam.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

import static edu.epam.bookshop.entity.constant.TableColumn.BOOK_ID_FK;
import static edu.epam.bookshop.entity.constant.TableColumn.SHELVE_ID_FK;

//@Embeddable
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Setter
//@Getter
public class ShelveBookId implements Serializable {

//    private static final long serialVersionUID = -2740883442817857397L;
//    @Column(name = SHELVE_ID_FK)
//    private Long shelveIdFk;
//
//    @Column(name = BOOK_ID_FK)
//    private Long bookIdFk;
}
