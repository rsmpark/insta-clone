package com.clone.insta.instagraph.service;

import com.clone.insta.instagraph.exception.UsernameAlreadyExistsException;
import com.clone.insta.instagraph.exception.UsernameNotExistsException;
import com.clone.insta.instagraph.model.Friendship;
import com.clone.insta.instagraph.model.NodeDegree;
import com.clone.insta.instagraph.model.User;
import com.clone.insta.instagraph.payload.PagedResult;
import com.clone.insta.instagraph.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            String message = String.format("Username %s already exists", user.getUsername());
            log.warn(message);

            throw new UsernameAlreadyExistsException(message);
        }

        User saveUser = userRepository.save(user);

        log.info("User {} save successfully", saveUser.getUsername());

        return saveUser;
    }

    public User updateUser(User user) {
        return userRepository
                .findByUsername(user.getUsername())
                .map(savedUser -> {
                    savedUser.setName(user.getName());
                    savedUser.setUsername(user.getUsername());
                    savedUser.setProfilePic(user.getProfilePic());

                    savedUser = userRepository.save(savedUser);
                    log.info("User {} updated successfully", savedUser.getUsername());

                    return savedUser;
                })
                .orElseThrow(() -> new UsernameNotExistsException(
                        String.format("User %s not exists", user.getUsername())));
    }

    @Transactional
    public User follow(User follower, User following) {
        log.info("User {} will follow {}",
                follower.getUsername(), following.getUsername());

        User savedFollower = userRepository
                .findByUserId(follower.getUserId())
                .orElseGet(() -> {
                    log.info("User {} not exists, creating it", follower.getUsername());
                    return this.addUser(follower);
                });

        User savedFollowing = userRepository
                .findByUserId(following.getUserId())
                .orElseGet(() -> {
                    log.info("User {} not exits, creating it", following.getUsername());
                    return this.addUser(following);
                });

        if(savedFollower.getFriendships() == null) {
            savedFollower.setFriendships(new HashSet<>());
        }

        savedFollower
                .getFriendships()
                .add(Friendship.builder()
                        .startNode(savedFollower)
                        .endNode(savedFollowing)
                        .build());

        return userRepository.save(savedFollower);
    }

    public NodeDegree findNodeDegree(String username) {
        log.info("Fetching degree for user {}", username);

        long out = userRepository.findOutDegree(username);
        long in = userRepository.findInDegree(username);

        log.info("Found {} outdegree and {} indegree for user {}", out, in, username);

        return NodeDegree
                .builder()
                .outDegree(out)
                .inDegree(in)
                .build();
    }

    public boolean isFollowing(String userA, String userb) {
        return userRepository.isFollowing(userA, userb);
    }

    public List<User> findFollowers(String username) {
        List<User> followers = userRepository.findFollowers(username);
        log.info("Found {} followers for user {}", followers.size(), username);

        return followers;
    }

    public PagedResult<User> findPaginatedFollowers(String username,int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> followers = userRepository.findFollowers(username, pageable);
        log.info("Found {} followers for user {}", followers.getTotalElements(), username);

        return buildPagedResult(followers);
    }

    public List<User> findFollowing(String username) {
        List<User> following = userRepository.findFollowing(username);
        log.info("Found {} that user {} is following", following.size(), username);

        return following;
    }

    private PagedResult<User> buildPagedResult(Page<User> page){
        return PagedResult
                .<User>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getPageable().getPageNumber())
                .size(page.getSize())
                .last(page.isLast())
                .build();
    }
}