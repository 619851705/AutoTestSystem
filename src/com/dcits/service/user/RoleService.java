package com.dcits.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.user.Role;
import com.dcits.dao.user.RoleDao;

@Service
public class RoleService {
	@Autowired
	private RoleDao dao;
	
	/**
	 * 查找所有的角色
	 * @return
	 */
	public List<Role> findAll(){
		return dao.findAll();
	}
	
	/**
	 * 根据ID查找指定的角色信息
	 * @param roleId
	 * @return
	 */
	public Role get(Integer roleId){
		return dao.get(roleId);
	}
	
	/**
	 * 删除一个角色,不能删除预置的admin角色,删除角色将会导致相关联的用户角色为空而不能执行任何操作
	 * @param roleId
	 */
	public void del(Integer roleId){
		//删除角色,该角色下的用户变更成default角色
		dao.changeUserRole(roleId);
		dao.delete(roleId);
	}
	
	/**
	 * 删除或者新增一个角色
	 * @param role
	 */
	public void edit(Role role){
		dao.edit(role);
	}
	
	/**
	 * 根据roleName查找角色
	 * @param roleName
	 * @return
	 */
	public Role findRoleByName(String roleName){
		return dao.get(roleName);
	}
}
