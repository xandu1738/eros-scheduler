package com.ceres.erostabler.utils;

import com.alibaba.fastjson2.JSONObject;
import com.ceres.erostabler.dto.Developer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendTemplateMail(Developer receiver, String subject, Map<String, List<Developer>> schedule, JSONObject randomQuote,String template) throws MessagingException, UnsupportedEncodingException {
        Context context = new Context();

        context.setVariable("quote", randomQuote.getString("quote"));
        context.setVariable("author", randomQuote.getString("author"));
        context.setVariable("year", LocalDate.now().getYear());

        context.setVariable("name", receiver.name());
        context.setVariable("schedule", schedule);

        String process = templateEngine.process(template, context);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setFrom("ceres1738@gmail.com", "Ceres Support");
        helper.setText(process, true);
        helper.setTo(receiver.email());

        try {
            emailSender.send(mimeMessage);
            log.info("Sending email to {}", receiver.email());
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }
}
