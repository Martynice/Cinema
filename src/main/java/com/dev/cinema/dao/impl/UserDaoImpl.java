package com.dev.cinema.dao.impl;

import com.dev.cinema.dao.UserDao;
import com.dev.cinema.exceptions.DataProcessingException;
import com.dev.cinema.model.User;
import java.util.Optional;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@Log4j
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User add(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            log.info("User was successfully added " + user);
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Couldn't insert user " + user, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Trying to get user by email " + email);
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User u "
                    + "JOIN FETCH u.roles "
                    + "WHERE u.email = :email ", User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    @Override
    public User get(Long id) {
        log.info("Trying to get user with id " + id);
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }
}
