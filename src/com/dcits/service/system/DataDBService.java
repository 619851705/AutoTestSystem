package com.dcits.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.system.DataDB;
import com.dcits.dao.system.DataDBDao;


@Service
public class DataDBService {
	
	@Autowired
	private DataDBDao dao;
	
	/**
	 * 查找所有可用的数据库
	 * @return
	 */
	public List<DataDB> findAll(){
		return dao.findAll();
	}
	
	/**
	 * 获取指定的数据库信息
	 * @param dbId
	 * @return
	 */
	public DataDB getDB(Integer dbId){
		return dao.get(dbId);				
	}
	
	/**
	 * 修改或者增加数据库信息
	 * @param db
	 */
	public void editDBInfo(DataDB db){
		dao.edit(db);
	}
	
	/**
	 * 通过save方法保存新增信息
	 * @param db
	 * @return
	 */
	public Integer saveDBInfo(DataDB db){
		return dao.save(db);
	}
	
	/**
	 * 删除指定的数据库信息
	 * @param dbId
	 */
	public void delDBInfo(Integer dbId){
		dao.delete(dbId);
	}
	
	/**
	 * 自定义dbId
	 * @return
	 */
	public Integer getMaxDBId(){
		DataDB db = dao.getMaxDBId();
		if(db==null){
			return 9999990;
		}else{
			return db.getDbId()+1;
		}
	}
}
