package com.ashcollege;


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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            return  this.sessionFactory.getCurrentSession().createQuery("FROM User ").list();
    }
    public <T> List<T> loadTeams(Class<T> clazz) {
        return  this.sessionFactory.getCurrentSession().createQuery("FROM Team ").list();
    }

    public <T> List<T> loadMatches(Class<T> clazz) {
        return  this.sessionFactory.getCurrentSession().createQuery("FROM Match ").list();
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
    public User getUserBySecret (String secret) {
        User user =(User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE secret = :secret ")
                .setParameter("secret", secret)
                .setMaxResults(1)
                .uniqueResult();
        return  user;
    }

    public List<Match> getMatchesByRound(int roundId){
        return this.sessionFactory.getCurrentSession().createQuery( "FROM Match WHERE roundNum = :roundId")
                .setParameter("roundId", roundId)
                .list();
    }

    public List<Team> getAllTeams() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Team ").list();
    }

    public List<Map<String, Object>> getAllMatches(){
        return this.sessionFactory.getCurrentSession().createQuery( "SELECT m.id, t1.name, t2.name, m.date" +
                "            FROM Match m " +
                "            JOIN Team t1 ON m.team1 = t1.id"  +
                "            JOIN Team t2 ON m.team2= t2.id "+
                "            ORDER BY m.date", Object[].class)
                .getResultList()
                .stream()
                .map(result -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", result[0]);
                    map.put("team1", result[1]);
                    map.put("team2", result[2]);
                    map.put("date", result[3]);
                    return map;
                })
                .collect(Collectors.toList());

    }
    public void update(User user){
        this.sessionFactory.getCurrentSession()
                .createQuery("UPDATE User SET username = :username, password = :password, email = :email WHERE secret = :secret")
                .setParameter("username", user.getUsername())
                .setParameter("password", user.getPassword())
                .setParameter("email", user.getEmail())
                .setParameter("secret", user.getSecret())
                .executeUpdate();

    }


}