package com.clone.insta.instapost.repository;

import com.clone.insta.instapost.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUsernameOrderByCreatedAtDesc(String username);
    List<Post> findByIdInOrderByCreatedAtDesc(List<String> ids);
}
