package com.clone.insta.instaauth;

import com.clone.insta.instaauth.messaging.UserEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableBinding(UserEventStream.class)
public class InstaAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstaAuthApplication.class, args);
    }

}
