package com.ashcollege.entities;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.List;

public class Round {
    private int id;
    private List<Match> matchList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}
