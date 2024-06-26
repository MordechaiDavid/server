package com.ashcollege;


import com.ashcollege.entities.Bet;
import com.ashcollege.entities.Match;
import com.ashcollege.entities.Team;
import com.ashcollege.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        String entityName = clazz.getSimpleName();
        return this.sessionFactory.getCurrentSession().createQuery("FROM " + entityName, clazz).list();
    }
    public User login (String username, String password) {
        try {
            return (User) this.sessionFactory.getCurrentSession().createQuery("FROM User WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean getUserByUserName (String username) {
        boolean exists = false;
        User user =(User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username = :username ")
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult();
        if (user != null)
            exists =true;
        return  exists;
    }
    public boolean checkUserByUserName (String username,String secret) {
        boolean exists =false;
        User user = (User)this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username = :username AND secret != :secret")
                .setParameter("username", username)
                .setParameter("secret", secret)
                .setMaxResults(1)
                .uniqueResult();
        if (user != null )
            exists =true;
        return  exists;
    }

    public User getUserBySecret (String secret) {
        User user =(User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE secret = :secret ")
                .setParameter("secret", secret)
                .setMaxResults(1)
                .uniqueResult();
        return  user;
    }

    public Match getMatchesById(int id){
        Match match = (Match) this.sessionFactory.getCurrentSession().createQuery( "FROM Match WHERE id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        return  match;
    }

    public List<Team> getAllTeams() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Team t ORDER BY  t.score DESC ,t.goalsScored-t.goalsConcedes DESC ,t.goalsScored DESC ,t.name").list();
    }

    public List<Match> getMatchesByType(String matchType) {
        List<Match> allMatches = loadList(Match.class);
        List<Match> resultMatches = new ArrayList<>();
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yy H:mm:ss");

        for (Match match : allMatches) {
            try {
                Date matchDate = simpleDateFormat.parse(match.getDate());
                long thirtySecondsInMillis = 30 * 1000;
                Date matchLiveDate = new Date(matchDate.getTime() + thirtySecondsInMillis);

                if (matchType.equals("available") && matchDate.after(today)) {
                    resultMatches.add(match);
                } else if (matchType.equals("live") && matchLiveDate.after(today)) {
                    resultMatches.add(match);
                } else if (matchType.equals("old") && matchLiveDate.before(today)) {
                    resultMatches.add(match);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMatches;
    }

    public List <Bet> getBetting (String secret){
        User user = getUserBySecret(secret);
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Bet WHERE gambler = :user" , Bet.class)
                 .setParameter("user", user)
                 .list();
    }
    public List <Bet> getBettingOnGame (int gameId){
        Match match = getMatchesById(gameId);
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Bet WHERE match = :match" , Bet.class)
                .setParameter("match", match)
                .list();
    }
    public void updateUser(User user){
        try{
            this.sessionFactory.getCurrentSession()
                    .createQuery("UPDATE User SET username = :username, password = :password, email = :email WHERE secret = :secret")
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .setParameter("email", user.getEmail())
                    .setParameter("secret", user.getSecret())
                    .executeUpdate();}
        catch (Exception e){
            e.printStackTrace();}

    }

    public void updateBalance(double sumOfBet, String secret){
        try {
            this.sessionFactory.getCurrentSession()
                    .createQuery("UPDATE User SET balance = balance + :sumOfBet WHERE secret = :secret")
                    .setParameter("sumOfBet", sumOfBet)
                    .setParameter("secret", secret)
                    .executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}