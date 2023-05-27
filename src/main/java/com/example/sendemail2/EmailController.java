package com.example.sendemail2;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@RestController
public class EmailController {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public EmailController(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/send-email")
    public String sendEmail(Model model) throws jakarta.mail.MessagingException {
        String uniqueCode = generateUniqueCode(); // Itt történik a generálás

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("inkeko@gmail.com");
        helper.setTo("munka6502@gmail.com");
        helper.setSubject("HTML Email Test");

        Context context = new Context();
        context.setVariable("generatedCode", uniqueCode);
        String processedHtmlContent = templateEngine.process("email-content.html", context);

        helper.setText(processedHtmlContent, true);

        emailSender.send(message);

        return "Az e-mail sikeresen elküldve!";
    }


    @GetMapping("/result")
    public ModelAndView showProgramPage(@RequestParam("code") String coDe) {
        ModelAndView modelAndView = new ModelAndView("resultPage");
        modelAndView.addObject("code", coDe);
        return modelAndView;
    }

    private String generateUniqueCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
