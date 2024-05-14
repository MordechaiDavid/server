package com.ashcollege.utils;

import com.ashcollege.Persist;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Round;
import com.ashcollege.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class Utils {
    @Autowired
    Persist persist;

    @PostConstruct
    public void createTeams() {
        List<Team> teams = persist.loadTeams(Team.class);
        if (teams.isEmpty()) {
            String[] teamNames = {"Barcelona FC", "Real Madrid FC", "Milan AC", "PSG FC", "liverpool FC", "Arsenal FC", "Manchester United FC", "Macabi TA FC"};
            for (int i = 0; i < 8; i++) {
                Team team = new Team(teamNames[i]);
                persist.save(team);
            }
        }
        List<Match> matchList = persist.loadMatches(Match.class);
        if(matchList.isEmpty()) {
            List<Date> dateList = new ArrayList<>();
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 28; i++) {
                calendar.setTime(today);
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.DAY_OF_MONTH, i * 2);
                Date currentDate = calendar.getTime();
                dateList.add(currentDate);
            }
            int count = 0;


            List<Match> matches = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                for (int j = i + 1; j <= 8; j++) {
                    Match match = new Match(i, j, dateList.get(count), j - 1);
                    matches.add(match);
                    count++;
                }
            }
            for (Match match : matches) {
                persist.save(match);
            }
        }

    }


}


