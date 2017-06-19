package com.dcits.service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebConfig;
import com.dcits.dao.web.WebConfigDao;

@Service
public class WebConfigService {
	
	@Autowired
	private WebConfigDao webConfigDao;
	
	/**
	 * ����userId���Ҷ�Ӧ��web�Զ�����������
	 * @param userId
	 * @return
	 */
	public WebConfig findConfig(Integer userId){
		return webConfigDao.findByUserId(userId);
	}
	
	/**
	 * ���ӻ����޸ĸ���ָ��������
	 * @param config
	 */
	public void editConfig(WebConfig config){
		webConfigDao.edit(config);
	}
}
