package com.example.sqlitelevelnext;

import java.util.ArrayList;

public class UserData {

    private String name;
    private String phone;
    private String email;
    private int id;
    private byte[] imageBytes;
//    private ArrayList<UserData> datalist=new ArrayList<>();

    public UserData(String name, String phone, String email, int id,byte[] imageBytes) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.imageBytes=imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public UserData(/*ArrayList<UserData> datalist*/)
    {
//        this.datalist.addAll(datalist);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
