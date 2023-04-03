package com.clone.insta.instafeed.service;

import com.clone.insta.instafeed.client.PostServiceClient;
import com.clone.insta.instafeed.config.JwtConfig;
import com.clone.insta.instafeed.exception.UnableToGetPostsException;
import com.clone.insta.instafeed.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    @Autowired
    private PostServiceClient postServiceClient;
    @Autowired
    private JwtConfig jwtConfig;

    public List<Post> findPostsIn(String token, List<String> ids) {
        log.info("Finding posts for ids {}", ids);

        ResponseEntity<List<Post>> response =
                postServiceClient.findPostsByIdIn(jwtConfig.getPrefix() + token, ids);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UnableToGetPostsException(
                    String.format("Unable to get posts for ids", ids));
        }
    }
}