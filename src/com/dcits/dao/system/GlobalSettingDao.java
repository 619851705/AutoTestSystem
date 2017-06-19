package com.dcits.dao.system;

import org.springframework.stereotype.Repository;

import com.dcits.bean.system.GlobalSetting;
import com.dcits.dao.message.BaseDao;

@Repository
public class GlobalSettingDao extends BaseDao<GlobalSetting>{
	
	public void edit(String settingName,String settingValue){
		String hql = "update GlobalSetting g set g.settingValue=:settingValue where g.settingName=:settingName";
		getSession().createQuery(hql).setString("settingValue", settingValue).setString("settingName",settingName).executeUpdate();
	}

}
