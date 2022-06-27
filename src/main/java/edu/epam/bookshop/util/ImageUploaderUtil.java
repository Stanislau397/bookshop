package edu.epam.bookshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class ImageUploaderUtil {

    private ImageUploaderUtil() {

    }

    public static String save(MultipartFile image, String uploadDirectory) {
        Path uploadPath = Paths.get(uploadDirectory);
        String imageName = image.getOriginalFilename();
        try (InputStream inputStream = image.getInputStream()) {
            if (imageName != null) {
                Path filePath = uploadPath.resolve(imageName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return imageName;
    }
}
