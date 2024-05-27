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

    public Team(String name) {
        Random random = new Random();
        this.name = name;
        this.score = 0;
        this.attackLevel = random.nextDouble();
        this.defenceLevel = random.nextDouble();
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

    public boolean getIsInjury() {
        return isInjury;
    }

    public void setInjury(boolean injury) {
        isInjury = injury;
    }
}
