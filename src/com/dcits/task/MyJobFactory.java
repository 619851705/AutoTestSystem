package com.dcits.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class MyJobFactory extends AdaptableJobFactory implements ApplicationContextAware {
//  @Resource  
//  private AutowireCapableBeanFactory capableBeanFactory;  
        
    ApplicationContext applicationContext;  
      
//  public AutowireCapableBeanFactory getCapableBeanFactoryYunZong() {  
//      return capableBeanFactoryYunZong;  
//  }  
//  
//  
//  public void setCapableBeanFactoryYunZong(  
//          AutowireCapableBeanFactory capableBeanFactoryYunZong) {  
//      this.capableBeanFactoryYunZong = capableBeanFactoryYunZong;  
//  }  
  
  
    @Override  
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {  
        //调用父类的方法  
        Object jobInstance = super.createJobInstance(bundle);  
          
       AutowireCapableBeanFactory aaa = applicationContext.getAutowireCapableBeanFactory();  
       aaa.autowireBeanProperties(jobInstance, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);  
//      applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);  
//      capableBeanFactoryYunZong.autowireBean(jobInstance);  
        return jobInstance;  
      }  
  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext)  
            throws BeansException {  
        this.applicationContext = applicationContext;  
          
    }  

}
