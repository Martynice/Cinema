package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.MovieSessionDao;
import com.dev.cinema.exceptions.DataProcessingException;
import com.dev.cinema.model.MovieSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Log4j
public class MovieSessionDaoImpl implements MovieSessionDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public MovieSessionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        log.info("Trying to find available movie sessions");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM MovieSession ms "
                            + "JOIN FETCH ms.cinemaHall "
                            + "JOIN FETCH ms.movie "
                            + "WHERE movie_id = :movieId "
                            + "AND showTime BETWEEN :startTime AND :endTime", MovieSession.class)
                    .setParameter("movieId", movieId)
                    .setParameter("startTime", date.atStartOfDay())
                    .setParameter("endTime", date.atTime(LocalTime.MAX))
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get available sessions with id = " + movieId
                    + " and date = " + date, e);
        }
    }

    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(movieSession);
            transaction.commit();
            log.info("Movie session was successfully added " + movieSession);
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert movie session " + movieSession, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
