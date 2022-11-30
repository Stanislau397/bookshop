package edu.epam.bookshop.service.impl;

import edu.epam.bookshop.entity.Language;
import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.NothingFoundException;
import edu.epam.bookshop.repository.LanguageRepository;
import edu.epam.bookshop.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.epam.bookshop.constant.ExceptionMessage.GIVEN_LANGUAGE_DOES_NOT_EXIST;
import static edu.epam.bookshop.constant.ExceptionMessage.NOTHING_WAS_FOUND_MSG;

@Service
@Slf4j
@AllArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public boolean languageExistsByName(String languageName) {
        boolean existsByName = languageRepository.existsByName(languageName);
        if (!existsByName) {
            log.info(GIVEN_LANGUAGE_DOES_NOT_EXIST);
            throw new EntityNotFoundException(GIVEN_LANGUAGE_DOES_NOT_EXIST);
        }
        return existsByName;
    }

    @Override
    public Language findLanguageByName(String name) {
        return languageRepository.findByName(name)
                .orElseThrow(() -> {
                    log.info(GIVEN_LANGUAGE_DOES_NOT_EXIST);
                    throw new EntityNotFoundException(GIVEN_LANGUAGE_DOES_NOT_EXIST);
                });
    }

    @Override
    public List<Language> findAllLanguages() {
        List<Language> allLanguages = languageRepository.findAll();
        if (allLanguages.isEmpty()) {
            log.info(NOTHING_WAS_FOUND_MSG);
            throw new NothingFoundException(NOTHING_WAS_FOUND_MSG);
        }
        return allLanguages;
    }

    @Override
    public Language findLanguageByLocalizedBookTitle(String title) {
        return languageRepository.selectLanguageByLocalizedBookTitle(title)
                .orElseThrow();
    }
}
