package com.example.mwojcik.retrofitone;

/***
 * {"id":1,"first_name":"George","last_name":"Bluth","avatar":"https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg"}
 */
public class ModelExample {
    private int id;
    private String name;
    private String lastName;
    private String avatarURL;

    public ModelExample(int id, String name, String lastName, String avatarURL) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.avatarURL = avatarURL;
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
        return "id: " + getId() + ", name: " + getName() + ", lastName: " + getLastName() + ", avatar: " + getAvatarURL();
    }
}
