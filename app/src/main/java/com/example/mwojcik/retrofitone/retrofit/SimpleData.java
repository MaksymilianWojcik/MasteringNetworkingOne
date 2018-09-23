package com.example.mwojcik.retrofitone.retrofit;

import com.google.gson.annotations.SerializedName;

public class SimpleData {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("avatar")
    private String avatarURL;

    public SimpleData(int id, String firstName, String lastName, String avatarURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarURL = avatarURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }


    @Override
    public String toString() {
        return "id: " + getId() + ", first name: " + getFirstName() + ", last name: " + getLastName()
                + ", avatar: " + getAvatarURL();
    }
}
