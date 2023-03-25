package com.clone.insta.instapost;

import com.clone.insta.instapost.messaging.PostEventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableBinding(PostEventStream.class)
public class InstaPostApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaPostApplication.class, args);
	}

}
