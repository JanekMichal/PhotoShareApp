package com.janek.photoShareApp.payload.request;

import javax.validation.constraints.NotBlank;

public class FollowRequest {
    @NotBlank
    private Long followerId;

    @NotBlank
    private Long followedId;

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }
}
