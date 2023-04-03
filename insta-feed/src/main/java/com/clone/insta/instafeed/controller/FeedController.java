package com.clone.insta.instafeed.controller;

import com.clone.insta.instafeed.model.Post;
import com.clone.insta.instafeed.payload.SlicedResult;
import com.clone.insta.instafeed.service.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class FeedController {

    @Autowired
    private FeedService feedService;

    @RequestMapping("/feed/{username}")
    public ResponseEntity<SlicedResult<Post>> getFeed(
            @PathVariable String username,
            @RequestParam(value = "ps", required = false) Optional<String> pagingState) {
        log.info("Fetching feed for user {} isFirstPage {}", username, pagingState.isEmpty());

        return ResponseEntity.ok(feedService.getUserFeed(username, pagingState));
    }
}