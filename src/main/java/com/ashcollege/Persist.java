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

import java.util.List;


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

    public List<Match> getMatchesByRound(int roundId){
        return this.sessionFactory.getCurrentSession().createQuery( "FROM Match WHERE roundNum= :roundId")
                .setParameter("roundId", roundId)
                .list();
    }

    public List<Team> getAllTeams(){
        return this.sessionFactory.getCurrentSession().createQuery( "FROM Team ").list();
    }

    //הוספתי את זה רק כדי לבדוק צד לקוח שבניתי
    public List<Match> getAllMatches(){
        return this.sessionFactory.getCurrentSession().createQuery( "FROM Match order by date ").list();
    }



}