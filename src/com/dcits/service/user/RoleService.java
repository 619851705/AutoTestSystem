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
	 * �������еĽ�ɫ
	 * @return
	 */
	public List<Role> findAll(){
		return dao.findAll();
	}
	
	/**
	 * ����ID����ָ���Ľ�ɫ��Ϣ
	 * @param roleId
	 * @return
	 */
	public Role get(Integer roleId){
		return dao.get(roleId);
	}
	
	/**
	 * ɾ��һ����ɫ,����ɾ��Ԥ�õ�admin��ɫ,ɾ����ɫ���ᵼ����������û���ɫΪ�ն�����ִ���κβ���
	 * @param roleId
	 */
	public void del(Integer roleId){
		//ɾ����ɫ,�ý�ɫ�µ��û������default��ɫ
		dao.changeUserRole(roleId);
		dao.delete(roleId);
	}
	
	/**
	 * ɾ����������һ����ɫ
	 * @param role
	 */
	public void edit(Role role){
		dao.edit(role);
	}
	
	/**
	 * ����roleName���ҽ�ɫ
	 * @param roleName
	 * @return
	 */
	public Role findRoleByName(String roleName){
		return dao.get(roleName);
	}
}
