package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.Message;
import com.dcits.dao.message.MessageDao;

@Service
public class MessageService {
	
	@Autowired
	private MessageDao messageDao;
	
	
	/**
	 * ��ȡ����״̬�ı����б�
	 * @return
	 */
	public List<Message> findAllMessages(){
		return messageDao.findAll();
	}
	
	/**
	 * �ı�״̬ 
	 * @param id
	 */
	public void changeStatus(int id,String status){
		String hsql="update Message t set t.status='"+status+"' where t.messageId="+id;
		messageDao.update(hsql);
	}
	
	/**
	 * ����
	 * @param msg
	 */
	public void saveMessage(Message msg){
		messageDao.save(msg);
	}
	
	/**
	 * ����ID��ȡ����
	 * @param messageId
	 * @return
	 */
	public Message getMessageById(Integer messageId){
		return messageDao.get(messageId);
	}
	
	/**
	 * �༭����
	 */
	public void editMessage(Message m){
		messageDao.edit(m);
	}
	
	/**
	 * ɾ������
	 * @param messageId
	 */
	public void delMessage(Integer messageId){
		messageDao.delete(messageId);
	}
}
