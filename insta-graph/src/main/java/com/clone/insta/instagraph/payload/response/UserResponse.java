package com.clone.insta.instagraph.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Boolean success;
    private String message;
}