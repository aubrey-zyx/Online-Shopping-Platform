package com.bfs.onlineshopping.dao;

import com.bfs.onlineshopping.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class UserDao extends AbstractHibernateDao<User> {
    public UserDao() {
        setClazz(User.class);
    }

    public Optional<User> loadUserByUsername(String username) {
        TypedQuery<User> query = getCurrentSession().createQuery("FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    public void saveUser(User user) {
        add(user);
    }
}
