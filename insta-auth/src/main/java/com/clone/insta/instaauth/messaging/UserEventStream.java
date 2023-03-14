package com.clone.insta.instaauth.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserEventStream {
    String OUTPUT = "instaUserChanged";

    @Output(OUTPUT)
    MessageChannel instaUserChanged();
}