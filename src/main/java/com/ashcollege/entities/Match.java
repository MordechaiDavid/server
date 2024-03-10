package com.ashcollege.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;


public class Match {

    private int id;
    private String team1;
    private String team2;
    private LocalDateTime date;
    private int resultTeam1;
    private int resultTeam2;
    private int winner;
    private String corners;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCorners() {
        return corners;
    }

    public void setCorners(String corners) {
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
