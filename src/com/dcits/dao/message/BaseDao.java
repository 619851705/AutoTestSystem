package com.dcits.dao.message;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.bean.message.PageModel;



/**
 * 泛型通用DAO
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BaseDao<T> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	private Class<T> clazz;
	

	public BaseDao() {
		//反射获取传入的类
		ParameterizedType type=(ParameterizedType)this.getClass().getGenericSuperclass();
		this.clazz=(Class<T>)type.getActualTypeArguments()[0];		
	}
	
	public Integer save(T entity) {
		// TODO Auto-generated method stub
		Integer id=(Integer) getSession().save(entity);
		return id;
	}

	public void delete(int id) {
		// TODO Auto-generated method stub
		getSession().delete(get(id));
	}

	public void edit(T entity) {
		// TODO Auto-generated method stub
		getSession().merge(entity);
	}

	
	public T get(Integer id) {
		// TODO Auto-generated method stub
		
		return (T)getSession().get(clazz, id);
	}

	public T load(Integer id) {
		// TODO Auto-generated method stub
		return (T)getSession().load(clazz, id);
	}

	public List<T> findAll() {
		// TODO Auto-generated method stub
		String hsql="select t from "+clazz.getSimpleName()+" t";
		
		return getSession().createQuery(hsql).list();
	}

	public int totalCount() {
		// TODO Auto-generated method stub
		int count=0;
		String hql="select count(t) from "+clazz.getSimpleName()+" t";
		Long temp=(Long)getSession().createQuery(hql).uniqueResult();
		if(temp!=null){
			count=temp.intValue();
		}
		return count;
	}

	public PageModel<T> findByPager(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		PageModel<T> pm=new PageModel<T>(pageNo, pageSize);
		String hql="select t from "+clazz.getSimpleName()+" t";
		pm.setDatas(getSession().createQuery(hql).setFirstResult(pageNo).setMaxResults(pageSize).list());
		pm.setRecordCount(totalCount());
		return pm;
	}

	public void update(String sql) {
		// TODO Auto-generated method stub
		getSession().createQuery(sql).executeUpdate();
	}

	public T findUnique(String sql) {
		// TODO Auto-generated method stub
		
		return (T)getSession().createQuery(sql).uniqueResult();
	}
}
