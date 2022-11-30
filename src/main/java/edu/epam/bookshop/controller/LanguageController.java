package edu.epam.bookshop.controller;

import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@AllArgsConstructor
public class LanguageController {

    private LanguageService languageService;

    @GetMapping("/findLanguageByName")
    public ResponseEntity<Language> findByName(String name) {
        return ResponseEntity.ok(languageService.findLanguageByName(name));
    }

    @GetMapping("/findAllLanguages")
    public ResponseEntity<List<Language>> displayAllLanguages() {
        return ResponseEntity.ok(languageService.findAllLanguages());
    }

    @GetMapping("/displayCurrentLocale")
    public ResponseEntity<Locale> displayCurrentLocale() {
        Locale currentLocale = LocaleContextHolder
                .getLocale();
        return ResponseEntity.ok(currentLocale);
    }

    @GetMapping("/displayLanguageByLocalizedBookTitle")
    public ResponseEntity<Language> displayLanguageByLocalizedBookTitle(String title) {
        return ResponseEntity.ok(languageService.findLanguageByLocalizedBookTitle(title));
    }
}
