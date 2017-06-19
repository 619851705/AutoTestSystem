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
	 * ��ȡָ���û���δ���ʼ�����
	 * @param userId
	 * @return
	 */
	public int getNoReadNum(Integer userId){
		return dao.getNoReadNum(userId);
	}
	
	/**
	 * ��ȡָ���û����ռ��б�
	 * @param userId
	 * @return
	 */
	public List<UserMail> findReadMails(Integer userId){
		return dao.findReadMails(userId);
	}
	
	/**
	 * ��ȡָ���û��ķ����б�
	 * @param userId
	 * @return
	 */
	public List<UserMail> findSendMail(Integer userId){
		return dao.findSendMails(userId);
	}
	
	/**
	 * �ı�ָ���ʼ���ָ��״̬
	 * @param mailId
	 * @param statusName
	 * @param status
	 */
	public void changeStatus(Integer mailId,String statusName,String status){
		dao.changeStatus(mailId, statusName, status);
	}
	
	/**
	 * ��ȡָ����mail
	 * @param mailId
	 * @return
	 */
	public UserMail getMail(Integer mailId){
		return dao.get(mailId);
	}
	
	/**
	 * ���»��߱����µ�mail
	 * @param mail
	 */
	public void edit(UserMail mail){
		dao.edit(mail);
	}
	
	/**
	 * ���沢����ID
	 * @param mail
	 * @return
	 */
	public Integer save(UserMail mail){
		return dao.save(mail);
	}
	/**
	 * ɾ��ָ���ʼ�
	 * @param mailId
	 */
	public void del(Integer mailId){
		dao.delete(mailId);
	}
}
