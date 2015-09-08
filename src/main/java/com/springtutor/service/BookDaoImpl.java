package com.springtutor.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springtutor.model.Book;

@Transactional
@Service("bookDao")
public class BookDaoImpl implements BookDao{
	private SessionFactory sessionFactory;
	
	@Override
	public List<Book> findAll(int first, int size, String title,
			String description, String orderBy) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Book.class);
		
		if (title != null && title.trim().length() > 0) {
			cr.add(Restrictions.ilike("title", title));
		}

		if (description != null && description.trim().length() > 0) {
			cr.add(Restrictions.ilike("description", description));
		}
		
		if (orderBy != null && orderBy.trim().length() > 0) {
			setOrder(cr, orderBy);
		}
		
		if (size != 0) {
			cr.setFirstResult(first-1);
			cr.setMaxResults(size);
		}
		
		return cr.list();
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void setOrder(Criteria criteria, String orderBy) {
		// id asc,name desc
		System.out.println("ABC setOrder metod( " + orderBy);
		for (String ord : orderBy.split(",")) {
			try {
				// id asc
				String[] clause = ord.split(" ");
				if (clause[1].toLowerCase().equals("asc")) {
					System.out.println("ABC Order by " + clause[0] + " asc");
					criteria.addOrder(Order.asc(clause[0]));
				} else if (clause[1].toLowerCase().equals("desc")) {
					System.out.println("ABC Order by " + clause[0] + " desc");
					criteria.addOrder(Order.desc(clause[0]));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	@Override
	public Book findById(int id) {
		return (Book) sessionFactory.getCurrentSession().createQuery("select b from Book b where id=:id").setParameter("id",id).uniqueResult();
	}
	
}
