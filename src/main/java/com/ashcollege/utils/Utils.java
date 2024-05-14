package com.ashcollege.utils;

import com.ashcollege.Persist;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Round;
import com.ashcollege.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        String[] teamNames = {"Barcelona FC", "Real Madrid FC", "Milan AC", "PSG FC", "liverpool FC", "Arsenal FC", "Manchester United FC", "Macabi TA FC"};
        List<Team> teams = persist.loadTeams(Team.class);
        if (teams.isEmpty())
            for (int i = 0; i < 8; i++) {
                Team team = new Team(teamNames[i]);
                persist.save(team);
            }
        //create 27 dates from today. gap of 2 between days
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
        List<Match> matchList = persist.loadMatches(Match.class);
        if(matchList.isEmpty()) {
            // create 27 matches
            List<Match> matches = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                for (int j = i + 1; j <= 8; j++) {
                    Match match = new Match(i, j, dateList.get(count), j - 1);
                    matches.add(match);
                    count++;
                }
            }
            for (int i = 0; i < matches.size(); i++) {
                persist.save(matches.get(i));
            }
        }
      /* // create 7 rounds.
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
        }*/

    }




}


