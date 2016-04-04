package com.revinate.sendgrid.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:applicationContext.xml"})
public class SendGridEventReceiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendGridEventReceiverApplication.class, args);
    }
}
