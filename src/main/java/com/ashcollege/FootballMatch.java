package com.ashcollege;

import com.ashcollege.entities.Match;
import com.ashcollege.entities.Result;
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

    @PostConstruct
    public void startMatch() {
        Runnable matchProgression = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTime(today);
                    Date currentDate = calendar.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("d/M/yy H:mm:ss");
                    String currentTime = formatter.format(currentDate);
                    List<Match> matchList = persist.loadList(Match.class);
                    for (int i = 0; i < matchList.size(); i++) {
                        if (matchList.get(i).getDate().equals(currentTime)){
                            int matchDurationSeconds = 30;
                            Result result = choseWinner(matchList.get(i));
                            int desiredResultTeam1 = result.getResultTeam1();
                            int desiredResultTeam2 = result.getResultTeam2();

                            List <Integer> secondsOfGoals = selectSeconds(result);
                            System.out.println(secondsOfGoals);
                            int timePerIterationMillis = 1000;
                            if(!secondsOfGoals.isEmpty()){
                            int index = 0;
                            for(int time=0; time<=matchDurationSeconds; time++) {
                                if(time == secondsOfGoals.get(index)  ){
                                if(matchList.get(i).getResultTeam1() < desiredResultTeam1 && matchList.get(i).getResultTeam2() < desiredResultTeam2)  {
                                    boolean currentTurn = ThreadLocalRandom.current().nextBoolean();
                                    if(currentTurn)
                                        matchList.get(i).setResultTeam1(matchList.get(i).getResultTeam1() + 1);
                                    else
                                        matchList.get(i).setResultTeam2(matchList.get(i).getResultTeam2() + 1);
                                }
                                else if (matchList.get(i).getResultTeam1() < desiredResultTeam1) {
                                    matchList.get(i).setResultTeam1(matchList.get(i).getResultTeam1() + 1);
                                }
                                else {
                                    matchList.get(i).setResultTeam2(matchList.get(i).getResultTeam2() + 1);
                                }
                                persist.save(matchList.get(i));
                                System.out.println(time);
                                System.out.println(matchList.get(i).getTeam1().getName()+" "+matchList.get(i).getResultTeam1());
                                System.out.println(matchList.get(i).getTeam2().getName()+" "+matchList.get(i).getResultTeam2());

                                try {
                                    Thread.sleep(timePerIterationMillis);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(index< desiredResultTeam1+desiredResultTeam2-1)
                                   index ++;
                                }
                            }
                            }
                        }
                    }
                }
            }

        };


        Thread matchThread = new Thread(matchProgression);
        matchThread.start();


    }

    public List<Integer> selectSeconds (Result result){
        int totalGoals = result.getResultTeam1()+ result.getResultTeam2();
        List <Integer> seconds = new ArrayList<>();
        List <Integer> chosenSeconds = new ArrayList<>();
        for(int i=0; i<=30; i++)
            seconds.add(i);
        Collections.shuffle(seconds);
        for(int i=1; i<=totalGoals ;i++)
           chosenSeconds.add(seconds.get(i));
        Collections.sort(chosenSeconds);
        return chosenSeconds;
    }

    public Result choseWinner (Match match){
        Result result=null;
        double total = match.getOddsTeam1()+ match.getOddsTeam2()+ match.getOddsDraw();
        double team1WinChance = match.getOddsTeam1()/total;
        double team2WinChance= match.getOddsTeam2()/total;
        double drawChance = match.getOddsDraw()/total;
        int goalsTeam1 = numOfGoals(match.getTeam1().getAttackLevel(),match.getTeam2().getDefenceLevel());
        int goalsTeam2 = numOfGoals(match.getTeam2().getAttackLevel(),match.getTeam1().getDefenceLevel());
       // System.out.println(team1WinChance+" , "+drawChance+" , "+team2WinChance);
        Random random = new Random();
        double winner = random.nextDouble();
      //  System.out.println(winner);
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
        int maxGoals = 7;
        Random random = new Random();
        double sum = random.nextDouble();
        return (int) Math.round(sum*maxGoals*(ratio/(ratio+1)));

    }
}