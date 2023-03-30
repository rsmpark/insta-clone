package com.clone.insta.instagraph.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface UserEventStream {
    String INPUT = "instaUserChanged";

    @Input(INPUT)
    MessageChannel instaUserChanged();
}