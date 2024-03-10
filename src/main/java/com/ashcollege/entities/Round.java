package com.ashcollege.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Round {
    @Id
    private int id;
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
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
