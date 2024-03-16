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
        String[] teamNames = {"Barcelona FC", "Real Madrid FC", "Milan AC", "PSG FC", "liverpool FC", "Arsenal FC", "Manchester United FC", "Macabi TA FC"};
        List<Team> teams = persist.loadTeams(Team.class);
        if (teams.isEmpty())
            for (int i = 0; i < 8; i++) {
                Team team = new Team(teamNames[i]);
                persist.save(team);
            }

        // create 27 matches
        List<Match> matches = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (j>i) {
                    Match match = new Match(i, j, new Date());
                    matches.add(match);
                }
            }
        }
        // create 7 rounds.
        // i is for rounds.
        // groups for ensure that all round is 4 matches which is 8 groups max.
        List<Integer> groups = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            for (int j = i-1; j < matches.size(); j++) {
                boolean exist = false;
                if (groups.size() <= 8){
                    for (int k = 0; k < groups.size(); k++) {
                        if (matches.get(j).getTeam1() == groups.get(k) ||
                                matches.get(j).getTeam2() == groups.get(k)
                                 || matches.get(j).getRoundNum() != -1){
                            exist = true;
                        }
                    }
                    if (!exist){
                        matches.get(j).setRoundNum(i);
                        groups.add(matches.get(j).getTeam1());
                        groups.add(matches.get(j).getTeam2());
                    }
                }
            }
            groups.clear();
        }

        for (int i = 0; i < matches.size(); i++) {
            List<Match> matchList = persist.loadTeams(Match.class);
            if (matchList.isEmpty()) {
                persist.save(matches.get(i));
            }
        }
    }


}


