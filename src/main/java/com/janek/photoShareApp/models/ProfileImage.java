package com.janek.photoShareApp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "profile_image")
public class ProfileImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "pic_byte", length = 1000)
    private byte[] picByte;

    public ProfileImage(String name, String type, Long ownerId, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.picByte = picByte;
    }
}
