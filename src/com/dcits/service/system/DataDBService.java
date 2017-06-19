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
	 * �������п��õ����ݿ�
	 * @return
	 */
	public List<DataDB> findAll(){
		return dao.findAll();
	}
	
	/**
	 * ��ȡָ�������ݿ���Ϣ
	 * @param dbId
	 * @return
	 */
	public DataDB getDB(Integer dbId){
		return dao.get(dbId);				
	}
	
	/**
	 * �޸Ļ����������ݿ���Ϣ
	 * @param db
	 */
	public void editDBInfo(DataDB db){
		dao.edit(db);
	}
	
	/**
	 * ͨ��save��������������Ϣ
	 * @param db
	 * @return
	 */
	public Integer saveDBInfo(DataDB db){
		return dao.save(db);
	}
	
	/**
	 * ɾ��ָ�������ݿ���Ϣ
	 * @param dbId
	 */
	public void delDBInfo(Integer dbId){
		dao.delete(dbId);
	}
	
	/**
	 * �Զ���dbId
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
