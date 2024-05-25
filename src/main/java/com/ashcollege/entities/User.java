package com.ashcollege.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private String secret;
    private String email;
    private double balance;

    public User(int id, String username, String password,String email, String secret) {
        this(username, password,email, secret);
        this.id = id;
    }

    public User(String username, String password, String email, String secret) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.secret = secret;
        this.balance = 100;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isSameUsername(String username) {
        return this.username.equals(username);
    }

    public boolean isSameCreds(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);

    }

}

