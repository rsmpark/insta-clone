package com.clone.insta.instagraph.payload.messaging;

import com.clone.insta.instagraph.messaging.UserEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventPayload {
    private String id;
    private String username;
    private String email;
    private String displayName;
    private String profilePictureUrl;
    private String oldProfilePicUrl;
    private UserEventType eventType;
}