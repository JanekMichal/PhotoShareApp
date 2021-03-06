package com.janek.photoShareApp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FollowRequest {
    @NotBlank
    private Long followerId;

    @NotBlank
    private Long followedId;


}
