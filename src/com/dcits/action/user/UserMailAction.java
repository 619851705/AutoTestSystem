package com.dcits.action.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.User;
import com.dcits.bean.user.UserMail;
import com.dcits.service.user.UserMailService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class UserMailAction extends ActionSupport implements ModelDriven<UserMail>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserMailService service;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private UserMail mail = new UserMail();
	
	private Integer mailType;
	
	private String statusName;
	
	private String status;
	
	private Integer receiveUserId;
	
	//��ȡδ���ʼ�����
	public String getNoReadMailNum(){
		User user = (User) StrutsMaps.getSessionMap().get("user");
		int num = service.getNoReadNum(user.getUserId());
		jsonMap.put("mailNum", num);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ�ռ����б���߷������б�
	//mailType=1�ռ����б�   mailType=2 �������б�
	public String listMails(){
		User user = (User) StrutsMaps.getSessionMap().get("user");
		List<UserMail> mails = new ArrayList<UserMail>();
		if(mailType==1){
			mails = service.findReadMails(user.getUserId());
		}else{
			mails = service.findSendMail(user.getUserId());
		}
		for(UserMail mail:mails){
			mail.setSendUserName();
			mail.setReceiveUserName();
			if(mail.getIfValidate().equals("0")&&mailType==1){
				mail.setMailInfo("");
			}
		}
		jsonMap.put("data", mails);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�ı��ʼ�״̬
	public String changeStatus(){
		if(statusName.equals("sendStatus")||statusName.equals("readStatus")||statusName.equals("ifValidate")){			
			if(statusName.equals("sendStatus")){
				UserMail mail1 = service.getMail(mail.getMailId());
				if(mail1.getReceiveUser()==null){
					jsonMap.put("returnCode", 3);
					jsonMap.put("msg", "��Ҫѡ��һ���ռ��û����ܷ���!");
					return SUCCESS;
				}
				mail1.setSendTime(new Timestamp(System.currentTimeMillis()));
				service.edit(mail1);
			}
			service.changeStatus(mail.getMailId(), statusName, status);
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "��������ȷ!");
		}		
		return SUCCESS;
	}
	
	//��ȡָ��mail
	public String get(){
		mail = service.getMail(mail.getMailId());
		mail.setReceiveUserName();
		mail.setSendUserName();
		if(mail.getReceiveUser()!=null){
			jsonMap.put("receiveUserId",mail.getReceiveUser().getUserId());
		}		
		jsonMap.put("mail", mail);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	
	//ɾ��
	//Ŀǰֻ��ɾ��δ���͵��ʼ�
	public String del(){
		service.del(mail.getMailId());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�����µ��ʼ���Ϣ���߸�����Ϣ
	public String save(){
		User user = (User) StrutsMaps.getSessionMap().get("user");
		
		mail.setReadStatus("1");
		mail.setSendStatus("1");
		mail.setSendUser(user);
		
		if(receiveUserId!=null){
			User receiveUser = new User();
			receiveUser.setUserId(receiveUserId);
			mail.setReceiveUser(receiveUser);
		}
		
		if(mail.getMailId()==null){
			Integer id = service.save(mail);
			jsonMap.put("mailId", id);
		}else{
			service.edit(mail);
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//����ϵͳ�ʼ�
	//ϵͳ����,�û�
	
	
	///////////////////////////////////////////set-get/////////////////////////////////////////////////////////
	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}
	
	@JSON(serialize=false)
	@Override
	public UserMail getModel() {
		// TODO Auto-generated method stub
		return mail;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
}
