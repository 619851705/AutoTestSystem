package com.dcits.task;

import java.sql.Timestamp;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.bean.system.AutoTask;
import com.dcits.bean.user.User;
import com.dcits.bean.user.UserMail;
import com.dcits.service.system.AutoTaskService;
import com.dcits.service.user.UserMailService;
import com.dcits.util.JsonUtil;

/**
 * �������ʵ����
 * ͨ��AutoTask�����Ե�����ص�����
 * �������Լ����д���
 * @author Administrator
 *
 */
public class JobAction implements Job{	
	
	@Autowired
	private UserMailService mailService;
	@Autowired
	private AutoTaskService taskService;
	
	@Override
	public void execute(JobExecutionContext context) {
		//��ȡ������Ϣ
		try {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			AutoTask task = (AutoTask)dataMap.get("scheduleJob"+context.getJobDetail().getKey().getGroup());
			String baseUrl = dataMap.getString("baseUrl");
			//�ж���������
			String requestUrl = "";
			String taskType = "";
			switch (task.getTaskType()) {
			//�ӿ��Զ���
			case "0":
				requestUrl = baseUrl+"/test-runTest";
				taskType = "�ӿ�";
				break;
			case "1":
			//web�Զ���
				requestUrl = baseUrl+"/caseSet-runTest";
				taskType = "Web";
				break;
			case "2":
			//app�Զ���	
				requestUrl = "";
				taskType = "APP";
				break;
			}
			String returnJson = HttpClientRequest.postForm(requestUrl, task.getRelatedId());	
			String jobTip = "";
			if(returnJson!=""){
				String returnCode = JsonUtil.getObjectByJson(returnJson, "returnCode", JsonUtil.TypeEnum.string);
				if(returnCode.equals("0")){
					jobTip ="ϵͳ�ɹ���ִ���˱���"+taskType+"�ӿ��Զ�����ʱ��������,��ϸ�����μ����Ա���";
				}else{
					String returnMsg = JsonUtil.getObjectByJson(returnJson, "msg", JsonUtil.TypeEnum.string);
					jobTip ="����"+taskType+"�Զ�����ʱ����ִ��ʧ��,������Ϣ:\n"+returnMsg;
				}
			}else{
				jobTip = "����"+taskType+"�Զ�����ʱ����ִ��ʧ��,��ϸ��Ϣ��μ����Ա���Ͷ�ʱ�����б�!";
			}
			
			AutoTask task1 = taskService.get(task.getTaskId());
			task1.setLastFinishTime(new Timestamp(System.currentTimeMillis()));
			task1.setRunCount(task1.getRunCount()+1);
			taskService.edit(task1);
			
			User user = new User();
			user.setUserId(2);
			UserMail mail = new UserMail(user, null, "1", jobTip, "0", "1", new Timestamp(System.currentTimeMillis()), "", "");
			mailService.edit(mail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
