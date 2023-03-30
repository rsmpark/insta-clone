package com.clone.insta.instagraph;

import com.clone.insta.instagraph.messaging.UserEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(UserEventStream.class)
public class InstaGraphApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstaGraphApplication.class, args);
    }

}
