package com.example.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingsController {
    @GetMapping("/hi")
    public String nicetomeetyou(Model model) {
        model.addAttribute("username", "HONGPARK");
        return "greetings";
    }

    @GetMapping("/bye")
    public String seeyounext(Model model){
        model.addAttribute("name", "태웅");
        return "goodbye";
    }
}
