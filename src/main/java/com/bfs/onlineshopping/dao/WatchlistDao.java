package com.bfs.onlineshopping.dao;

import com.bfs.onlineshopping.domain.Product;
import com.bfs.onlineshopping.domain.User;
import com.bfs.onlineshopping.domain.Watchlist;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class WatchlistDao extends AbstractHibernateDao<Watchlist> {
    public WatchlistDao() {
        setClazz(Watchlist.class);
    }

    public boolean existsByUserAndProduct(User user, Product product) {
        TypedQuery<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(w) FROM Watchlist w WHERE w.user = :user AND w.product = :product", Long.class);
        query.setParameter("user", user);
        query.setParameter("product", product);
        return query.getSingleResult() > 0;
    }

    public void removeByUserAndProduct(User user, Product product) {
        getCurrentSession().createQuery(
                        "DELETE FROM Watchlist w WHERE w.user = :user AND w.product = :product")
                .setParameter("user", user)
                .setParameter("product", product)
                .executeUpdate();
    }

    public List<Watchlist> findByUser(User user) {
        TypedQuery<Watchlist> query = getCurrentSession().createQuery(
                "FROM Watchlist w WHERE w.user = :user", Watchlist.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}

