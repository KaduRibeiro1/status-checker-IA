package com.stefanini.status_checker.Controller;

import com.stefanini.status_checker.Service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/check-status")
    public String checkStatus(@RequestParam String url) {
        return statusService.getStatus(url);
    }
}
