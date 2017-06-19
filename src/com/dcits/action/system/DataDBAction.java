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
	//ajax调用返回的map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer dbId;
	
	private DataDB db = new DataDB();
	
	//展示所有
	public String list(){
		List<DataDB> dbs = service.findAll();
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", dbs);
		return SUCCESS;
	}
	
	//删除
	public String del(){
		service.delDBInfo(dbId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//增加或者编辑
	public String edit(){
		if(db.getDbId()==null){
			//新增
			db.setDbId(service.getMaxDBId());
		}
		service.editDBInfo(db);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//查找指定信息
	public String get(){
		DataDB db = service.getDB(dbId);
		jsonMap.put("returnCode", 0);
		jsonMap.put("dataDB", db);		
		return SUCCESS;
	}
	
	//测试是否可以连接
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
