package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Publisher;
import edu.epam.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_PUBLISHERS_BY_BOOK_ID_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.PUBLISHER_EXISTS_BY_NAME_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_ALL_PUBLISHERS_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_PUBLISHERS_BY_KEYWORD_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_PUBLISHER_BY_NAME_URN;
import static edu.epam.bookshop.controller.constant.GetMappingURN.FIND_PUBLISHERS_BY_PAGE_URN;

import static edu.epam.bookshop.controller.constant.PostMappingURN.ADD_PUBLISHER_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.UPDATE_PUBLISHER_INFO_URN;
import static edu.epam.bookshop.controller.constant.PostMappingURN.DELETE_PUBLISHER_BY_ID;

@AllArgsConstructor
@RestController
public class PublisherController {

    private final BookService bookService;

    @PostMapping(ADD_PUBLISHER_URN)
    public ResponseEntity<Void> insertPublisher(Publisher newPublisher,
                                                MultipartFile publisherImage) {
        bookService.addPublisher(newPublisher, publisherImage);
        return ResponseEntity.ok().build();
    }

    @PostMapping(UPDATE_PUBLISHER_INFO_URN)
    public ResponseEntity<Void> changePublisherInfo(Publisher editedPublisher,
                                                    MultipartFile updatedPublisherImage) {
        bookService.updatePublisherInfo(editedPublisher, updatedPublisherImage);
        return ResponseEntity.ok().build();
    }

    @PostMapping(DELETE_PUBLISHER_BY_ID)
    public ResponseEntity<Void> removePublisherById(
            @RequestParam Long publisherId) {
        bookService.deletePublisherById(publisherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(PUBLISHER_EXISTS_BY_NAME_URN)
    public ResponseEntity<Boolean> checkIfPublisherExistsByName(
            @RequestParam String publisherName) {
        boolean publisherExistsByName = bookService.isPublisherExistsByName(publisherName);
        return ResponseEntity.ok(publisherExistsByName);
    }

    @GetMapping(FIND_PUBLISHER_BY_NAME_URN)
    public ResponseEntity<Publisher> getPublisherByName(
            @RequestParam String publisherName) {
        Publisher foundPublisherByName = bookService.findPublisherInfoByName(publisherName);
        return ResponseEntity.ok(foundPublisherByName);
    }

    @GetMapping(FIND_PUBLISHERS_BY_KEYWORD_URN)
    public ResponseEntity<List<Publisher>> getPublishersByKeyWord(
            @RequestParam String keyWord) {
        List<Publisher> publishersByKeyword = bookService.findPublishersByKeyword(keyWord);
        return ResponseEntity.ok(publishersByKeyword);
    }

    @GetMapping(FIND_PUBLISHERS_BY_BOOK_ID_URN)
    public ResponseEntity<List<Publisher>> getPublishersByBookId(@RequestParam Long bookId) {
        List<Publisher> publishersByBookId = bookService.findPublishersByBookId(bookId);
        return ResponseEntity.ok(publishersByBookId);
    }

    @GetMapping(FIND_PUBLISHERS_BY_PAGE_URN)
    public ResponseEntity<Page<Publisher>> getPublishersByPage(
            @RequestParam Integer page) {
        Page<Publisher> publishersByPage = bookService.findPublishersByPage(page);
        return ResponseEntity.ok(publishersByPage);
    }

    @GetMapping(FIND_ALL_PUBLISHERS_URN)
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        List<Publisher> allPublishers = bookService.findAllPublishers();
        return ResponseEntity.ok(allPublishers);
    }
}
