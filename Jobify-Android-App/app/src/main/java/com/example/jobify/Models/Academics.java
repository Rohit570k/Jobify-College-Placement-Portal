package com.example.jobify.Models;

public class Academics {
    private String name;
    private String college;
    private String rollnos;
    private String tenthmark;
    private String tenthyearofpassing;
    private String twelfthmark;
    private String twelfthmarkyearofpassing;
    private String collegemarks;
    private String collegeyearofpassing;
    private String nosofbacklog;
    private String title;
//    private String createdBy; // Assuming this is a String representation of ObjectId

    // Constructors, getters, setters, and other methods

    // Constructor
    public Academics(String name, String college, String rollnos, String  tenthmark, String  tenthyearofpassing,
                     String twelfthmark, String twelfthmarkyearofpassing, String collegemarks,
                     String collegeyearofpassing, String nosofbacklog, String title) {
        this.name = name;
        this.college = college;
        this.rollnos = rollnos;
        this.tenthmark = tenthmark;
        this.tenthyearofpassing = tenthyearofpassing;
        this.twelfthmark = twelfthmark;
        this.twelfthmarkyearofpassing = twelfthmarkyearofpassing;
        this.collegemarks = collegemarks;
        this.collegeyearofpassing = collegeyearofpassing;
        this.nosofbacklog = nosofbacklog;
        this.title = title;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getRollnos() {
        return rollnos;
    }

    public void setRollnos(String rollnos) {
        this.rollnos = rollnos;
    }

    public String getTenthmark() {
        return tenthmark;
    }

    public void setTenthmark(String tenthmark) {
        this.tenthmark = tenthmark;
    }

    public String getTenthyearofpassing() {
        return tenthyearofpassing;
    }

    public void setTenthyearofpassing(String tenthyearofpassing) {
        this.tenthyearofpassing = tenthyearofpassing;
    }

    public String getTwelfthmark() {
        return twelfthmark;
    }

    public void setTwelfthmark(String twelfthmark) {
        this.twelfthmark = twelfthmark;
    }

    public String getTwelfthmarkyearofpassing() {
        return twelfthmarkyearofpassing;
    }

    public void setTwelfthmarkyearofpassing(String twelfthmarkyearofpassing) {
        this.twelfthmarkyearofpassing = twelfthmarkyearofpassing;
    }

    public String getCollegemarks() {
        return collegemarks;
    }

    public void setCollegemarks(String collegemarks) {
        this.collegemarks = collegemarks;
    }

    public String getCollegeyearofpassing() {
        return collegeyearofpassing;
    }

    public void setCollegeyearofpassing(String collegeyearofpassing) {
        this.collegeyearofpassing = collegeyearofpassing;
    }

    public String getNosofbacklog() {
        return nosofbacklog;
    }

    public void setNosofbacklog(String nosofbacklog) {
        this.nosofbacklog = nosofbacklog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    // Other methods
    // ...

    @Override
    public String toString() {
        return "Academics{" +
                "name='" + name + '\'' +
                ", college='" + college + '\'' +
                ", rollnos='" + rollnos + '\'' +
                ", tenthmark=" + tenthmark +
                ", tenthyearofpassing=" + tenthyearofpassing +
                ", twelfthmark='" + twelfthmark + '\'' +
                ", twelfthmarkyearofpassing='" + twelfthmarkyearofpassing + '\'' +
                ", collegemarks='" + collegemarks + '\'' +
                ", collegeyearofpassing='" + collegeyearofpassing + '\'' +
                ", nosofbacklog='" + nosofbacklog + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
