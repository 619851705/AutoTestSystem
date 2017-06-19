package com.dcits.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.user.UserMail;
import com.dcits.dao.user.UserMailDao;

@Service
public class UserMailService {
	
	@Autowired
	private UserMailDao dao;
	
	/**
	 * 获取指定用户的未读邮件数量
	 * @param userId
	 * @return
	 */
	public int getNoReadNum(Integer userId){
		return dao.getNoReadNum(userId);
	}
	
	/**
	 * 获取指定用户的收件列表
	 * @param userId
	 * @return
	 */
	public List<UserMail> findReadMails(Integer userId){
		return dao.findReadMails(userId);
	}
	
	/**
	 * 获取指定用户的发件列表
	 * @param userId
	 * @return
	 */
	public List<UserMail> findSendMail(Integer userId){
		return dao.findSendMails(userId);
	}
	
	/**
	 * 改变指定邮件的指定状态
	 * @param mailId
	 * @param statusName
	 * @param status
	 */
	public void changeStatus(Integer mailId,String statusName,String status){
		dao.changeStatus(mailId, statusName, status);
	}
	
	/**
	 * 获取指定的mail
	 * @param mailId
	 * @return
	 */
	public UserMail getMail(Integer mailId){
		return dao.get(mailId);
	}
	
	/**
	 * 更新或者保存新的mail
	 * @param mail
	 */
	public void edit(UserMail mail){
		dao.edit(mail);
	}
	
	/**
	 * 保存并返回ID
	 * @param mail
	 * @return
	 */
	public Integer save(UserMail mail){
		return dao.save(mail);
	}
	/**
	 * 删除指定邮件
	 * @param mailId
	 */
	public void del(Integer mailId){
		dao.delete(mailId);
	}
}
