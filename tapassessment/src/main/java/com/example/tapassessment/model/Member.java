package com.example.tapassessment.model;

public class Member {
    private int id;
    private String surname;
    private String forenames;
    private String email;
    
    // Getters and setters
    // Optionally, add a toString() method
    // ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForenames() {
        return forenames;
    }

    public void setForenames(String forenames) {
        this.forenames = forenames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
