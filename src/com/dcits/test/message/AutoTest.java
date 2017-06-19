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
	//ִ�в��Եĸ��ַ���
	private static Logger logger = Logger.getLogger(AutoTest.class);
	//CallService����,���÷���ͨ��httpЭ��
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
			httpConn.setDoOutput(true);// ʹ�� URL ���ӽ������   
			httpConn.setDoInput(true);// ʹ�� URL ���ӽ�������   
			httpConn.setUseCaches(false);// ���Ի���   
			httpConn.setConnectTimeout(config.getConnectTimeOut());//���ӳ�ʱʱ��
			httpConn.setReadTimeout(config.getReadTimeOut());//��ȡ��ʱʱ��
			httpConn.addRequestProperty("Content-type","application/json;charset=UTF-8");
			String method="POST";
/*			if(config.getHttpMethodFlag().equals("1")){
				method="GET";
			}*/
			httpConn.setRequestMethod(method);// ����URL���󷽷�   
			
			
			byte[] bypes = requestJson.toString().getBytes();
			
			httpConn.getOutputStream().write(bypes);// �������
			
			long beginTime=System.currentTimeMillis();
			statusCodeN=httpConn.getResponseCode();
			long endTime=System.currentTimeMillis();
			
			//�ж�http code
			if ( statusCodeN != 200){
				
			}else{
				InputStream in = httpConn.getInputStream();				
				//����ʱ��
				useTime=String.valueOf(endTime-beginTime);
				int iTotal = httpConn.getContentLength();
				byte[] bt = new byte[2048];
				int len = 0;
				ByteArrayOutputStream bout = new ByteArrayOutputStream(
						iTotal == -1 ? 50 * 1024 : iTotal);
				
				while ((len = in.read(bt)) != -1) {
					
					bout.write(bt, 0, len);
					
				}
					
				//��ȡ��������
				byte[] bta = bout.toByteArray();
				responseJson = new String(bta, "UTF-8");
				runStatus="0";
			}
			
		} catch (Exception e) {
			
			//e.printStackTrace();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//			e.printStackTrace(new PrintStream(baos));  
			logger.error("���ýӿ�ʧ��---��ַ:"+requestUrl+",����:"+requestJson);
			logger.error(e.toString());
			responseJson = e.toString();
			e.printStackTrace();
			runStatus="2";
			
		}finally{
			if(httpConn!=null){
			//�Ͽ�����
			httpConn.disconnect();
			
			}
			returnMap.put("statusCode", String.valueOf(statusCodeN));
			returnMap.put("responseJson", responseJson);
			returnMap.put("useTime", useTime);	
			returnMap.put("runStatus",runStatus);
		}
		return returnMap;				
	}
	
	
	//�жϷ��ر��ĵ���ȷ��
	//ȫ����֤ģʽ
	public static Map<String,String> CheckReturnDefault(String ResponseHtml,String validateStr){
		String code=CorreLation.GetReturnCode(ResponseHtml);
		Map<String,String> returnMap = new HashMap<String,String>();
		if(code==null){
			returnMap.put("status", "1");
			returnMap.put("msg", "���ر�����û���ҵ�RETURN_CODE�ڵ�,����ʹ���Զ������֤����");
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
			returnMap.put("msg", "ʵ�ʷ��ص�RETURN_CODE�ڵ�ֵΪ��"+code+",������Ԥ�ڽ��");
			return returnMap;
	}
	
	//�жϷ��ر��ĵ���ȷ��
	//ȫ����֤����ģʽ
	public static Map<String,String> checkReturnFull(String ResponseHtml,String validateValue){
		Map<String,String> returnMap = new HashMap<String,String>();
		//ת����ʽ����json��
		String jsonR = GJsonFormatUtil.formatJsonStr(ResponseHtml);
		String jsonV = GJsonFormatUtil.formatJsonStr(validateValue);
		
		if(jsonR!=null&&jsonV!=null){
			if(jsonR.equals(jsonV)){
				returnMap.put("status", "0");
				returnMap.put("msg", "");
			}else{
				returnMap.put("status", "1");
				returnMap.put("msg", "���ر��ĺ���֤���Ĳ�ƥ��,����");
			}
		}else{
			returnMap.put("status", "1");
			returnMap.put("msg", "���ر��Ļ�����֤���Ĳ�����ȷ��json��ʽ,����");
		}
		return returnMap;
	}
	
	//�жϷ��ر��ĵ���ȷ��
	//������֤ģʽ
	//�б����ֹͣ��֤
	@SuppressWarnings("unchecked")
	public static Map<String,String> checkReturnParams(String ResponseHtml,String requestJson,List<SceneValidateRule> validateRules,Map<String,DataDB> dataDBS){
		Map<String,String> returnMap = new HashMap<String,String>();
		//��κͳ���ת����map
		Map<String,String> requestDatas = (Map<String, String>) JsonUtil.getJsonList(requestJson, 2);
		Map<String,String> responseDatas = (Map<String, String>) JsonUtil.getJsonList(ResponseHtml, 2);
		
		if(requestDatas==null||responseDatas==null){
			returnMap.put("status", "1");
			returnMap.put("msg", "�ó����ĳ��α��Ļ�����α��Ĳ�����ȷ��Json��ʽ,����!");
			return returnMap;
		}
		
		//��ʼ��֤,ȡstatus=0����֤����
		String validateStr = null;
		for(SceneValidateRule rule:validateRules){
			if(rule.getStatus().equals("0")){
				//�жϽڵ�·����ȡ�ó��α����еĶ�Ӧ�ڵ�ֵ
				if(rule.getComplexFlag().equals("0")){
					validateStr = JsonUtil.getObjectByJson(ResponseHtml, rule.getParameterName(), JsonUtil.TypeEnum.string);
				}else{
					validateStr = responseDatas.get(rule.getParameterName());
				}
				if(validateStr==null){
					returnMap.put("status", "1");
					returnMap.put("msg", "�ڳ��α�����û�л�ȡ���ڵ�:"+rule.getParameterName()+"��ֵ,��֤ʧ��,���Һ��Խ���������֤����,����!");
					return returnMap;
				}
				//��ȡ�ȶ�ֵ
				switch (rule.getGetValueMethod()) {
				case "0"://��ͨ�ַ���
					if(!validateStr.equals(rule.getValidateValue())){
						returnMap.put("status", "1");
						returnMap.put("msg", "���α��Ľڵ�:"+rule.getParameterName()+"ֵΪ["+validateStr+"],��Ԥ�ڽ��["+rule.getValidateValue()+"]��ƥ��,��֤ʧ��,����!");
						return returnMap;
					}
					break;
				case "1"://��νڵ�
					String comparisonStr = JsonUtil.getObjectByJson(ResponseHtml, rule.getValidateValue(), JsonUtil.TypeEnum.string);						
					if(comparisonStr==null){
						comparisonStr = requestDatas.get(rule.getValidateValue());
						if(comparisonStr==null){
							returnMap.put("status", "1");
							returnMap.put("msg", "��ȡԤ����ֵ֤����:����α����л�ȡ����ָ���ڵ�"+rule.getValidateValue()+"��ֵ,��֤ʧ��,����!");
							return returnMap;
						}
						if(!validateStr.equals(comparisonStr)){
							returnMap.put("status", "1");
							returnMap.put("msg", "���α��Ľڵ�:"+rule.getParameterName()+"ֵΪ["+validateStr+"],��Ԥ�ڽ����α���"+rule.getValidateValue()+"�ڵ�ֵ["+comparisonStr+"]��ƥ��,��֤ʧ��,����!");
							return returnMap;
						}
						
					}
					break;
				default://���ݿ�ȡֵ
					DataDB dbInfo = dataDBS.get(rule.getGetValueMethod());
					if(dbInfo==null){
						returnMap.put("status", "1");
						returnMap.put("msg", "û���ҵ�IDΪ"+rule.getGetValueMethod()+"�Ĳ�ѯ���ݿ���Ϣ,��������ݿ���Ϣ�Ƿ���ڣ�");
						return returnMap;
					}
					//��ȡ���ݿ�����
					Connection conn = DBUtil.getConnection(dbInfo.getDbType(), dbInfo.getDbUrl(), dbInfo.getDbName(), dbInfo.getDbUsername(), dbInfo.getDbPasswd());
					if(conn==null){
						returnMap.put("status", "1");
						returnMap.put("msg", "��ȡ��ѯ���ݿ�"+dbInfo.getDbMark()+"-"+dbInfo.getDbName()+"�����ӳ���,��֤ʧ��,����!");
						return returnMap;
					}
					//SQL����滻
					String sqlStr = PracticalUtils.replaceSqlStr(rule.getValidateValue(), requestDatas, requestJson);
					//�������ݿ��ѯ
					String comparisonStr1 = DBUtil.getDBData(conn, sqlStr);
					if(!comparisonStr1.equals(validateStr)){
						returnMap.put("status", "1");
						returnMap.put("msg", "���α��Ľڵ�:"+rule.getParameterName()+"ֵΪ["+validateStr+"],��Ԥ�ڽ��-���ݿ�ȡֵ["+comparisonStr1+"]��ƥ��,��֤ʧ��,����!");
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
