package com.ashcollege.entities;

public class Bet {

    private int id;

    private Match match;

    private User gambler;

    public Bet(int id, Match match, User user) {
        this.id = id;
        this.match = match;
        this.gambler = user;
    }

    public Bet() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public User getGambler() {
        return gambler;
    }

    public void setGambler(User gambler) {
        this.gambler = gambler;
    }
}
