package com.example.mgutierrezplaza.bbdd;

public class Pantalla {

    private int id;
    private String name;
    private byte[] image;
    private byte[] image2;
    private byte[] image3;
    private byte[] image4;

    public Pantalla(int id, String name, byte[] image, byte[] image2, byte[] image3, byte[] image4) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public byte[] getImage3() {
        return image3;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public byte[] getImage4() {
        return image4;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }
}
