package com.ashcollege.controllers;

import com.ashcollege.FootballMatch;
import com.ashcollege.Persist;
import com.ashcollege.entities.Bet;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Team;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BalanceResponse;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.BalanceResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.utils.DbUtils;
import com.ashcollege.utils.Utils;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ashcollege.utils.Errors.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GeneralController {

    @Autowired
    private DbUtils dbUtils;
    @Autowired
    private Persist persist;
    @Autowired
    private Utils utils;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();



    @RequestMapping (value = "login", method = {RequestMethod.POST})
    public BasicResponse login (String username, String password) {
        BasicResponse basicResponse = null;
        boolean success = false;
        Integer errorCode = null;
        if (!username.isEmpty()) {
            if (!password.isEmpty()) {
                User user = persist.login(username, password);
                if (user != null) {
                    basicResponse = new LoginResponse(true, errorCode, user.getId(), user.getSecret());
                } else {
                    errorCode = ERROR_LOGIN_WRONG_CREDS;
                }
            } else {
                errorCode = ERROR_NO_PASSWORD;
            }
        } else {
            errorCode = ERROR_NO_USERNAME;
        }
        if (errorCode != null) {
            basicResponse = new BasicResponse(success, errorCode);
        }
        return basicResponse;
    }

    @RequestMapping (value = "add-user" , method = {RequestMethod.POST})
    public boolean addUser (String username, String password, String email) {
        Faker faker = new Faker();
        User userToAdd = new User(username, password ,email,faker.lorem().word());
        return dbUtils.addUser(userToAdd);
    }

    @RequestMapping (value = "get-users")
    public List<User> getUsers () {
        List <User> users = persist.loadList(User.class);
        return users;
    }

    @RequestMapping (value = "get-user" , method = {RequestMethod.POST})
    public User getUser (String secret){
        User user = persist.getUserBySecret(secret);
        return user;
    }
    @RequestMapping(value = "create-account" , method = {RequestMethod.POST})
    public BasicResponse createAccount(String username, String password, String password1 ,String email) {
        BasicResponse basicResponse ;
        Integer errorCode = null;
        boolean success = false;
        if (!username.isEmpty()) {
            if (!password.isEmpty()) {
                if (password.equals(password1)) {
                    if (this.isStrongPassword(password)) {
                        if(this.isValidEmail(email)){
                            if (!persist.getUserByUserName(username)) {
                                Faker faker = new Faker();
                                User user = new User(username, password ,email,faker.lorem().word());
                                persist.save(user);
                                success = true;
                            } else{errorCode=ERROR_USERNAME_TAKEN;}}
                        else {
                            errorCode = EMAIL_FORMAT_NOT_VALID ;}}
                    else{
                        errorCode =PASSWORD_IS_WEEK ;}}
                else {
                    errorCode = ERROR_PASSWORDS_DONT_MATCH;}}
            else {
                errorCode = ERROR_NO_PASSWORD;
            }}
        else errorCode = ERROR_NO_USERNAME;
        basicResponse  = new BasicResponse(success,errorCode);
        return basicResponse;
    }

    @RequestMapping (value = "update-user", method = {RequestMethod.POST})
    public BasicResponse updateUser (String username, String password, String password1 ,String email,String secret) {
        BasicResponse basicResponse;
        Integer errorCode = null;
        boolean success = false;
        if (!username.isEmpty()) {
            if (!password.isEmpty()) {
                if (password.equals(password1)) {
                    if (this.isStrongPassword(password)) {
                        if(this.isValidEmail(email)){
                                User user = persist.getUserBySecret(secret);
                                if(user!=null) {
                                    if(!persist.checkUserByUserName(username,secret)){
                                        user = new User(username,password,email,secret);
                                        persist.updateUser(user);
                                        success = true;}
                                    else{
                                        errorCode = ERROR_USERNAME_TAKEN;}}
                                else{
                                    errorCode=ERROR_NO_SUCH_USER;}}
                        else {
                            errorCode = EMAIL_FORMAT_NOT_VALID ;}}
                    else{
                        errorCode =PASSWORD_IS_WEEK ;}}
                else {
                    errorCode = ERROR_PASSWORDS_DONT_MATCH;}}
            else {
                errorCode = ERROR_NO_PASSWORD;
            }}
        else errorCode = ERROR_NO_USERNAME;
        basicResponse = new BasicResponse(success,errorCode);
        return basicResponse;
    }

    @RequestMapping (value = "get-teams")
    public List<Team> getTeams(){
        return persist.getAllTeams();
    }

    @RequestMapping (value = "get-matches-by-type")
    public List<Match> getMatchesByType(String type){return persist.getMatchesByType(type);}

    @RequestMapping(value = "add-bet" )
    public BasicResponse addBet(int matchId, String secret, int sumOfBet, int result) throws ParseException {
        BasicResponse basicResponse=null;
        Integer errorCode = null;
        boolean success = false;
        User user = persist.getUserBySecret(secret);
        if(user != null){
            Match match = persist.getMatchesById(matchId);
            if(match !=null && isAvailableMatch(match)){
                if(isValidResult(result)){
                    if(isValidSum(sumOfBet, user.getBalance())){
                        success = true;
                        Bet bet = new Bet(match,user, sumOfBet ,result,winRatioInput(result,match));
                        persist.save(bet);
                        persist.updateBalance((double)-sumOfBet,secret);
                        basicResponse = new BalanceResponse(success,null,persist.getUserBySecret(secret));
                    }else{errorCode = ERROR_NO_VALID_SUM;}
                }else{errorCode = ERROR_NO_VALID_RESULT;}
            }else{errorCode = ERROR_NO_SUCH_MATCH;}
        }else {errorCode = ERROR_NO_SUCH_USER;}
        if(errorCode != null){
           basicResponse = new BasicResponse(success,errorCode);}
        return basicResponse;
    }

    @RequestMapping(value = "get-betting")
    public List<Bet> getBettingOfUser (String secret){
        return persist.getBetting(secret);
    }

    @RequestMapping(value = "update-balance" )
    public BasicResponse updateBalance(String secret, double balanceToAdd){
        BasicResponse basicResponse = null;
        Integer errorCode = null;
        boolean success = false;
        if(balanceToAdd>=0) {
            User user = persist.getUserBySecret(secret);
            if(user!=null){
                success = true;
                persist.updateBalance(balanceToAdd,secret);
                basicResponse = new BalanceResponse(true,null,persist.getUserBySecret(secret));
            }else{errorCode = ERROR_NO_SUCH_USER;}
        }else{errorCode = ERROR_NO_VALID_SUM;}
        if(errorCode!=null){
         basicResponse = new BasicResponse(success,errorCode);}
        return basicResponse;
    }

    private boolean isStrongPassword (String password){
        return password.length() >= 6;
    }
    private boolean isValidEmail (String email){
        return email.contains("@");
    }

    private boolean isValidResult (int result){
        return result >= 0 && result <=2;
    }

    private boolean isValidSum(int sum, double balance){
        return sum <= balance && sum % 5 == 0;
    }

    private boolean isAvailableMatch (Match match) throws ParseException {
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yy H:mm:ss");
        Date currentDate =simpleDateFormat.parse (match.getDate());
        return currentDate.after(today);
    }

    private double winRatioInput(int result,Match match){
        double winRatio = match.getOddsDraw();
        if (result == 1)
            winRatio = match.getOddsTeam1();;
        if( result ==2)
            winRatio = match.getOddsTeam2();

        return winRatio;
    }
}





