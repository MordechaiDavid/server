package com.ashcollege.utils;

import com.ashcollege.Persist;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
            List<String> dateList = new ArrayList<>();
            Date today = new Date();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


            for (int i = 0; i < 8; i++) {
                calendar.setTime(today);
                calendar.set(Calendar.HOUR_OF_DAY, 17);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.DAY_OF_MONTH, (i * 2)+1);
                Date currentDate = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("d/M/yy H:mm:ss");
                String formattedDate = formatter.format(currentDate);
                dateList.add(formattedDate);
            }

            List<Match> matches = new ArrayList<>();
            for (int i = 1; i <= 7; i++) {
                for (int j = 1; j <= 4; j++) {
                    Match match= null;
                    int teamA= (i+j-2)%7+1;
                    int teamB = (7-(j-1)+i-1)%7+1;
                    if(j-1==0)
                        teamB =8;

                    match = new Match(i, teams.get(teamA-1), teams.get(teamB-1), dateList.get(i-1));
                    calculateOdds(match);
                    matches.add(match);
                }
            }
            for (Match match : matches) {
                 persist.save(match);
            }
        }

    }




    public void calculateOdds(Match match) {
        double powerA = match.getTeam1().getAttackLevel() - match.getTeam2().getDefenceLevel();
        double powerB = match.getTeam2().getAttackLevel() - match.getTeam1().getDefenceLevel();

        powerA = Math.max(0, powerA);
        powerB = Math.max(0, powerB);

        double totalPower = powerA + powerB;
        double winProbA;
        double winProbB;
        double drawProb;

        if (totalPower == 0) {
            winProbA = 0.3333;
            winProbB = 0.3333;
            drawProb = 0.3333;
        } else {
            winProbA = powerA / totalPower;
            winProbB = powerB / totalPower;
            drawProb = 1 - winProbA - winProbB;
        }

        double oddsA = Math.max(1.1, Math.min(5, 1 / winProbA));
        double oddsB = Math.max(1.1, Math.min(5, 1 / winProbB));
        double oddsDraw = Math.max(1.1, Math.min(5, 1 / drawProb));

        if (oddsA >= 3 && oddsB >= 3 && oddsDraw >= 3) {
            oddsA = Math.min(2.9, oddsA);
            oddsB = Math.min(2.9, oddsB);
            oddsDraw = Math.min(2.9, oddsDraw);
        }


        DecimalFormat df = new DecimalFormat("#.##");
        oddsA = Double.parseDouble(df.format(oddsA));
        oddsB = Double.parseDouble(df.format(oddsB));
        oddsDraw = Double.parseDouble(df.format(oddsDraw));

        match.setOddsTeam1(oddsA);
        match.setOddsTeam2(oddsB);
        match.setOddsDraw(oddsDraw);
    }


}


