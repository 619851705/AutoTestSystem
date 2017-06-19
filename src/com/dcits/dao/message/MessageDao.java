package com.dcits.dao.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.Message;

@Repository
public class MessageDao extends BaseDao<Message> {
	
	//��֤�ñ���״̬Ϊ�����Ҷ�Ӧ�Ľӿ�ҲΪ����
	@SuppressWarnings("unchecked")
	@Override
	public List<Message> findAll() {
		List<Message> msgs=new ArrayList<Message>();
		String hsql="From Message m where m.interfaceInfo.status<>'2' and m.status<>'2'";
		msgs=getSession().createQuery(hsql).list();
		return msgs;
		
	}
	
	
	
}
