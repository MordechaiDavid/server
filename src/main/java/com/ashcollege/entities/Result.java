package com.ashcollege.entities;

public class Result {

    private int resultTeam1;

    private int resultTeam2;

    public Result(int resultTeam1 ,int resultTeam2) {
        this.resultTeam1 = resultTeam1;
        this.resultTeam2 = resultTeam2;
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
}
