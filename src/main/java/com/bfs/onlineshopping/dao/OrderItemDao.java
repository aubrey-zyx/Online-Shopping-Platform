package com.bfs.onlineshopping.dao;

import com.bfs.onlineshopping.domain.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderItemDao extends AbstractHibernateDao<OrderItem> {
    public OrderItemDao() {
        setClazz(OrderItem.class);
    }

    public List<OrderItem> getItemsByOrderId(Long orderId) {
        TypedQuery<OrderItem> query = getCurrentSession().createQuery("FROM OrderItem oi WHERE oi.order.orderId = :orderId", OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
