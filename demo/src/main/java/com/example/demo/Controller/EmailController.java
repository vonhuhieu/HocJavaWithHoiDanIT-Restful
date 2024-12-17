package com.example.demo.Controller;

import com.example.demo.Service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
//    @Scheduled(cron = "*/60 * * * * *")
    public String sendSimpleEmail() {
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("vonhuhieu2003@gmail.com", "test send email", "<h1><b>hello</b></h1>", false, true);
        this.emailService.sendEmailFromTemplateSync("vonhuhieu2003@gmail.com", "test send email", "job");
        return "ok";
    }
}

