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
     * ���һ����ʱ����
     * @param task   ��������
     * @param classz ִ���������ϸjob
     * @throws SchedulerException 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void addTask(AutoTask task,Class classz,String baseUrl) throws SchedulerException{
    	//��ȡ������Ϣ����
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    	//�ж���ӵ������Ƿ��Ѿ����ڣ�
    	if(trigger==null){
    		//������,����һ��
    		 JobDetail jobDetail = JobBuilder.newJob(classz).withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).build();
    		 jobDetail.getJobDataMap().put("scheduleJob"+task.getTaskId(), task);
    		 jobDetail.getJobDataMap().put("baseUrl", baseUrl);
    		//���ʽ���ȹ�����  
             CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCronExpression());  
             //���µ�cronExpression���ʽ����һ���µ�trigger  
             trigger = TriggerBuilder.newTrigger().withIdentity(task.getTaskName(), String.valueOf(task.getTaskId())).withSchedule(scheduleBuilder).build();  
             scheduler.scheduleJob(jobDetail,trigger); 
    	}else{
    		//���ھ͸��±��ʽ
    		//���ʽ���ȹ�����  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getTaskCronExpression());  
            //���µ�cronExpression���ʽ���¹���trigger  
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();  
            //���µ�trigger��������jobִ��  
            scheduler.rescheduleJob(triggerKey, trigger);
    	}
    
    }
    
    /**
     * ֹͣ����
     * @param task
     * @throws SchedulerException 
     */
    public void stopTask(AutoTask task) throws SchedulerException{
    	//��ȡ������Ϣ����
    	TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), String.valueOf(task.getTaskId()));
    	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    	//������ھ�ɾ��(ֹͣ)
    	if(trigger!=null){
    		scheduler.unscheduleJob(triggerKey);
    	}
    }
    
    
    /** 
     * �������ж�ʱ���� 
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
     * ֹͣ��������
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
