package com.dcits.dao.user;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.dcits.bean.user.User;
import com.dcits.dao.message.BaseDao;

@Repository
public class UserDao extends BaseDao<User>{
	
	/**
	 * ƥ����е�½���û�
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public User login(String userName,String passWord){
		// ����HQL���
		Query query = getSession()
				.createQuery(
						"from User u where u.username=:userName and u.password=:passWord");
		// ����HQL����еĲ���
		query.setString("userName", userName);
		query.setString("passWord", passWord);
		// ִ��HQL���
		return (User) query.uniqueResult();
	}
	
	
	public void resetPasswd(Integer userId,String passwd){
		String hql = "update User u set u.password=:passwd where u.userId=:userId";
		getSession().createQuery(hql).setString("passwd", passwd).setInteger("userId", userId).executeUpdate();
		
	}
	
	public void lockUser(Integer userId,String status){
		String hql = "update User u set u.status=:status where u.userId=:userId";
		getSession().createQuery(hql).setInteger("userId", userId).setString("status",status).executeUpdate();
	}
	
	public User validateUsername(String username){		
		String hql = "From User u where u.username=:username";
		return (User) getSession().createQuery(hql).setString("username", username).uniqueResult();
	}
	
	public User validateUsername(String username,Integer userId){
		String hql = "From User u where u.username=:username and u.userId<>:userId";
		return (User) getSession().createQuery(hql).setString("username", username).setInteger("userId", userId).uniqueResult();
	}
	
	public void updateRealName(String realName,Integer userId){
		String  hql = "update User u set u.realName=:realName where u.userId=:userId";
		getSession().createQuery(hql).setString("realName", realName).setInteger("userId",userId).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findByRealName(String realName){
		String hql = "From User u where u.realName like :realName";
		return getSession().createQuery(hql).setString("realName","%"+realName+"%").list();
	}
}
