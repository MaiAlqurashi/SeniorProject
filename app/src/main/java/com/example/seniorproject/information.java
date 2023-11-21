package com.example.seniorproject;

public class information {
    String location;
    String description;
    String fName;
    String lName;
    String phone;
    String label;
    public information(String location, String description) {
        this.location = location;
        this.description = description;

    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getPhone() {
        return phone;
    }
}