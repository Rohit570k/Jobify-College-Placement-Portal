package com.example.jobify.Models;

public class User {
    private String _id;
    private String name;
    private String email;
    private String password;
    private String lastName;
    private String location;
    private String role;
    private int erpId;

    //For Login
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //For Signup
    public User( String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    //For Updating
    public User( String name, String lastName,String email, String location,int erpId) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.location = location;
        this.erpId = erpId;
    }

    public int getErpId() {
        return erpId;
    }

    public void setErpId(int erpId) {
        this.erpId = erpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
