package com.clone.insta.instapost.service;

import com.clone.insta.instapost.exception.NotAllowedException;
import com.clone.insta.instapost.exception.ResourceNotFoundException;
import com.clone.insta.instapost.model.Post;
import com.clone.insta.instapost.payload.request.PostRequest;
import com.clone.insta.instapost.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post createPost(PostRequest postRequest) {
        log.info("Creating post image {}", postRequest.getImageUrl());

        Post post = new Post(postRequest.getImageUrl(), postRequest.getCaption());

        post = postRepository.save(post);
        // TODO: Add Kafka messaging

        log.info("Post {} is saved successfully for user {}",
                post.getId(), post.getUsername());

        return post;
    }

    public void deletePost(String postId, String username) {
        log.info("Deleting post {}", postId);

        postRepository
                .findById(postId)
                .map(post -> {
                    if (!post.getUsername().equals(username)) {
                        log.warn("User {} is not allowed to delete post id {}", username, postId);
                        throw new NotAllowedException(username, "post id " + postId, "delete");
                    }

                    postRepository.delete(post);
                    // TODO: Add Kafka messaging
                    return post;
                })
                .orElseThrow(() -> {
                    log.warn("Post not found id {}", postId);
                    return new ResourceNotFoundException(postId);
                });
    }

    public List<Post> postsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public List<Post> postsByIdIn(List<String> ids) {
        return postRepository.findByIdInOrderByCreatedAtDesc(ids);
    }
}