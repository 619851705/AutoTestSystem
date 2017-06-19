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
	 * 获取正常状态的报文列表
	 * @return
	 */
	public List<Message> findAllMessages(){
		return messageDao.findAll();
	}
	
	/**
	 * 改变状态 
	 * @param id
	 */
	public void changeStatus(int id,String status){
		String hsql="update Message t set t.status='"+status+"' where t.messageId="+id;
		messageDao.update(hsql);
	}
	
	/**
	 * 保存
	 * @param msg
	 */
	public void saveMessage(Message msg){
		messageDao.save(msg);
	}
	
	/**
	 * 根据ID获取报文
	 * @param messageId
	 * @return
	 */
	public Message getMessageById(Integer messageId){
		return messageDao.get(messageId);
	}
	
	/**
	 * 编辑保存
	 */
	public void editMessage(Message m){
		messageDao.edit(m);
	}
	
	/**
	 * 删除报文
	 * @param messageId
	 */
	public void delMessage(Integer messageId){
		messageDao.delete(messageId);
	}
}
