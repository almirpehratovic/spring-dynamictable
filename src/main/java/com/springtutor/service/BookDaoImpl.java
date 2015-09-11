package com.springtutor.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.QueryException;
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
	public List<Book> find(int first, int size, String title,
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

	@Override
	public Book save(Book book) {
		sessionFactory.getCurrentSession().saveOrUpdate(book);
		return book;
	}

	@Override
	public void delete(Book book) {
		sessionFactory.getCurrentSession().delete(book);
		
	}

	@Override
	public List<Book> find(String HQL) throws QueryException{
		List<Book> books = books = new ArrayList<Book>();
		try {
			books = sessionFactory.getCurrentSession().createQuery("select o from Book o where " + HQL).list();
		} catch (QueryException e) {
			QueryException queryException = new QueryException("HQL Error. Valid properties are " + 
						getAvaliableQueryProperties(Book.class) + ". Error text " + e.getMessage());
			throw queryException;
		}
		return books;
	}
	
	private List<String> getAvaliableQueryProperties(Class<?> cls) {
		List<String> props = new ArrayList<String>();
		for (Method method : cls.getDeclaredMethods()) {
			if (method.getName().startsWith("get") && method.getName().length() > 3) {
				props.add("o." + method.getName().substring(3,4).toLowerCase() +  method.getName().substring(4));
			}
		}
		return props;
	}
	
}
