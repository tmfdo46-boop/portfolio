package com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alerts")
public class AlertViewController {

    @GetMapping("/view")
    public String alertView() {
        return "alert"; // resources/templates/alert.html
    }
}
