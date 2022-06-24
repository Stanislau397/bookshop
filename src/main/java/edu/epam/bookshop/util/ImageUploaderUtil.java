package edu.epam.bookshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
public class ImageUploaderUtil {

    private ImageUploaderUtil() {

    }

    private static final String DOT = ".";

    public static String save(MultipartFile image, String uploadDirectory) {
        Path uploadPath = Paths.get(uploadDirectory);
        String imageName = image.getOriginalFilename();
        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(imageName);
            if (Files.exists(filePath)) {
                imageName = changeFileName(imageName);
                filePath = uploadPath.resolve(imageName);
            }
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return imageName;
    }

    public static String changeFileName(String fileName) {
        int indexOfDot = fileName.indexOf(DOT);
        String fileExtension = fileName.substring(indexOfDot);
        String newFileName = UUID.randomUUID().toString();
        return newFileName.concat(fileExtension);
    }
}
