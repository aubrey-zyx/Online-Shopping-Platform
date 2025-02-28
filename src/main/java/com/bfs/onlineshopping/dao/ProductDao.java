package com.bfs.onlineshopping.dao;

import com.bfs.onlineshopping.domain.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductDao extends AbstractHibernateDao<Product> {
    public ProductDao() {
        setClazz(Product.class);
    }

    public List<Product> getAllProducts() {
        TypedQuery<Product> query = getCurrentSession().createQuery(
                "FROM Product", Product.class);
        return query.getResultList();
    }

    public List<Product> getAvailableProducts() {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Product.class);
        criteria.add(Restrictions.gt("quantity", 0));
        return criteria.list();
    }

    public Product getProductById(Long id) {
        return getCurrentSession().find(Product.class, id);
    }

    public void update(Product product) {
        getCurrentSession().update(product);
    }
}
