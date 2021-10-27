package com.janek.photoShareApp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "image_table")
@Getter
@Setter
public class Image {

    public Image() {
        super();
    }

    public Image(String name, String type, Long ownerId, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.picByte = picByte;
    }

    public Image(String name, String type, Long ownerId, String description, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.description = description;
        this.picByte = picByte;
    }

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

    @Column(name = "description")
    private String description;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "pic_byte", length = 1000)
    private byte[] picByte;
}
