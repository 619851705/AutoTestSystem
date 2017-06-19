package com.dcits.action.user;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.json.annotations.JSON;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.bean.message.TestConfig;
import com.dcits.bean.system.AutoTask;
import com.dcits.bean.user.Role;
import com.dcits.bean.user.User;
import com.dcits.service.message.AutoTestService;
import com.dcits.service.user.UserService;
import com.dcits.task.JobManager;
import com.dcits.util.MD5Util;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class UserAction extends ActionSupport implements ModelDriven<User>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(UserAction.class);
	
	@Autowired
	private UserService service;
	@Autowired
	private AutoTestService atService;
	
	private User user = new User();;

	private Integer roleId;
	
	private Integer userId;
	private String userName;
	private String mode;
	//ajax���÷��ص�map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	//չʾ�����û�
	public String list(){		
		List<User> users = service.findAll();
		jsonMap.put("data", users);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�û���½
	public String toLogin(){
		user=service.findUserBylogin(user.getUsername(), MD5Util.code(user.getPassword()));
		User user1 = (User)StrutsMaps.getSessionMap().get("user");
		int returnCode;
		String msg;
		if(user!=null){
			if(user1!=null&&user1.getUserId()==user.getUserId()){
				jsonMap.put("returnCode", 4);
				jsonMap.put("msg", "���ѵ�¼���˺�,���л�����ͬ���˺�!");
				return SUCCESS;
			}
			if(user.getStatus().equals("0")){
				returnCode=0;
				msg="";
				//���û���Ϣ����session��
				StrutsMaps.getSessionMap().put("user", user);
				
				//���û����Զ����������÷���session��
				TestConfig config=atService.getConfigByUserId(user.getUserId());
				if(config==null){
					config=atService.getConfigByUserId(0);
					TestConfig config1 = new TestConfig(user.getUserId(),config.getRequestUrlFlag(),config.getConnectTimeOut(),config.getReadTimeOut(),config.getHttpMethodFlag(),config.getValidateString(),config.getCheckDataFlag(),config.getBackgroundExecFlag());
					Integer configId = atService.addTestConfig(config1);
					config1.setConfigId(configId);
					StrutsMaps.getSessionMap().put("config", config1);					
				}else{
					StrutsMaps.getSessionMap().put("config", config);
				}
				
				user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
				service.updateUser(user);
				logger.info("�û�"+user.getRealName()+"��¼�ɹ�!");
			}else{
				returnCode=2;
				msg="����˺��ѱ�����,����ϵ����Ա���н�����";
			}
			
		}else{
			returnCode=1;
			msg="�˺Ż����벻��ȷ,����������!";
		}
		jsonMap.put("returnCode", returnCode);
		jsonMap.put("msg", msg);
		return SUCCESS;
	}
	
	//�û��ǳ�
	@SuppressWarnings("rawtypes")
	public String logout(){
		logger.info("�û�"+((User)StrutsMaps.getSessionMap().get("user")).getRealName()+"�ѵǳ�!");
		((SessionMap)StrutsMaps.getSessionMap()).invalidate();
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	
	//�ж��û��Ƿ��½
	public String judgeLogin(){
		User user=(User)StrutsMaps.getSessionMap().get("user");
		if(user!=null){
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 1);
		}
		return SUCCESS;

	}
	
	
	//��ȡ�ѵ�¼�����û��Ļ�����Ϣ
	public String getLoginUserInfo(){
		
		User user=(User)StrutsMaps.getSessionMap().get("user");		
		
		if(user!=null){
			jsonMap.put("data", user);			
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 1);
		}
		return SUCCESS;
	}
	
	//��¼�û��޸��Լ�����ʵ����
	public String editMyName(){
		User user1 = (User)StrutsMaps.getSessionMap().get("user");		
		service.editMyName(user.getRealName(), user1.getUserId());
		user1.setRealName(user.getRealName());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��֤��ǰ����
	public String verifyPasswd(){
		User user1=(User)StrutsMaps.getSessionMap().get("user");
		if(user1.getPassword().equals(MD5Util.code(user.getPassword()))){
			jsonMap.put("returnCode", 0);			
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "������֤ʧ��!");
		}
		
		
		return SUCCESS;
	}
	
	//�޸�����
	public String modifyPasswd(){
		User user1=(User)StrutsMaps.getSessionMap().get("user");
		service.resetPasswd(user1.getUserId(), MD5Util.code(user.getPassword()));
		user1.setPassword(MD5Util.code(user.getPassword()));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ��ָ���û�
	public String del(){
		if(userName.equals("admin")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "����ɾ��Ԥ�ù���Ա�û�!");
			return SUCCESS;
		}
		service.delUser(userId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�������߽����û�
	public String lock(){
		if(userName.equals("admin")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "��������Ԥ�ù���Ա�û�!");
			return SUCCESS;
		}
		service.lockedUser(userId, mode);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	//��������
	public String resetPwd(){
		service.resetPasswd(userId,MD5Util.code("111111"));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡָ���û���Ϣ
	public String get(){
		jsonMap.put("returnCode", 0);
		jsonMap.put("user", service.getUserInfo(userId));
		return SUCCESS;
	}

	
	//�û��༭
	public String edit(){
		User u1 = service.validateUsername(user.getUserId(), user.getUsername());
		if(u1!=null){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "�û����Ѵ���!");
			return SUCCESS;
		}
		Role r = new Role();
		r.setRoleId(roleId);
		user.setRole(r);
		if(user.getUserId()==null){
			//����
			user.setIfNew("1");
			user.setCreateTime(new Timestamp(System.currentTimeMillis()));
			user.setPassword(MD5Util.code("111111"));
			user.setStatus("0");
			user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
		}else{
			//�޸�
			User u2 = service.getUserInfo(user.getUserId());
			user.setIfNew(u2.getIfNew());
			user.setPassword(u2.getPassword());			
		}
		service.updateUser(user);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//������ѯ
	public String filter(){
		List<User> users = service.findByRealName(user.getRealName());
		if(users.size()==0){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "û�в�ѯ��ָ�����û�");
		}else{
			jsonMap.put("returnCode", 0);		
		}
		jsonMap.put("data",users );
		return SUCCESS;
	}
	
	
	
	//����
	public String quartzTest(){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc=request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		JobManager obj = (JobManager) ac.getBean("jobManager");
		AutoTask task = new AutoTask("����", "0", 0, "0 4 19 * * ?", 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "0");
    	task.setTaskId(1);
		try {
			obj.stopTask(task);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@JSON(serialize=false)
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}
	
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
