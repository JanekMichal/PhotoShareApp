package com.janek.photoShareApp.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String description;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long photoId;

    public Comment(String description, Long ownerId, Long photoId) {
        this.description = description;
        this.ownerId = ownerId;
        this.photoId = photoId;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long createdBy) {
        this.ownerId = createdBy;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
}
