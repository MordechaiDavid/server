package com.ashcollege;

import com.ashcollege.entities.Match;
import com.ashcollege.entities.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

                            int desiredResultTeam1 = 4;  //should be function that return a result
                            int desiredResultTeam2 = 3;  //should be function that return a result
                            Result result = new Result(desiredResultTeam1,desiredResultTeam2);
                            List <Integer> secondsOfGoals = selectSeconds(result);
                            System.out.println(secondsOfGoals);
                            int timePerIterationMillis = 3000;
                            int time = 0,index = 0;
                            while (time < matchDurationSeconds) {
                                if(time == secondsOfGoals.get(index)){
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
                                time ++;
                            }
                            // TO DO:
                            // 1. update attack_level and defence_level of the teams
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
}