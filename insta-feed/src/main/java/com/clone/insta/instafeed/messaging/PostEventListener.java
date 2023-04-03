package com.clone.insta.instafeed.messaging;

import com.clone.insta.instafeed.model.Post;
import com.clone.insta.instafeed.payload.messaging.PostEventPayload;
import com.clone.insta.instafeed.payload.messaging.PostEventType;
import com.clone.insta.instafeed.service.FeedGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.annotation.StreamListener;

@Component
@Slf4j
public class PostEventListener {
    private FeedGeneratorService feedGeneratorService;

    public PostEventListener(FeedGeneratorService feedService) {
        this.feedGeneratorService = feedService;
    }

    @StreamListener(PostEventStream.INPUT)
    public void onMessage(Message<PostEventPayload> message) {
        PostEventType eventType = message.getPayload().getEventType();

        log.info("Received message to process post {} for user {} eventType {}",
                message.getPayload().getId(),
                message.getPayload().getUsername(),
                eventType.name());

        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        switch (eventType) {
            case CREATED:
                feedGeneratorService.addToFeed(convertTo(message.getPayload()));
                break;
            case DELETED:
                break;
        }

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

    private Post convertTo(PostEventPayload payload) {
        return Post.builder()
                .id(payload.getId())
                .createdAt(payload.getCreatedAt())
                .username(payload.getUsername())
                .build();
    }
}