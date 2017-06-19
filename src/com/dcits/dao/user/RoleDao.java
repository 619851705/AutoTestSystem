package com.dcits.dao.user;

import org.springframework.stereotype.Repository;

import com.dcits.bean.user.Role;
import com.dcits.dao.message.BaseDao;

@Repository
public class RoleDao extends BaseDao<Role> {
	
	public Role get(String roleName){
		String hql = "From Role r where r.roleName=:roleName";
		return (Role) getSession().createQuery(hql).setString("roleName", roleName).setFirstResult(0).setMaxResults(1).uniqueResult();
	}
	
	public void changeUserRole(int roleId){
		String sql = "update at_user set role_id=3 where role_id="+roleId;
		getSession().createSQLQuery(sql).executeUpdate();
	}
}
