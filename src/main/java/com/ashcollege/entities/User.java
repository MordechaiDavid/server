package com.ashcollege.entities;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String secret;
    private String email;
    private Double balance; // שינוי לסוג Double

    public User(int id, String username, String password, String email, String secret) {
        this(username, password, email, secret);
        this.id = id;
    }

    public User(String username, String password, String email, String secret) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.secret = secret;
        this.balance = 100.0;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        if (balance == null) {
            this.balance = 100.0; // ערך ברירת מחדל
        } else {
            this.balance = balance;
        }
    }

    public boolean isSameUsername(String username) {
        return this.username.equals(username);
    }

    public boolean isSameCreds(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}