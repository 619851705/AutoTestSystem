package com.dcits.action.system;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.system.DataDB;
import com.dcits.service.system.DataDBService;
import com.dcits.util.db.DBUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class DataDBAction extends ActionSupport implements ModelDriven<DataDB>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DataDBService service;
	//ajax���÷��ص�map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer dbId;
	
	private DataDB db = new DataDB();
	
	//չʾ����
	public String list(){
		List<DataDB> dbs = service.findAll();
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", dbs);
		return SUCCESS;
	}
	
	//ɾ��
	public String del(){
		service.delDBInfo(dbId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//���ӻ��߱༭
	public String edit(){
		if(db.getDbId()==null){
			//����
			db.setDbId(service.getMaxDBId());
		}
		service.editDBInfo(db);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//����ָ����Ϣ
	public String get(){
		DataDB db = service.getDB(dbId);
		jsonMap.put("returnCode", 0);
		jsonMap.put("dataDB", db);		
		return SUCCESS;
	}
	
	//�����Ƿ��������
	public String testDB() throws SQLException{
		DataDB db = service.getDB(dbId);
		Connection conn = DBUtil.getConnection(db.getDbType(), db.getDbUrl(), db.getDbName(), db.getDbUsername(), db.getDbPasswd());
		if(conn!=null){
			jsonMap.put("returnCode", 0);
			DBUtil.close(conn);
		}else{
			jsonMap.put("returnCode", 2);
		}		
		return SUCCESS;
	}
	////////////////////////////////GET-SET//////////////////////////////////////////
	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	@JSON(serialize=false)
	@Override
	public DataDB getModel() {
		// TODO Auto-generated method stub
		return db;
	}
}
