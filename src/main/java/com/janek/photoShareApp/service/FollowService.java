package com.janek.photoShareApp.service.implementation;

import com.janek.photoShareApp.models.Follow;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.payload.response.FollowResponse;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;


    public FollowResponse followUser(Long currentUserId, Long followedUserId) {
        User follower = userRepository.findUserById(currentUserId);
        User following = userRepository.findUserById(followedUserId);

        Follow followObject = new Follow();
        followObject.setFollower(follower);
        followObject.setFollowing(following);
        followRepository.save(followObject);

        return new FollowResponse(true);
    }


    public FollowResponse unfollowUser(Long currentUserId, Long followedUserId) {
        followRepository.deleteFollowByFollowerIdAndFollowingId(currentUserId, followedUserId);
        return new FollowResponse(true);
    }


    public List<User> getFollowedUsers(Long followerId) {
        List<Follow> followedUsers = followRepository.findAllByFollowerId(followerId);
        List<User> userList = new ArrayList<>();
        for (Follow follow : followedUsers) {
            userList.add(userRepository.findUserById(follow.getFollowing().getId()));
        }
        return userList;
    }


    public List<User> getFollowers(Long followerId) {
        List<Follow> followers = followRepository.findAllByFollowingId(followerId);
        List<User> userList = new ArrayList<>();
        for (Follow follower : followers) {
            userList.add(userRepository.findUserById(follower.getFollower().getId()));
        }
        return userList;
    }


    public int getFollowersCount(Long followerId) {
        return followRepository.countByFollowingId(followerId);
    }


    public int getFollowingCount(Long followerId) {
        return followRepository.countByFollowerId(followerId);
    }


    public FollowResponse isFollowing(Long currentUserId, Long followedUserId) {
        Optional<Follow> follow = followRepository.findFollowByFollowerIdAndFollowingId(currentUserId, followedUserId);
        if (follow.isPresent()) {
            return new FollowResponse(true);
        }
        return new FollowResponse(false);
    }
}
