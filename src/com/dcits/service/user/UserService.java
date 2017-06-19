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
	 * 查找当前所有用户包括正常状态和锁定状态
	 * @return
	 */
	public List<User> findAll(){
		return userDao.findAll();
	}

	
	/**
	 * 登陆
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public User findUserBylogin(String userName,String passWord){
		return userDao.login(userName, passWord);
	}
	
	/**
	 * 保存新增用户
	 * @param user
	 */
	public void saveUser(User user){
		userDao.save(user);
	}
	
	/**
	 * 更新用户或者新增用户
	 * @param user
	 */
	public void updateUser(User user){
		userDao.edit(user);
	}
	
	/**
	 * 删除指定用户
	 * @param userId
	 */
	public void delUser(Integer userId){
		userDao.delete(userId);
	}
	
	/**
	 * 重置指定用户的密码,默认为111111
	 * @param userId
	 */
	public void resetPasswd(Integer userId,String newPasswd){
		userDao.resetPasswd(userId,newPasswd);
	}
	
	/**
	 * 获取指定用户的信息
	 * @param userId
	 * @return
	 */
	public User getUserInfo(Integer userId){
		return userDao.get(userId);
	}
	
	/**
	 * 根据传入的status锁定或者解锁用户
	 * @param userId
	 * @param status
	 */
	public void lockedUser(Integer userId,String status){
		userDao.lockUser(userId, status);
	}
	
	/**
	 * 验证username的唯一性
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
	 * 更新指定用户/登录用户的真实姓名
	 * @param realName
	 * @param userId
	 */
	public void editMyName(String realName,Integer userId){
		userDao.updateRealName(realName, userId);
	}
	
	/**
	 * 根据真实姓名查询用户
	 * 模糊查询
	 * @param realName
	 * @return
	 */
	public List<User> findByRealName(String realName){
		return userDao.findByRealName(realName);
	}
}
