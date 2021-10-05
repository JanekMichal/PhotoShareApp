package com.janek.photoShareApp.models;

import javax.persistence.*;

@Entity
@Table(name = "image_table")
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
    @Column(name = "picByte", length = 1000)
    private byte[] picByte;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
