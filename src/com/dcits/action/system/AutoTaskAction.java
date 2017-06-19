package com.dcits.action.system;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.dcits.bean.system.AutoTask;
import com.dcits.service.message.TestSetService;
import com.dcits.service.system.AutoTaskService;
import com.dcits.service.web.WebCaseSetService;
import com.dcits.task.JobAction;
import com.dcits.task.JobManager;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class AutoTaskAction extends ActionSupport implements ModelDriven<AutoTask>{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AutoTaskService service;
	@Autowired
	private JobManager manager;
	@Autowired
	private TestSetService mService;
	@Autowired
	private WebCaseSetService wService;
	
	//ajax���÷��ص�map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private AutoTask task = new AutoTask();
	
	//�����б�չʾ
	public String list(){
		List<AutoTask> tasks = service.findAllTasks();
		for(AutoTask task:tasks){
			if(task.getRelatedId()==0){
				task.setSetName("ȫ������");
			}else{
				switch (task.getTaskType()) {
				case "0":
					task.setSetName((mService.findById(task.getRelatedId())).getSetName());
					break;
				case "1":
					task.setSetName((wService.get(task.getRelatedId())).getSetName());
					break;
				case "2":					
					break;
				}
			}
		}
		Object schedulerFlag = StrutsMaps.getApplicationMap().get("schedulerFlag");
		jsonMap.put("schedulerFlag", schedulerFlag);
		jsonMap.put("data", tasks);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//����cron���ʽ
	public String updateExpression(){
		service.updateExpression(task.getTaskId(), task.getTaskCronExpression());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡָ�������������Ϣ���
	public String get(){
		AutoTask t = service.get(task.getTaskId());
		if(t.getRelatedId()==0){
			t.setSetName("ȫ������");
		}else{
			switch (t.getTaskType()) {
			case "0":
				t.setSetName((mService.findById(t.getRelatedId())).getSetName());
				break;
			case "1":
				t.setSetName((wService.get(t.getRelatedId())).getSetName());
				break;
			case "2":	
				t.setSetName("");
				break;
			}
		}
		jsonMap.put("task", t);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//�༭��������
	public String edit(){
		if(task.getTaskId()==null){
			task.setStatus("1");
			task.setCreateTime(new Timestamp(System.currentTimeMillis()));
			task.setTaskCronExpression("");
		}
		service.edit(task);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�������п����е�����,����Ա���ܲ���,������վ������֮��,��Ҫ�ֶ�����̨����������Ҫ��������
	//������ʱ����
	public String startAllTasks() throws SchedulerException{
		Object schedulerFlag = StrutsMaps.getApplicationMap().get("schedulerFlag");
		if(schedulerFlag==null||!schedulerFlag.toString().equals("0")){
			ApplicationContext ac = StrutsMaps.getApplicationContext();
			JobManager manager = (JobManager) ac.getBean("jobManager");
			//��ȡ���п����е�����
			List<AutoTask> tasks = service.findRunTasks();
			for(AutoTask task:tasks){
				task.setLastFinishTime(null);
				task.setRunCount(0);
				service.edit(task);
				manager.addTask(task, JobAction.class, StrutsMaps.getSettingValue("home"));
			}
			manager.startTasks();
			StrutsMaps.getApplicationMap().put("schedulerFlag", "0");
			jsonMap.put("returnCode", 0);
		}		
		return SUCCESS;
	}
	
	
	//���һ����������
	public String addTask() throws SchedulerException{
		
		Object schedulerFlag = StrutsMaps.getApplicationMap().get("schedulerFlag");
		
		if(schedulerFlag==null||!schedulerFlag.toString().equals("0")){
			jsonMap.put("returnCode", 3);
			jsonMap.put("msg", "��������������������ٿ�������!");
			return SUCCESS;
		}
		
		
		AutoTask t = service.get(task.getTaskId());
		if(t.getTaskCronExpression()==null||t.getTaskCronExpression().equals("")){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "��ʱ����Ϊ�ջ��߲�����Ҫ��,����������!");
			return SUCCESS;
		}
		
		//��runCount��lastFinishTime�ÿ�
		t.setLastFinishTime(null);
		t.setRunCount(0);
		t.setStatus("0");
		service.edit(t);
		
		manager.addTask(t, JobAction.class, StrutsMaps.getSettingValue("home"));
		jsonMap.put("returnCode", 0);
		jsonMap.put("msg", "���гɹ�,������վ���ʼ������˽��������!");
		return SUCCESS;
	}
	
	//ֹͣ����
	public String stopTask() throws SchedulerException{
		
		Object schedulerFlag = StrutsMaps.getApplicationMap().get("schedulerFlag");
		
		if(schedulerFlag==null||!schedulerFlag.toString().equals("0")){
			jsonMap.put("returnCode", 3);
			jsonMap.put("msg", "������������������������²���!");
			return SUCCESS;
		}
		
		AutoTask t = service.get(task.getTaskId());
		t.setStatus("1");
		service.edit(t);
		manager.stopTask(t);
		jsonMap.put("returnCode", 0);
		jsonMap.put("msg", "����ֹͣ�ɹ�!");
		return SUCCESS;
	}
	
	//ɾ������
	public String del(){
		service.del(task.getTaskId());
		jsonMap.put("returnCode", 0);
		jsonMap.put("msg", "����ɾ���ɹ�!");
		return SUCCESS;
	}
	
	////////////////////////////////////////set-get//////////////////////////////////////////////////////
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	@JSON(serialize=false)
	@Override
	public AutoTask getModel() {
		// TODO Auto-generated method stub
		return task;
	}
	
}
