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
 * 任务相关实现类
 * 通过AutoTask的属性调用相关的请求
 * 并不是自己进行处理
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
		//获取任务信息
		try {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			AutoTask task = (AutoTask)dataMap.get("scheduleJob"+context.getJobDetail().getKey().getGroup());
			String baseUrl = dataMap.getString("baseUrl");
			//判断任务类型
			String requestUrl = "";
			String taskType = "";
			switch (task.getTaskType()) {
			//接口自动化
			case "0":
				requestUrl = baseUrl+"/test-runTest";
				taskType = "接口";
				break;
			case "1":
			//web自动化
				requestUrl = baseUrl+"/caseSet-runTest";
				taskType = "Web";
				break;
			case "2":
			//app自动化	
				requestUrl = "";
				taskType = "APP";
				break;
			}
			String returnJson = HttpClientRequest.postForm(requestUrl, task.getRelatedId());	
			String jobTip = "";
			if(returnJson!=""){
				String returnCode = JsonUtil.getObjectByJson(returnJson, "returnCode", JsonUtil.TypeEnum.string);
				if(returnCode.equals("0")){
					jobTip ="系统成功的执行了本次"+taskType+"接口自动化定时测试任务,详细结果请参见测试报告";
				}else{
					String returnMsg = JsonUtil.getObjectByJson(returnJson, "msg", JsonUtil.TypeEnum.string);
					jobTip ="本次"+taskType+"自动化定时任务执行失败,错误信息:\n"+returnMsg;
				}
			}else{
				jobTip = "本次"+taskType+"自动化定时任务执行失败,详细信息请参见测试报告和定时任务列表!";
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
