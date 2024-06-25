package com.ashcollege;

import com.ashcollege.entities.*;
import com.ashcollege.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FootballMatch {
    @Autowired
    Persist persist;
    @Autowired
    Utils utils;

    @PostConstruct
    public void startMatch() {
        Runnable matchProgression = new Runnable() {
            @Override
            public void run() {
                int matchesInRound = 0 , roundNum = 1;
                while (roundNum < 8) {
                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTime(today);
                    Date currentDate = calendar.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("d/M/yy H:mm:ss");
                    String currentTime = formatter.format(currentDate);
                    List<Match> matchList = persist.loadList(Match.class);
                    for (int i = 0; i < matchList.size(); i++) {
                        if (matchList.get(i).getDate().equals(currentTime)){
                            matchesInRound++;
                            int matchDurationSeconds = 30;
                            Result result = choseWinner(matchList.get(i));
                            int desiredResultTeam1 = result.getResultTeam1();
                            int desiredResultTeam2 = result.getResultTeam2();
                            List <Integer> secondsOfGoals = selectNumbers(desiredResultTeam1+desiredResultTeam2,30);
                            System.out.println(secondsOfGoals);
                            int timePerIterationMillis = 1000;
                            int index = 0;
                            for(int time=1; time<=matchDurationSeconds; time++) {
                                if(!secondsOfGoals.isEmpty()) {
                                    if (time == secondsOfGoals.get(index)) {
                                        if (matchList.get(i).getResultTeam1() < desiredResultTeam1 && matchList.get(i).getResultTeam2() < desiredResultTeam2) {
                                            boolean currentTurn = ThreadLocalRandom.current().nextBoolean();
                                            if (currentTurn)
                                                matchList.get(i).setResultTeam1(matchList.get(i).getResultTeam1() + 1);
                                            else
                                                matchList.get(i).setResultTeam2(matchList.get(i).getResultTeam2() + 1);
                                        } else if (matchList.get(i).getResultTeam1() < desiredResultTeam1) {
                                            matchList.get(i).setResultTeam1(matchList.get(i).getResultTeam1() + 1);
                                        } else {
                                            matchList.get(i).setResultTeam2(matchList.get(i).getResultTeam2() + 1);
                                        }
                                        persist.save(matchList.get(i));
                                        System.out.println(time);
                                        System.out.println(matchList.get(i).getTeam1().getName() + " " + matchList.get(i).getResultTeam1());
                                        System.out.println(matchList.get(i).getTeam2().getName() + " " + matchList.get(i).getResultTeam2());

                                        if (index < desiredResultTeam1 + desiredResultTeam2 - 1)
                                            index++;
                                    }
                                }

                                try {
                                    Thread.sleep(timePerIterationMillis);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(time == matchDurationSeconds)
                                    updateData(matchList.get(i),result);
                            }
                            List <Bet> bettingOnMatch = persist.getBettingOnGame(matchList.get(i).getId());
                            if(!bettingOnMatch.isEmpty())
                                updateBalanceOfWinner(bettingOnMatch);

                        }
                        if(matchesInRound ==4){
                            Random random = new Random();
                            int numOfTeams = random.nextInt(8);
                            System.out.println("random is "+numOfTeams);
                            if(numOfTeams > 0) {
                                List<Integer> selectedNumbers = selectNumbers(numOfTeams, 8);
                                System.out.println("change boolean in " + selectedNumbers);
                                updateInjury(selectedNumbers);
                            }
                            utils.calculateOdds(persist.getMatchesByType("available"));
                            roundNum++;
                            System.out.println("Round num "+roundNum+ " begin");
                            matchesInRound =0;
                        }
                    }
                }
            }
        };
        Thread matchThread = new Thread(matchProgression);
        matchThread.start();
    }

    private void updateBalanceOfWinner (List<Bet> betList){
        for(Bet bet : betList){
            int winner = bet.getMatch().choseWinner();
            if(winner == bet.getResult()){
                String secret = bet.getGambler().getSecret();
                double updateSum= bet.getSumOfBet();
                if(winner==0)
                    updateSum *= bet.getMatch().getOddsDraw();
                if(winner==1)
                    updateSum *= bet.getMatch().getOddsTeam1();
                if(winner==2)
                    updateSum *= bet.getMatch().getOddsTeam2();
                persist.updateBalance(updateSum,secret);
            }
        }
    }
    private void updateData (Match match,Result result){
        int gameWinner =match.choseWinner();
        if(gameWinner == 1){
            match.getTeam1().setScore(match.getTeam1().getScore()+3);
            match.getTeam1().setWon(match.getTeam1().getWon()+1);
            match.getTeam2().setLost(match.getTeam2().getLost()+1);
        }
        if(gameWinner==2){
            match.getTeam2().setScore(match.getTeam2().getScore()+3);
            match.getTeam1().setLost(match.getTeam1().getLost()+1);
            match.getTeam2().setWon(match.getTeam2().getWon()+1);
        }
        if(gameWinner==0){
            match.getTeam1().setScore(match.getTeam1().getScore()+1);
            match.getTeam2().setScore(match.getTeam2().getScore()+1);
            match.getTeam1().setDrawn(match.getTeam1().getDrawn()+1);
            match.getTeam2().setDrawn(match.getTeam2().getDrawn()+1);
        }
        match.getTeam1().setGoalsScored(match.getTeam1().getGoalsScored()+result.getResultTeam1());
        match.getTeam1().setGoalsConcedes(match.getTeam1().getGoalsConcedes()+result.getResultTeam2());
        match.getTeam2().setGoalsScored(match.getTeam2().getGoalsScored()+result.getResultTeam2());
        match.getTeam2().setGoalsConcedes(match.getTeam2().getGoalsConcedes()+result.getResultTeam1());
        match.getTeam1().setAttackLevel(Math.min(1,match.getTeam1().getAttackLevel()+(result.getResultTeam1()*0.02)));
        match.getTeam2().setAttackLevel(Math.min(1,match.getTeam2().getAttackLevel()+(result.getResultTeam2()*0.02)));
        match.getTeam1().setDefenceLevel(Math.max(0, match.getTeam1().getDefenceLevel()-(result.getResultTeam2()*0.02)));
        match.getTeam2().setDefenceLevel(Math.max(0, match.getTeam2().getDefenceLevel()-(result.getResultTeam1()*0.02)));
        if(match.getTeam1().getGoalsScored()==0){
            match.getTeam1().setAttackLevel(Math.max(0,match.getTeam1().getAttackLevel()-0.05));
            match.getTeam2().setDefenceLevel(Math.min(0,match.getTeam2().getDefenceLevel()+0.05));
        }
        if(match.getTeam2().getGoalsScored()==0){
            match.getTeam1().setDefenceLevel(Math.min(0,match.getTeam1().getDefenceLevel()+0.05));
            match.getTeam2().setAttackLevel(Math.max(0,match.getTeam2().getAttackLevel()-0.05));
        }
        Team teamA = match.getTeam1();
        Team teamB = match.getTeam2();
        persist.save(teamA);
        persist.save(teamB);
    }
private void updateInjury (List <Integer> numbers){
        List <Team> teamList = persist.loadList(Team.class);
        for(int i =0; i<numbers.size(); i++){
            int chosenTeam = numbers.get(i)-1;
            Team team = teamList.get(chosenTeam);
            team.setIsInjury(!teamList.get(chosenTeam).getIsInjury());
            persist.save(team);
        }
}
    private List<Integer> selectNumbers (int total ,int range){
        List <Integer> origin = new ArrayList<>();
        List <Integer> chosenNumbers = new ArrayList<>();
        for(int i=1; i<=range; i++)
            origin.add(i);
        Collections.shuffle(origin);
        for(int i=1; i<=total ;i++)
           chosenNumbers.add(origin.get(i));
        Collections.sort(chosenNumbers);
        return chosenNumbers;
    }

    private Result choseWinner (Match match){
        Result result=null;
        double total = match.getOddsTeam1()+ match.getOddsTeam2()+ match.getOddsDraw();
        double team1WinChance = match.getOddsTeam1()/total;
        double team2WinChance= match.getOddsTeam2()/total;
        double drawChance = match.getOddsDraw()/total;
        int goalsTeam1 = numOfGoals(match.getTeam1().getAttackLevel(),match.getTeam2().getDefenceLevel());
        int goalsTeam2 = numOfGoals(match.getTeam2().getAttackLevel(),match.getTeam1().getDefenceLevel());
        goalsTeam1 = Math.min(goalsTeam1,4);
        goalsTeam2 = Math.min(goalsTeam2,4);
        Random random = new Random();
        double winner = random.nextDouble();
        if(winner <= team1WinChance){
          if(goalsTeam1 <=goalsTeam2)
              goalsTeam1 = goalsTeam2+random.nextInt(2)+1;
        }
        if(winner>team1WinChance && winner <=drawChance+team1WinChance){
            if(goalsTeam1!=goalsTeam2)
                goalsTeam1 = goalsTeam2;
        }
        if(winner>drawChance+team1WinChance){
            if(goalsTeam2 <= goalsTeam1)
                goalsTeam2 = goalsTeam1+random.nextInt(2)+1;
        }
          result= new Result(goalsTeam1,goalsTeam2);

        return result;
    }

    public int numOfGoals (double attackLevel, double defenceLevel){
        double ratio = attackLevel / defenceLevel;
        int maxGoals = 6;
        Random random = new Random();
        double sum = random.nextDouble();
        return (int) Math.round(sum*maxGoals*(ratio/(ratio+1)));

    }
}