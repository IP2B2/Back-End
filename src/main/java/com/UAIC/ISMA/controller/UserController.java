package com.UAIC.ISMA.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user/test")
    public String userTest() {
        return "Hello, User!";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Hello, Admin!";
    }
}
