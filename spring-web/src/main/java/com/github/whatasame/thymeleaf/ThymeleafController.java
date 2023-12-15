package com.github.whatasame.thymeleaf;

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
}
