package com.example.glutenfree;

public class User {
    private String FirstName;
    private String LastName;
    private String Password;
    private String Email;
    private String ProfileImage;
    private String key;
    private String profileType;

    public User(String firstName, String lastName, String password, String email, String profileImage, String key, String profileType) {
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        Email = email;
        ProfileImage = profileImage;
        this.key = key;
        this.profileType = profileType;
    }

    public User() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    @Override
    public String toString() {
        return "User{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Password='" + Password + '\'' +
                ", Email='" + Email + '\'' +
                ", ProfileImage='" + ProfileImage + '\'' +
                ", key='" + key + '\'' +
                ", profileType='" + profileType + '\'' +
                '}';
    }
}
