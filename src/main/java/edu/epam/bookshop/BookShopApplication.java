package edu.epam.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class BookShopApplication {

    public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
        String date = "2019-12-11";
        LocalDate.parse(date);
    }
}
