package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Genre;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_GENRES_BY_BOOK_ID_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_GENRES_BY_KEYWORD_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.GENRE_EXISTS_BY_TITLE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_GENRES_BY_PAGE_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_ALL_GENRES_URN;

import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_GENRE_TO_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_GENRE_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.REMOVE_GENRE_FROM_BOOK_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_GENRE_TITLE_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.DELETE_GENRE_BY_ID_URN;

@AllArgsConstructor
@RestController
public class GenreController {

    private final BookService bookService;

    @PostMapping(ADD_GENRE_URN)
    public ResponseEntity<Void> insertGenre(Genre genre) {
        bookService.addGenre(genre);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ADD_GENRE_TO_BOOK_URN)
    public ResponseEntity<Void> addGenreToBook(Long genreId, Long bookId) {
        bookService.addGenreToBook(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(REMOVE_GENRE_FROM_BOOK_URN)
    public ResponseEntity<Void> removeGenreFromBook(Long genreId, Long bookId) {
        bookService.removeGenreFromBook(genreId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_GENRE_TITLE_URN)
    public ResponseEntity<Void> changeGenreTitle(Genre genre) {
        bookService.updateGenreTitle(genre);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_GENRE_BY_ID_URN)
    public ResponseEntity<Void> removeGenreById(Long genreId) {
        bookService.deleteGenreById(genreId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(GENRE_EXISTS_BY_TITLE_URN)
    public ResponseEntity<Boolean> checkIfGenreExistsByTitle(String genreTitle) {
        boolean genreExistsByTitle = bookService.isGenreExistsByTitle(genreTitle);
        return ResponseEntity.ok(genreExistsByTitle);
    }

    @GetMapping(FIND_GENRES_BY_KEYWORD_URN)
    public ResponseEntity<List<Genre>> getGenresByKeyWord(String keyWord) {
        List<Genre> genresByKeyWord = bookService.findGenresByKeyword(keyWord);
        return ResponseEntity.ok(genresByKeyWord);
    }

    @GetMapping(FIND_GENRES_BY_PAGE_URN)
    public ResponseEntity<Page<Genre>> getGenresByPage(Integer page) {
        Page<Genre> genresByPage = bookService.findGenresByPage(page);
        return ResponseEntity.ok(genresByPage);
    }

    @GetMapping(FIND_ALL_GENRES_URN)
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> allGenres = bookService.findAllGenres();
        return ResponseEntity.ok(allGenres);
    }

    @GetMapping(FIND_GENRES_BY_BOOK_ID_URN)
    public ResponseEntity<List<Genre>> displayGenresByBookId(Long bookId) {
        List<Genre> genresByBookId = bookService.findGenresByBookId(bookId);
        return ResponseEntity.ok(genresByBookId);
    }
}
