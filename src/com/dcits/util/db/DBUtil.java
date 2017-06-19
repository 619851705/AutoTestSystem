package com.dcits.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBUtil {
	private static Logger logger = Logger.getLogger(DBUtil.class);
	
	/**
	 * 使用JDBC建立数据库连接
	 * 目前支持mysql和oracle
	 * @param dbType
	 * @param dbUrl
	 * @param dbName
	 * @param dbUserName
	 * @param dbPasswd
	 * @return
	 */
	public static Connection getConnection(String dbType,String dbUrl,String dbName,String  dbUserName,String dbPasswd){
   	 Connection con=null;
   	 try {
   		 
   		 if(dbType.equals("oracle")){
   			Class.forName("oracle.jdbc.driver.OracleDriver");
   			con = DriverManager.getConnection("jdbc:oracle:thin:@"+dbUrl+":"+dbName, dbUserName, dbPasswd);
   		 }else if(dbType.equals("mysql")){
   			Class.forName("com.mysql.jdbc.Driver");
   			con = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/"+dbName+"?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true", dbUserName, dbPasswd);
   		 }else{
   			logger.info("不支持的数据库类型,无法连接");
   			return null;
   		 }			
		} catch (Exception e) {
			logger.error(dbUrl+","+dbName+"建立查询数据库连接失败:"+e.toString());
			e.printStackTrace();
		}
   	 return con;
    }
	
	
    public static void close(Connection con)throws SQLException{
   	 if(con!=null){
   		 try {
				con.close();
			} catch (SQLException e) {				
				e.printStackTrace();
				logger.error("关闭查询数据库异常:"+e.toString());
				throw e;
			}
   	 }
    }
    
    /**
     * 传入数据库连接和要执行的sql语句
     * 得到返回值,多个值只取第一个,没有值返回null
     * @param con
     * @param sqlStr
     * @return
     */
    public static String getDBData(Connection con,String sqlStr){
    	String returnStr = null;
    	PreparedStatement ps=null;
    	ResultSet rs=null;
    	try {
    		ps=con.prepareStatement(sqlStr);
    		rs=ps.executeQuery();
    		while(rs.next()){
    			//只取第一条记录
    			returnStr = rs.getString(1);
    			break;
    		}   		
		} catch (Exception e) {			
			logger.error("查询语句执行失败["+sqlStr+"]:"+e.toString());
			e.printStackTrace();
		}finally{
			try {
				close(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	return returnStr;
    }
}
