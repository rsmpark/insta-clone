package com.clone.insta.instamedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableEurekaServer
@EnableMongoAuditing
public class InstaMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstaMediaApplication.class, args);
    }

}
