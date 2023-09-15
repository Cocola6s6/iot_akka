package com.akka.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class IotAkkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotAkkaApplication.class, args);
    }
}
