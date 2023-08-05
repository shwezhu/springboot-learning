package com.david.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.Arrays;

import com.david.bean.Cat;
 
@Controller
public class MainController {
    ArrayList<Cat> cats = new ArrayList<>(Arrays.asList(
            new Cat("Bella", 1.2),
            new Cat("Max", 0.5),
            new Cat("Milo", 2)
    ));

    @GetMapping("/")
    public String home(
            @RequestParam(name = "name", required = false, defaultValue = "")
            String name, Model model) {
        model.addAttribute("title", "hello little kittens");
        if (name.equals("")) {
            // add "cats" attribute to the html file you'll return
            model.addAttribute("cats", cats);
        } else {
            for (Cat cat : cats) {
                if (cat.name().equals(name)) {
                    model.addAttribute("cat", cat);
                    return "index"; // return index.html
                }
            }
            model.addAttribute("cat", null);
            model.addAttribute("name", name);
        }
        return "index";  // return index.html
    }
}
