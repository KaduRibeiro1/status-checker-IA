package com.stefanini.status_checker.Controller;

import com.stefanini.status_checker.Service.SeleniumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeleniumController {

    private final SeleniumService seleniumService;

    public SeleniumController(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }

    @GetMapping("/check-gemini")
    public String checkGemini() {
        return seleniumService.getGeminiStatus();
    }
}

