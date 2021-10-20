package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.payload.follow.FollowResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowService {
    FollowResponse followUser(Long currentUserId, Long followedUserId);

    FollowResponse unfollowUser(Long followerId, Long followedId);

    List<User> getFollowedUsers(Long followerId);

    List<User> getFollowers(Long followerId);

    int getFollowersCount(Long followerId);

    int getFollowingCount(Long followerId);

    FollowResponse isFollowing(Long currentUserId, Long followedUserId);
}
