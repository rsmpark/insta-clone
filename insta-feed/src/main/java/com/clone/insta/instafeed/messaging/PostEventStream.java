package com.clone.insta.instafeed.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PostEventStream {
    String INPUT = "instaPostChanged";

    @Input(INPUT)
    SubscribableChannel instaPostChanged();
}