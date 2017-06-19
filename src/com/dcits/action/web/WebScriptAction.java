package com.dcits.action.web;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebScriptInfo;
import com.dcits.bean.web.WebScriptReport;
import com.dcits.service.web.WebScriptService;
import com.dcits.test.script.RunRubyScript;
import com.dcits.util.StrutsMaps;
import com.dcits.util.Upload;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class WebScriptAction extends ActionSupport implements ModelDriven<WebScriptInfo>{

	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WebScriptService service;
	
	private File file;
	    
    //�ύ������file������
    private String fileFileName;
    
    //�ύ������file��MIME����
    private String fileFileContentType;
	

	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private WebScriptInfo script = new WebScriptInfo();
	
	private Integer objId;
	
	private String scriptNames;
	
	private String scriptIds;
	
	private Integer fileKind;
	
	
	//�б�
	public String listScripts(){
		List<WebScriptInfo> scripts = service.findAllScripts(String.valueOf(fileKind));
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", scripts);
		return SUCCESS;
	}
	
	//�б�-���Ա���
	public String listReports(){
		List<WebScriptReport> reports = service.findAllReports();
		for(WebScriptReport r:reports){
			r.setTestUserName();
		}
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", reports);
		return SUCCESS;
	}
	
	//��ȡָ��script��Ϣ
	public String getScript(){
		WebScriptInfo s = service.getScript(script.getScriptId());
		jsonMap.put("returnCode", 0);
		jsonMap.put("webScript", s);
		return SUCCESS;
	}
	
	//�ű����ϴ�
	public String uploadScript() throws IOException{
		int returnCode = 0;
		String msg = "�ļ��ϴ��ɹ�!�뷵����д��Ϣ!";
		String ext = "";
        if(file == null) {
        	returnCode =2;
            msg = "û�з����ϴ����ļ�,����!";
        } else {
            ext = this.getFileFileName().substring(this.getFileFileName().lastIndexOf(".")).toLowerCase();
            if(".rb.feature".indexOf(ext) != -1) {
                Upload upload = new Upload();
                // �ļ����1Ϊ��ͨ�ű��ļ���2Ϊ�����ű��ļ���3Ϊ�����ļ�
                String fps = upload.singleUpload(file, this.getFileFileName(),fileKind);
                fps = fps.replace(",", "");
                if(!"".equals(fps)) {
                	returnCode = 0;
                    msg = "�ļ��ϴ��ɹ�!�뷵����д��Ϣ!";
                } else {
                	returnCode = 3;
                    msg = "�ļ��ϴ�ʧ��!���Ժ�����!";
                }
            } else {
            	returnCode = 4;
                msg = "�������ϴ������͵��ļ�!";
            }
        }	
        jsonMap.put("scriptPath", this.getFileFileName().substring(0,this.getFileFileName().lastIndexOf(".")));
        jsonMap.put("returnCode", returnCode);
        jsonMap.put("msg", msg);
        return SUCCESS;
	}
	
	
	//�ű���Ϣ�༭��������
	public String editScript(){
		if(script.getScriptId()==null){
			script.setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		service.editScript(script);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ���ű���Ϣ
	public String delScript(){			
		//ɾ���ļ�
		File fileRb = new File(StrutsMaps.getProjectPath()+"/files/rubyScript/"+script.getScriptPath()+".rb");
		
		if(fileRb.exists()){
			fileRb.delete();
		}
		
		if(script.getIfPublic().equals("1")){
			File fileFeature = new File(StrutsMaps.getProjectPath()+"/files/rubyScript/"+script.getScriptPath()+".feature");		
			if(fileFeature.exists()){
				fileFeature.delete();
			}
		}
		
		
		if(objId==null){
			objId = script.getScriptId();
		}
		service.delScript(objId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//ɾ��ָ�����Ա���
	public String delReport(){
		WebScriptReport report = service.getReport(objId);
		
		//ɾ���ļ�
		File fileReport = new File(StrutsMaps.getProjectPath()+"/files/rubyReport/"+report.getReportPath());
		
		if(fileReport.exists()){
			fileReport.delete();
		}
		
		service.delReport(objId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//���нű�
	public String rubScripts(){
		if(scriptNames==null||scriptNames.equals("")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "ָ������һ�����Խű�!");
		}
		//�����ű�
		String[] ss = scriptNames.split(",");
		String[] ids = scriptIds.split(",");
		//�����ű�
		List<WebScriptInfo> publics = service.findAllScripts("0");
		List<String> publicss = new ArrayList<String>();
		List<String> publicIds = new ArrayList<String>();
		for(WebScriptInfo s:publics){
			publicss.add(s.getScriptPath());
			publicIds.add(String.valueOf(s.getScriptId()));
		}
		
		User user = (User) StrutsMaps.getSessionMap().get("user");
		WebScriptReport report = RunRubyScript.runScripts(ss,publicss, StrutsMaps.getSettingValue("rubyWorkHome"), StrutsMaps.getProjectPath()+"/files", user.getUserId());
		service.saveReport(report);
		//�������һ������ʱ��
		publicIds.addAll(Arrays.asList(ids));
		String[] ss2 = (String[]) publicIds.toArray(new String[publicIds.size()]);
		service.updateRunTime(ss2);
		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setScriptIds(String scriptIds) {
		this.scriptIds = scriptIds;
	}
	
	
	public void setFileKind(Integer fileKind) {
		this.fileKind = fileKind;
	}
	
	public void setScriptNames(String scriptNames) {
		this.scriptNames = scriptNames;
	}
	
	public void setObjId(Integer objId) {
		this.objId = objId;
	}
		
	public void setFile(File file) {
		this.file = file;
	}
	@JSON(serialize=false)
	public File getFile() {
		return file;
	}
	
	@JSON(serialize=false)
	public String getFileFileName() {
		return fileFileName;
	}
	@JSON(serialize=false)
	public String getFileFileContentType() {
		return fileFileContentType;
	}
	
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	
	public void setFileFileContentType(String fileFileContentType) {
		this.fileFileContentType = fileFileContentType;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	@JSON(serialize=false)
	@Override
	public WebScriptInfo getModel() {
		// TODO Auto-generated method stub
		return script;
	}
}
