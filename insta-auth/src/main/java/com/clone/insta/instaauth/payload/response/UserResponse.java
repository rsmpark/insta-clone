package com.clone.insta.instaauth.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String name;
    private String profilePicture;
}
