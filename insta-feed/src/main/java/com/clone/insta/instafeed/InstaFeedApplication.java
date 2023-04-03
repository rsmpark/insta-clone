package com.clone.insta.instafeed;

import com.clone.insta.instafeed.messaging.PostEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(PostEventStream.class)
@EnableFeignClients
public class InstaFeedApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstaFeedApplication.class, args);
    }

}
