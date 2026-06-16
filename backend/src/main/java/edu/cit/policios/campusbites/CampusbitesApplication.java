package edu.cit.policios.campusbites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CampusbitesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusbitesApplication.class, args);
    }
}
