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
        List<Team> teams = persist.loadList(Team.class);
        if (teams.isEmpty()) {
            String[] teamNames = {"Barcelona FC", "Real Madrid FC", "Milan AC", "PSG FC", "liverpool FC", "Arsenal FC", "Manchester United FC", "Macabi TA FC"};
            for (int i = 0; i < 8; i++) {
                Team team = new Team(teamNames[i]);
                teams.add(team);
                persist.save(team);
            }
        }
        List<Match> matchList = persist.loadList(Match.class);
        if(matchList.isEmpty()) {
            int numOfRounds = 7;
            int numOfGames = 4;
            String [][] dateArray = new String[numOfRounds][numOfGames];
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.add(Calendar.MINUTE, 2);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            SimpleDateFormat formatter = new SimpleDateFormat("d/M/yy H:mm:ss");
            for (int i = 0; i < numOfRounds; i++) {
                for (int j = 0; j < numOfGames; j++) {
                    Date currentDate = calendar.getTime();
                    String formattedDate = formatter.format(currentDate);
                    dateArray[i][j] = formattedDate;
                    if (j < numOfGames-1) {
                        calendar.add(Calendar.MINUTE, 1);
                    }
                }
                if (i < numOfRounds-1) {
                    calendar.add(Calendar.MINUTE, 5);
                }
            }


            List<Match> matches = new ArrayList<>();
            for (int i = 1; i <= numOfRounds; i++) {
                for (int j = 1; j <= numOfGames; j++) {
                    int teamA= (i+j-2)%7+1;
                    int teamB = (7-(j-1)+i-1)%7+1;
                    if(j-1==0)
                        teamB =8;
                   Match match = new Match(i, teams.get(teamA-1), teams.get(teamB-1), dateArray[i-1][j-1]);
                    matches.add(match);
                }
            }
            calculateOdds(matches);
        }
    }

  public void calculateOdds(List <Match> matches) {
        for (Match match : matches){
        double powerA = match.getTeam1().getAttackLevel() - match.getTeam2().getDefenceLevel();
        double powerB = match.getTeam2().getAttackLevel() - match.getTeam1().getDefenceLevel();
        double injuredInA = !match.getTeam1().getIsInjury() ? 0: 0.15;
        double injuredInB = !match.getTeam2().getIsInjury() ? 0: 0.15;
        if(powerA < 0 && powerB < 0){
            powerA = 1 + powerA;
            powerB = 1 + powerB;
        }
        else{
            powerA = Math.max(0, powerA);
            powerB = Math.max(0, powerB);
        }
        double totalPower = powerA + powerB;
        double winProbA,winProbB,drawProb;

        if (totalPower == 0) {
            winProbA = 0.25;
            winProbB = 0.25;
            drawProb = 0.5;
        } else {
            winProbA = powerA / totalPower;
            winProbB = powerB / totalPower;
            drawProb = 1 - totalPower;
        }
        winProbA = 1 / winProbA - 0.3 + injuredInA;
        winProbB =  1 / winProbB + injuredInB;

        double oddsA = Math.max(1.1, Math.min(5,  winProbA));
        double oddsB = Math.max(1.1, Math.min(5,  winProbB));
        double oddsDraw = Math.max(1.1, Math.min(5.0, 1 / drawProb));



        DecimalFormat df = new DecimalFormat("#.##");
        oddsA = Double.parseDouble(df.format(oddsA));
        oddsB = Double.parseDouble(df.format(oddsB));
        oddsDraw = Double.parseDouble(df.format(oddsDraw));

        match.setOddsTeam1(oddsA);
        match.setOddsTeam2(oddsB);
        match.setOddsDraw(oddsDraw);
        persist.save(match);

        }
    }




}


