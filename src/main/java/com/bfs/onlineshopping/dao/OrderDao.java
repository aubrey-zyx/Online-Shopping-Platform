package com.bfs.onlineshopping.dao;

import com.bfs.onlineshopping.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractHibernateDao<Order> {
    public OrderDao() {
        setClazz(Order.class);
    }

    public List<Order> getOrdersByUsername(String username) {
        TypedQuery<Order> query = getCurrentSession().createQuery(
                "FROM Order o WHERE o.user.username = :username", Order.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    public Order getOrderById(Long orderId) {
        return getCurrentSession().find(Order.class, orderId);
    }

    public void update(Order order) {
        getCurrentSession().update(order);
    }
}
