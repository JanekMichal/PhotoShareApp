package com.janek.photoShareApp.payload.response;

public class FollowResponse {
    private boolean followed;

    public FollowResponse(boolean followed) {
        this.followed = followed;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
