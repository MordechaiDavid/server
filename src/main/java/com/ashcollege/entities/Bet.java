package com.ashcollege.entities;

public class Bet {

    private int id;
    private Match match;
    private User gambler;
    private int sumOfBet;
    private int result;

    public Bet( Match match, User user,int sumOfBet,int result) {
        this.match = match;
        this.gambler = user;
        this.sumOfBet = sumOfBet;
        this.setResult(result);
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

    public int getSumOfBet() {
        return sumOfBet;
    }

    public void setSumOfBet(int sumOfBet) {
        this.sumOfBet = sumOfBet;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        if(result == 0 || result == 1 || result == 2)
           this.result = result;
    }
}
