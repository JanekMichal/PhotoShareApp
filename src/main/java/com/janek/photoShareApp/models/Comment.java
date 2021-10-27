package com.janek.photoShareApp.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
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
    @Column(name = "date")
    private Date commentDate;

    public Comment(String description, Long ownerId, Long photoId) {
        this.description = description;
        this.ownerId = ownerId;
        this.photoId = photoId;
        this.commentDate = new Date();
    }

    public Comment() {

    }
}
