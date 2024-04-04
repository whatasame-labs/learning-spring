package com.github.whatasame.thymeleaf;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/meals")
    public String index(final Model model) {
        model.addAttribute("breakfast", "hamburger");
        model.addAttribute("launch", "pizza");
        model.addAttribute("dinner", "chicken");

        return "thymeleaf/meals";
    }

    @GetMapping("/library")
    public String library(final Model model) {
        final List<Book> books = List.of(
                new Book("The Great Gatsby", "F. Scott Fitzgerald"),
                new Book("To Kill a Mockingbird", "Harper Lee"),
                new Book("1984", "George Orwell"));

        model.addAttribute("books", books);

        return "thymeleaf/library";
    }
}
