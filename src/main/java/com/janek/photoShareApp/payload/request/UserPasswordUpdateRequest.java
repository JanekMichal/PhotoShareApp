package com.janek.photoShareApp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserPasswordUpdateRequest {

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
