package com.ashcollege.responses;

import com.ashcollege.entities.User;

public class BalanceResponse extends BasicResponse {
    private User user;

    public BalanceResponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }

    public BalanceResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
