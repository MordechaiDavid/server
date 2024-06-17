package com.ashcollege.entities;

import javax.persistence.Entity;
import java.util.Random;

public class Team {
    private int id;
    private String name;
    private int score;
    private double attackLevel;
    private double defenceLevel;
    private boolean isInjury;

    private int goalsScored;
    private  int goalsConcedes;

    public Team(String name) {
        Random random = new Random();
        this.name = name;
        this.score = 0;
        this.attackLevel = random.nextDouble();
        this.defenceLevel = random.nextDouble();
        this.goalsScored = 0;
        this.goalsConcedes = 0;
        this.isInjury = false;
    }

    public Team() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getAttackLevel() {
        return attackLevel;
    }

    public void setAttackLevel(double level) {
        this.attackLevel = level;
    }

    public double getDefenceLevel() {
        return defenceLevel;
    }

    public void setDefenceLevel(double defenceLevel) {
        this.defenceLevel = defenceLevel;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsConcedes() {
        return goalsConcedes;
    }

    public void setGoalsConcedes(int goalsConcedes) {
        this.goalsConcedes = goalsConcedes;
    }

    public boolean getIsInjury() {
        return isInjury;
    }

    public void setInjury(boolean injury) {
        isInjury = injury;
    }
}
