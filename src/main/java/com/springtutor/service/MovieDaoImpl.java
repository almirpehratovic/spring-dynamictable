package com.springtutor.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springtutor.model.Movie;

@Service("movieDao")
@Transactional
public class MovieDaoImpl implements MovieDao {

	private SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery("select m from Movie m").list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAll(int first, int size, String id, String name,
			String description, String orderBy) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(
				Movie.class);
		
		if (id != null && id.trim().length() > 0) {
			try {
				cr.add(Restrictions.eq("id", Integer.parseInt(id)));
			} catch (Exception e) {
				
			}
			
		}

		if (name != null && name.trim().length() > 0) {
			cr.add(Restrictions.ilike("name", name));
		}

		if (description != null && description.trim().length() > 0) {
			cr.add(Restrictions.ilike("description", description));
		}
		
		if (orderBy != null && orderBy.trim().length() > 0)
			setOrder(cr, orderBy);
		
		if (size != 0) {
			cr.setFirstResult(first - 1);
			cr.setMaxResults(size);
		}
		
		return cr.list();
	}

	@Override
	@Transactional(readOnly = true)
	public Movie findById(int id) {
		return (Movie) sessionFactory.getCurrentSession()
				.createQuery("select m from Movie m where m.id=:id")
				.setParameter("id", id).uniqueResult();
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

}
