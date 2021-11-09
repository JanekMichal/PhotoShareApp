package com.janek.photoShareApp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private long ownerId;

    private long imageId;

    public Like(long ownerId, long imageId) {
        this.ownerId = ownerId;
        this.imageId = imageId;
    }

    public Like() {
    }
}
