package com.dcits.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.user.User;
import com.dcits.dao.user.UserDao;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	/**
	 * ���ҵ�ǰ�����û���������״̬������״̬
	 * @return
	 */
	public List<User> findAll(){
		return userDao.findAll();
	}

	
	/**
	 * ��½
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public User findUserBylogin(String userName,String passWord){
		return userDao.login(userName, passWord);
	}
	
	/**
	 * ���������û�
	 * @param user
	 */
	public void saveUser(User user){
		userDao.save(user);
	}
	
	/**
	 * �����û����������û�
	 * @param user
	 */
	public void updateUser(User user){
		userDao.edit(user);
	}
	
	/**
	 * ɾ��ָ���û�
	 * @param userId
	 */
	public void delUser(Integer userId){
		userDao.delete(userId);
	}
	
	/**
	 * ����ָ���û�������,Ĭ��Ϊ111111
	 * @param userId
	 */
	public void resetPasswd(Integer userId,String newPasswd){
		userDao.resetPasswd(userId,newPasswd);
	}
	
	/**
	 * ��ȡָ���û�����Ϣ
	 * @param userId
	 * @return
	 */
	public User getUserInfo(Integer userId){
		return userDao.get(userId);
	}
	
	/**
	 * ���ݴ����status�������߽����û�
	 * @param userId
	 * @param status
	 */
	public void lockedUser(Integer userId,String status){
		userDao.lockUser(userId, status);
	}
	
	/**
	 * ��֤username��Ψһ��
	 * @param userId
	 * @param username
	 * @return
	 */
	public User validateUsername(Integer userId,String username){
		if(userId==null){
			return userDao.validateUsername(username);
		}
		return userDao.validateUsername(username,userId);
	}
	
	/**
	 * ����ָ���û�/��¼�û�����ʵ����
	 * @param realName
	 * @param userId
	 */
	public void editMyName(String realName,Integer userId){
		userDao.updateRealName(realName, userId);
	}
	
	/**
	 * ������ʵ������ѯ�û�
	 * ģ����ѯ
	 * @param realName
	 * @return
	 */
	public List<User> findByRealName(String realName){
		return userDao.findByRealName(realName);
	}
}
