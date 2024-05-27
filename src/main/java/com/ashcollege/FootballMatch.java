package com.ashcollege;

import com.ashcollege.entities.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
                    System.out.println(currentTime);
                    List<Match> matchList = persist.loadMatches(Match.class);
                    for (int i = 0; i < matchList.size(); i++) {
                        if (matchList.get(i).getDate().equals(currentTime)){
                            int matchDurationSeconds = 30;

                            int desiredResultTeam1 = 4;  //should be function that return a result
                            int desiredResultTeam2 = 3;  //should be function that return a result

                            int timePerIterationMillis = 3000;

                            int time = 0;
                            while (time < matchDurationSeconds) {
                                List<Match> matchList2 = persist.loadMatches(Match.class);

                                if (matchList.get(0).getResultTeam1() < desiredResultTeam1) {
                                    matchList.get(0).setResultTeam1(matchList.get(0).getResultTeam1() + 1);
                                }
                                if (matchList.get(0).getResultTeam2() < desiredResultTeam2) {
                                    matchList.get(0).setResultTeam2(matchList.get(0).getResultTeam2() + 1);
                                }
                                persist.save(matchList.get(0));
                                System.out.println(matchList.get(0).getResultTeam1());
                                System.out.println(matchList.get(0).getResultTeam2());

                                try {
                                    Thread.sleep(timePerIterationMillis);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                time += 3;
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


}
