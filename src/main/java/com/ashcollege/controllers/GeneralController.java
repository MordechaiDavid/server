package com.ashcollege.controllers;

import com.ashcollege.Persist;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.utils.DbUtils;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ashcollege.utils.Errors.*;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class GeneralController {

    @Autowired
    private DbUtils dbUtils;

    @Autowired
    private Persist persist;




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
                errorCode = ERROR_SIGN_UP_NO_PASSWORD;
            }
        } else {
            errorCode = ERROR_SIGN_UP_NO_USERNAME;
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
    @RequestMapping(value = "create-account" , method = {RequestMethod.POST})
    public BasicResponse createAccount(String username, String password, String password1 ,String email) {
        BasicResponse basicResponse = null;
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
                        } else{errorCode=ERROR_SIGN_UP_USERNAME_TAKEN;}}
                        else {
                            errorCode = EMAIL_FORMAT_NOT_VALID ;}}
                    else{
                         errorCode =PASSWORD_IS_WEEK ;}}
                else {
                     errorCode = ERROR_SIGN_UP_PASSWORDS_DONT_MATCH;}}
            else {
                errorCode = ERROR_SIGN_UP_NO_PASSWORD;
            }}
        else errorCode = ERROR_SIGN_UP_NO_USERNAME;
         basicResponse  = new BasicResponse(success,errorCode);
         return basicResponse;

    }
    private boolean isStrongPassword (String password){
        boolean isStrong = false;
        if (password.length() >= 6) isStrong = true;
        return isStrong;
    }
    private boolean isValidEmail (String email){
        boolean isValid = false;
        if (email.contains("@")) isValid = true;
        return isValid;
    }

}





