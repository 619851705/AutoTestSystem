package com.dcits.task;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.dcits.bean.system.AutoTask;

public class JobManager {
	
	@Resource  
    private Scheduler scheduler ;  
      
    public Scheduler getScheduler() {  
        return scheduler;  
    }  
  
    public void setScheduler(Scheduler scheduler) {  
        this.scheduler = scheduler;  
    }
    
    /**
     * 添加一个定时任务
     * @param task   任务详情
     * @param classz 执行任务的详细job
     * @throws SchedulerException 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void addTask(AutoTask task,Class classz,String baseUrl) throws SchedulerException{
    	//获取任务信息数据
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    	//判断添加的任务是否已经存在？
    	if(trigger==null){
    		//不存在,创建一个
    		 JobDetail jobDetail = JobBuilder.newJob(classz).withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).build();
    		 jobDetail.getJobDataMap().put("scheduleJob"+task.getTaskId(), task);
    		 jobDetail.getJobDataMap().put("baseUrl", baseUrl);
    		//表达式调度构建器  
             CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCronExpression());  
             //按新的cronExpression表达式构建一个新的trigger  
             trigger = TriggerBuilder.newTrigger().withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).withSchedule(scheduleBuilder).build();  
             scheduler.scheduleJob(jobDetail,trigger); 
    	}else{
    		//存在就更新表达式
    		//表达式调度构建器  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCronExpression());  
            //按新的cronExpression表达式重新构建trigger  
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();  
            //按新的trigger重新设置job执行  
            scheduler.rescheduleJob(triggerKey, trigger);
    	}
    
    }
    
    /**
     * 停止任务
     * @param task
     * @throws SchedulerException 
     */
    public void stopTask(AutoTask task) throws SchedulerException{
    	//获取任务信息数据
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    	//任务存在就删除(停止)
    	if(trigger!=null){
    		scheduler.unscheduleJob(triggerKey);
    	}
    }
    
    
    /** 
     * 启动所有定时任务 
     * 
     */  
    public  void startTasks() {  
        try {  
            scheduler.start();  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**
     * 停止所有任务
     */
    public void stopTasks(){
    	try {
			scheduler.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
}
