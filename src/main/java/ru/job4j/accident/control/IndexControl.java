package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexControl {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", "Petr Arsentev");
        List<String> customers = List.of("Anton", "Victor", "Ivan", "Semyon");
        model.addAttribute("customers", customers);
        return "index";
    }
}