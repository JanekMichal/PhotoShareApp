package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.payload.response.FollowResponse;
import com.janek.photoShareApp.payload.request.FollowRequest;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.service.implementation.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class FollowController {

    @Autowired
    FollowRepository followRepository;

    @Autowired
    FollowService followService;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/get_followers/{follower_id}")
    public ResponseEntity<?> getFollowers(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowers(followerId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/get_followed/{follower_id}")
    public ResponseEntity<?> getFollowedUsers(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowedUsers(followerId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/follow_user")
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest) {
        FollowResponse followResponse = followService.followUser(followRequest.getFollowerId(), followRequest.getFollowedId());
        return new ResponseEntity<>(followResponse, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{follower_id}/unfollow_user/{followed_id}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> unfollowUser(@PathVariable("follower_id") Long followerId,
                                          @PathVariable("followed_id") Long followedId) {
        FollowResponse followResponse = followService.unfollowUser(followerId, followedId);
        return new ResponseEntity<>(followResponse, HttpStatus.OK);
    }

    @GetMapping("/{follower_id}/is_following/{followed_id}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public boolean isFollowing(@PathVariable("follower_id") Long followerId,
                               @PathVariable("followed_id") Long followedId) {
        FollowResponse followResponse = followService.isFollowing(followerId, followedId);
        return followResponse.isFollowed();
    }

    @GetMapping("/get_followers_count/{follower_id}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getFollowersCount(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowersCount(followerId), HttpStatus.OK);
    }

    @GetMapping("/get_following_count/{follower_id}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getFollowingCount(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowingCount(followerId), HttpStatus.OK);
    }
}
