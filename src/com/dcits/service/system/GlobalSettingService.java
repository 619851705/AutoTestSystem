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
	 * 获取所有全局设置项
	 * @return
	 */
	public List<GlobalSetting> findAll(){
		return dao.findAll();
	}
	
	/**
	 * 更新单个全局设置项
	 * @param settingName
	 * @param settingValue
	 */
	public void updateSetting(String settingName,String settingValue){
		dao.edit(settingName, settingValue);
	}
}
