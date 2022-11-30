package edu.epam.bookshop.service;

import edu.epam.bookshop.entity.Language;

import java.util.List;

public interface LanguageService {

    boolean languageExistsByName(String languageName);

    Language findLanguageByName(String name);

    List<Language> findAllLanguages();

    Language findLanguageByLocalizedBookTitle(String title);
}
