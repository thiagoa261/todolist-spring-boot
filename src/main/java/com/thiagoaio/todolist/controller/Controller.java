package com.thiagoaio.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
 
    @GetMapping("/ok")
    public String ApiOnline() {
        return "API online!";
    }
}
