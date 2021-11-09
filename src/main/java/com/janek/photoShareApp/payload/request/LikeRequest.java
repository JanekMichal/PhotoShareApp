package com.janek.photoShareApp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LikeRequest {
    @NotBlank
    private Long imageId;

    @NotBlank
    private Long ownerId;


}
