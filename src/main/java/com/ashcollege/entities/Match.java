package com.ashcollege.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

public class Match {

    private int id;
    private Team team1;
    private Team team2;
    private String date;
    private int roundNum;
    private double oddsTeam1;
    private double oddsTeam2;
    private double   oddsDraw;
    private int resultTeam1;
    private int resultTeam2;
    private int winner;
    private int corners;

    public Match( Team team1, Team team2, String date) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.roundNum = -1;
    }
    public Match(int roundNum, Team team1,Team team2, String date) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.roundNum = roundNum;
    }
    public Match() {

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public double getOddsTeam1() {
        return oddsTeam1;
    }

    public void setOddsTeam1(double oddsTeam1) {
        this.oddsTeam1 = oddsTeam1;
    }

    public double getOddsTeam2() {
        return oddsTeam2;
    }

    public void setOddsTeam2(double oddsTeam2) {
        this.oddsTeam2 = oddsTeam2;
    }

    public double getOddsDraw() {
        return oddsDraw;
    }

    public void setOddsDraw(double oddsDraw) {
        this.oddsDraw = oddsDraw;
    }

    public int getWinner() {
        int winner=0;
        if(resultTeam1>resultTeam2)
            winner =1;
        if(resultTeam1<resultTeam2)
            winner =2;
        this.winner = winner;
        return winner;
    }



}
