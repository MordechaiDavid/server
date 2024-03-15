package com.ashcollege.utils;

import com.ashcollege.Persist;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Round;
import com.ashcollege.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Utils {
    @Autowired
    Persist persist;

    @PostConstruct
    public void createTeams() {
        String[] teamNames = {"Barcelona FC", "Real Madrid FC", "Milan AC", "PSG FC", "Liverpol FC", "Arsenal FC", "Manchester United FC", "Macabi TA FC"};
        List<Team> teams = persist.loadTeams(Team.class);
        if (teams.isEmpty())
            for (int i = 0; i < 8; i++) {
                Team team = new Team(teamNames[i]);
                persist.save(team);
            }

//        for (int i = 1; i < 8; i++) {
//            for (int j = 1; j < 8; j++) {
//                if (i != j) {
//                    Match match = new Match(i, j, new Date(), new Round());
//                    persist.save(match);
//                }
//            }
//        }
    }


}


