package com.example.demo.Service;

import com.example.demo.Domain.DTO.Response.Email.ResponseEmailJobDTO;
import com.example.demo.Domain.Job;
import com.example.demo.Domain.Skill;
import com.example.demo.Repository.JobRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final JobRepository jobRepository;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, JobRepository jobRepository) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.jobRepository = jobRepository;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("vonhuhieu2003@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        this.mailSender.send(msg);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    @Async
    @Transactional
    public void sendEmailFromTemplateSync(String to, String subject, String templateName) {
        Context context = new Context();
        List<Job> arrJob = this.jobRepository.findAll();
        List<ResponseEmailJobDTO> arrJobDTO = new ArrayList<>();
        for (Job job : arrJob){
            ResponseEmailJobDTO responseEmailJobDTO = new ResponseEmailJobDTO();
            responseEmailJobDTO.setName(job.getName());
            responseEmailJobDTO.setSalary(job.getSalary());
            ResponseEmailJobDTO.CompanyEmail companyEmail = new ResponseEmailJobDTO.CompanyEmail();
            companyEmail.setName(job.getCompany().getName());
            responseEmailJobDTO.setCompany(companyEmail);
            List<ResponseEmailJobDTO.SkillEmail> arrSkillDTO = new ArrayList<>();
            for (Skill skill : job.getSkills()){
                ResponseEmailJobDTO.SkillEmail skillEmail = new ResponseEmailJobDTO.SkillEmail();
                skillEmail.setName(skill.getName());
                arrSkillDTO.add(skillEmail);
            }
            responseEmailJobDTO.setSkills(arrSkillDTO);
            arrJobDTO.add(responseEmailJobDTO);
        }
        String name = "HIEU";
        context.setVariable("name", name);
        context.setVariable("jobs", arrJob);

        String content = this.springTemplateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }
}

