package com.clone.insta.instapost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
    private Boolean success;
    private String message;
}
