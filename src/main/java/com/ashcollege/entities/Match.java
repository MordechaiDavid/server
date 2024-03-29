package com.ashcollege.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;


public class Match {

    private int id;
    private int team1;
    private int team2;
    private Date date;
    private int roundNum;
    private int resultTeam1;
    private int resultTeam2;
    private int winner;
    private int corners;

    public Match( int team1, int team2, Date date) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.roundNum = -1;
    }

    public Match() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeam1() {
        return team1;
    }

    public void setTeam1(int team1) {
        this.team1 = team1;
    }

    public int getTeam2() {
        return team2;
    }

    public void setTeam2(int team2) {
        this.team2 = team2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int round) {
        this.roundNum = round;
    }

    public int getCorners() {
        return corners;
    }

    public void setCorners(int corners) {
        this.corners = corners;
    }

    public int getResultTeam1() {
        return resultTeam1;
    }

    public void setResultTeam1(int resultTeam1) {
        this.resultTeam1 = resultTeam1;
    }

    public int getResultTeam2() {
        return resultTeam2;
    }

    public void setResultTeam2(int resultTeam2) {
        this.resultTeam2 = resultTeam2;
    }
    public int getWinner() {
        return winner;
    }

    public void setWinner() {
        int winner=0;
        if(resultTeam1>resultTeam2)
            winner =1;
        if(resultTeam1<resultTeam2)
            winner =2;
        this.winner = winner;
    }
}
