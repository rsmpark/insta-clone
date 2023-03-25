package com.clone.insta.instapost.payload.request;

import lombok.Data;

@Data
public class PostRequest {
    private String imageUrl;
    private String caption;
}
