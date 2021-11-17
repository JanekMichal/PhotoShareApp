package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.payload.request.FollowRequest;
import com.janek.photoShareApp.payload.response.FollowResponse;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.service.implementation.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class FollowController {

    FollowRepository followRepository;
    FollowService followService;
    UserRepository userRepository;

    @GetMapping("/get_followers/{follower_id}")
    public ResponseEntity<?> getFollowers(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowers(followerId), HttpStatus.OK);
    }

    @GetMapping("/get_followed/{follower_id}")
    public ResponseEntity<?> getFollowedUsers(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowedUsers(followerId), HttpStatus.OK);
    }

    @PostMapping("/follow_user")
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest) {
        FollowResponse followResponse = followService.followUser(followRequest.getFollowerId(), followRequest.getFollowedId());
        return new ResponseEntity<>(followResponse, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{follower_id}/unfollow_user/{followed_id}")
    public ResponseEntity<?> unfollowUser(@PathVariable("follower_id") Long followerId,
                                          @PathVariable("followed_id") Long followedId) {
        FollowResponse followResponse = followService.unfollowUser(followerId, followedId);
        return new ResponseEntity<>(followResponse, HttpStatus.OK);
    }

    @GetMapping("/{follower_id}/is_following/{followed_id}")
    public boolean isFollowing(@PathVariable("follower_id") Long followerId,
                               @PathVariable("followed_id") Long followedId) {
        FollowResponse followResponse = followService.isFollowing(followerId, followedId);
        return followResponse.isFollowed();
    }

    @GetMapping("/get_followers_count/{follower_id}")
    public ResponseEntity<?> getFollowersCount(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowersCount(followerId), HttpStatus.OK);
    }

    @GetMapping("/get_following_count/{follower_id}")
    public ResponseEntity<?> getFollowingCount(@PathVariable("follower_id") Long followerId) {
        return new ResponseEntity<>(followService.getFollowingCount(followerId), HttpStatus.OK);
    }
}
