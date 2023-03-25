package com.clone.insta.instapost.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface PostEventStream {

    String OUTPUT = "instaPostChanged";

    @Output(OUTPUT)
    MessageChannel momentsPostChanged();
}
