package com.clone.insta.instaauth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    private String displayName;
    private String profilePictureUrl;
    private Date dob;
    private Set<Address> addresses;
}