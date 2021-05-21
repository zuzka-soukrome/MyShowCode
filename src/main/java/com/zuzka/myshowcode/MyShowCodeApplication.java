package com.zuzka.myshowcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class MyShowCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShowCodeApplication.class, args);
    }

}
