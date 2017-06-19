package com.dcits.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.system.GlobalSetting;
import com.dcits.dao.system.GlobalSettingDao;

@Service
public class GlobalSettingService {
	@Autowired
	private GlobalSettingDao dao;
	
	
	
	/**
	 * ��ȡ����ȫ��������
	 * @return
	 */
	public List<GlobalSetting> findAll(){
		return dao.findAll();
	}
	
	/**
	 * ���µ���ȫ��������
	 * @param settingName
	 * @param settingValue
	 */
	public void updateSetting(String settingName,String settingValue){
		dao.edit(settingName, settingValue);
	}
}
