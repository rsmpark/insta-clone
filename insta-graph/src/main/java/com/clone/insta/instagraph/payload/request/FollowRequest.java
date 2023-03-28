package com.clone.insta.instagraph.payload.request;

import com.clone.insta.instagraph.payload.UserPayload;
import lombok.Data;

@Data
public class FollowRequest {
    UserPayload follower;
    UserPayload following;
}