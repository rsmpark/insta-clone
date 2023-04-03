package com.clone.insta.instafeed.service;


import com.clone.insta.instafeed.client.GraphServiceClient;
import com.clone.insta.instafeed.config.JwtConfig;
import com.clone.insta.instafeed.constants.PageConstants;
import com.clone.insta.instafeed.exception.UnableToGetFollowersException;
import com.clone.insta.instafeed.model.Post;
import com.clone.insta.instafeed.model.User;
import com.clone.insta.instafeed.model.UserFeed;
import com.clone.insta.instafeed.payload.PagedResult;
import com.clone.insta.instafeed.repository.FeedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedGeneratorService {
    @Autowired
    private AuthService tokenService;
    @Autowired
    private GraphServiceClient graphClient;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private FeedRepository feedRepository;

    public void addToFeed(Post post) {
        log.info("Adding post {} to feed for user {}", post.getUsername(), post.getId());

        String token = tokenService.getAccessToken();

        boolean isLast = false;
        int page = 0;
        int size = PageConstants.PAGE_SIZE;

        while (!isLast) {
            ResponseEntity<PagedResult<User>> response =
                    graphClient.findFollowers(jwtConfig.getPrefix() + token, post.getUsername(), page, size);

            if (response.getStatusCode().is2xxSuccessful()) {
                PagedResult<User> result = response.getBody();

                log.info("Found {} followers for user {}, page {}",
                        result.getTotalElements(), post.getUsername(), page);

                result.getContent()
                        .stream()
                        .map(user -> convertTo(user, post))
                        .forEach(feedRepository::insert);

                isLast = result.isLast();
                page++;

            } else {
                String message = String.format("Unable to get followers for user %s", post.getUsername());
                log.warn(message);

                throw new UnableToGetFollowersException(message);
            }
        }
    }

    private UserFeed convertTo(User user, Post post) {
        return UserFeed
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}