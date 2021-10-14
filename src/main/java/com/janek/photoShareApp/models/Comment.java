package com.janek.photoShareApp.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date commentDate;

    public Comment(String description, Long ownerId, Long photoId) {
        this.description = description;
        this.ownerId = ownerId;
        this.photoId = photoId;
        this.commentDate = new Date();
    }

    public Comment() {

    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
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
