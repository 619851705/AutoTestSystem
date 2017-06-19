package com.dcits.test.message;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dcits.bean.message.SceneValidateRule;
import com.dcits.bean.message.TestConfig;
import com.dcits.bean.system.DataDB;
import com.dcits.util.GJsonFormatUtil;
import com.dcits.util.JsonUtil;
import com.dcits.util.PracticalUtils;
import com.dcits.util.db.DBUtil;

public class AutoTest {
	//执行测试的各种方法
	private static Logger logger = Logger.getLogger(AutoTest.class);
	//CallService方法,调用服务通过http协议
	public static Map<String,String> callService(String requestUrl,String requestJson,TestConfig config){
		Map<String,String> returnMap=new HashMap<String,String>();
		String responseJson="";
		String useTime="0";
		String runStatus="0";
		int statusCodeN=0;
		HttpURLConnection httpConn=null;
		try {
			URL url = new URL(requestUrl);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出   
			httpConn.setDoInput(true);// 使用 URL 连接进行输入   
			httpConn.setUseCaches(false);// 忽略缓存   
			httpConn.setConnectTimeout(config.getConnectTimeOut());//连接超时时间
			httpConn.setReadTimeout(config.getReadTimeOut());//读取超时时间
			httpConn.addRequestProperty("Content-type","application/json;charset=UTF-8");
			String method="POST";
/*			if(config.getHttpMethodFlag().equals("1")){
				method="GET";
			}*/
			httpConn.setRequestMethod(method);// 设置URL请求方法   
			
			
			byte[] bypes = requestJson.toString().getBytes();
			
			httpConn.getOutputStream().write(bypes);// 输入参数
			
			long beginTime=System.currentTimeMillis();
			statusCodeN=httpConn.getResponseCode();
			long endTime=System.currentTimeMillis();
			
			//判断http code
			if ( statusCodeN != 200){
				
			}else{
				InputStream in = httpConn.getInputStream();				
				//调用时间
				useTime=String.valueOf(endTime-beginTime);
				int iTotal = httpConn.getContentLength();
				byte[] bt = new byte[2048];
				int len = 0;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(
						iTotal == -1 ? 50 * 1024 : iTotal);
				
				while ((len = in.read(bt)) != -1) {
					
					bout.write(bt, 0, len);
					
				}
					
				//获取出参内容
				byte[] bta = bout.toByteArray();
				responseJson = new String(bta, "UTF-8");
				runStatus="0";
			}
			
		} catch (Exception e) {
			
			//e.printStackTrace();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//			e.printStackTrace(new PrintStream(baos));  
			logger.error("调用接口失败---地址:"+requestUrl+",报文:"+requestJson);
			logger.error(e.toString());
			responseJson = e.toString();
			e.printStackTrace();
			runStatus="2";
			
		}finally{
			if(httpConn!=null){
			//断开连接
			httpConn.disconnect();
			
			}
			returnMap.put("statusCode", String.valueOf(statusCodeN));
			returnMap.put("responseJson", responseJson);
			returnMap.put("useTime", useTime);	
			returnMap.put("runStatus",runStatus);
		}
		return returnMap;				
	}
	
	
	//判断返回报文的正确性
	//全局验证模式
	public static Map<String,String> CheckReturnDefault(String ResponseHtml,String validateStr){
		String code=CorreLation.GetReturnCode(ResponseHtml);
		Map<String,String> returnMap = new HashMap<String,String>();
		if(code==null){
			returnMap.put("status", "1");
			returnMap.put("msg", "返回报文中没有找到RETURN_CODE节点,建议使用自定义的验证规则");
			return returnMap;
		}
		String[] strs=validateStr.split(",");			
			for(String str:strs){
				if(code.equals(str)){
					returnMap.put("status", "0");
					returnMap.put("msg", "");
					return returnMap;
				}
			}
			returnMap.put("status", "1");
			returnMap.put("msg", "实际返回的RETURN_CODE节点值为："+code+",不符合预期结果");
			return returnMap;
	}
	
	//判断返回报文的正确性
	//全文验证规则模式
	public static Map<String,String> checkReturnFull(String ResponseHtml,String validateValue){
		Map<String,String> returnMap = new HashMap<String,String>();
		//转换格式化的json串
		String jsonR = GJsonFormatUtil.formatJsonStr(ResponseHtml);
		String jsonV = GJsonFormatUtil.formatJsonStr(validateValue);
		
		if(jsonR!=null&&jsonV!=null){
			if(jsonR.equals(jsonV)){
				returnMap.put("status", "0");
				returnMap.put("msg", "");
			}else{
				returnMap.put("status", "1");
				returnMap.put("msg", "返回报文和验证报文不匹配,请检查");
			}
		}else{
			returnMap.put("status", "1");
			returnMap.put("msg", "返回报文或者验证报文不是正确的json格式,请检查");
		}
		return returnMap;
	}
	
	//判断返回报文的正确性
	//参数验证模式
	//有报错就停止验证
	@SuppressWarnings("unchecked")
	public static Map<String,String> checkReturnParams(String ResponseHtml,String requestJson,List<SceneValidateRule> validateRules,Map<String,DataDB> dataDBS){
		Map<String,String> returnMap = new HashMap<String,String>();
		//入参和出参转换成map
		Map<String,String> requestDatas = (Map<String, String>) JsonUtil.getJsonList(requestJson, 2);
		Map<String,String> responseDatas = (Map<String, String>) JsonUtil.getJsonList(ResponseHtml, 2);
		
		if(requestDatas==null||responseDatas==null){
			returnMap.put("status", "1");
			returnMap.put("msg", "该场景的出参报文或者入参报文不是正确的Json格式,请检查!");
			return returnMap;
		}
		
		//开始验证,取status=0的验证规则
		String validateStr = null;
		for(SceneValidateRule rule:validateRules){
			if(rule.getStatus().equals("0")){
				//判断节点路径并取得出参报文中的对应节点值
				if(rule.getComplexFlag().equals("0")){
					validateStr = JsonUtil.getObjectByJson(ResponseHtml, rule.getParameterName(), JsonUtil.TypeEnum.string);
				}else{
					validateStr = responseDatas.get(rule.getParameterName());
				}
				if(validateStr==null){
					returnMap.put("status", "1");
					returnMap.put("msg", "在出参报文中没有获取到节点:"+rule.getParameterName()+"的值,验证失败,并且忽略接下来的验证规则,请检查!");
					return returnMap;
				}
				//获取比对值
				switch (rule.getGetValueMethod()) {
				case "0"://普通字符串
					if(!validateStr.equals(rule.getValidateValue())){
						returnMap.put("status", "1");
						returnMap.put("msg", "出参报文节点:"+rule.getParameterName()+"值为["+validateStr+"],与预期结果["+rule.getValidateValue()+"]不匹配,验证失败,请检查!");
						return returnMap;
					}
					break;
				case "1"://入参节点
					String comparisonStr = JsonUtil.getObjectByJson(ResponseHtml, rule.getValidateValue(), JsonUtil.TypeEnum.string);						
					if(comparisonStr==null){
						comparisonStr = requestDatas.get(rule.getValidateValue());
						if(comparisonStr==null){
							returnMap.put("status", "1");
							returnMap.put("msg", "获取预期验证值出错:在入参报文中获取不到指定节点"+rule.getValidateValue()+"的值,验证失败,请检查!");
							return returnMap;
						}
						if(!validateStr.equals(comparisonStr)){
							returnMap.put("status", "1");
							returnMap.put("msg", "出参报文节点:"+rule.getParameterName()+"值为["+validateStr+"],与预期结果入参报文"+rule.getValidateValue()+"节点值["+comparisonStr+"]不匹配,验证失败,请检查!");
							return returnMap;
						}
						
					}
					break;
				default://数据库取值
					DataDB dbInfo = dataDBS.get(rule.getGetValueMethod());
					if(dbInfo==null){
						returnMap.put("status", "1");
						returnMap.put("msg", "没有找到ID为"+rule.getGetValueMethod()+"的查询数据库信息,请检查该数据库信息是否存在？");
						return returnMap;
					}
					//获取数据库连接
					Connection conn = DBUtil.getConnection(dbInfo.getDbType(), dbInfo.getDbUrl(), dbInfo.getDbName(), dbInfo.getDbUsername(), dbInfo.getDbPasswd());
					if(conn==null){
						returnMap.put("status", "1");
						returnMap.put("msg", "获取查询数据库"+dbInfo.getDbMark()+"-"+dbInfo.getDbName()+"的连接出错,验证失败,请检查!");
						return returnMap;
					}
					//SQL语句替换
					String sqlStr = PracticalUtils.replaceSqlStr(rule.getValidateValue(), requestDatas, requestJson);
					//连接数据库查询
					String comparisonStr1 = DBUtil.getDBData(conn, sqlStr);
					if(!comparisonStr1.equals(validateStr)){
						returnMap.put("status", "1");
						returnMap.put("msg", "出参报文节点:"+rule.getParameterName()+"值为["+validateStr+"],与预期结果-数据库取值["+comparisonStr1+"]不匹配,验证失败,请检查!");
						return returnMap;
					}
					break;
				}
				
			}
		}
		returnMap.put("status", "0");
		returnMap.put("msg", "");
		return returnMap;
		
	}
}
