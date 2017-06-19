package com.dcits.test.web;

import java.lang.reflect.Constructor;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dcits.bean.web.WebObject;

/**
 * 实现对象识别方法
 * 包括等待获取对象、即时获取对象以及对alert窗口的处理
 * @author Administrator
 *
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestObject {
	
	public static WebElement locateElement(WebDriver driver,String how,String value,Integer objectSeq){
		try {
			
			Class clz=Class.forName("org.openqa.selenium.By$By"+how);					
			Constructor ctr=clz.getConstructor(String.class);
			By ins=(By)ctr.newInstance(value);
			if(objectSeq==0){
				return ins.findElement(driver);
			}
			List<WebElement> els=ins.findElements(driver);
			return els.get(objectSeq-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 在指定时间内获取对象
	 * @param driver 
	 * @param object
	 * @param seconds 超时时间
	 * @return
	 */
	public static Object findObject(final WebDriver driver,final WebObject object,Integer seconds){
		if(object.getObjectType().equals("url")){
			return object.getPropertyValue();
		}
		WebDriverWait wait=new WebDriverWait(driver, seconds);
		return wait.until(new ExpectedCondition<WebElement>() {  			  
            @Override  
            public WebElement apply(WebDriver d) {  
                try {
					return locateElement(driver, object.getHow(), object.getPropertyValue(),object.getObjectSeq());
				} catch (Exception e) {
					e.printStackTrace();
				}
                	return null;
            }  
        }); 
	}
	
	/**
	 * 直接获取对象
	 * @param driver
	 * @param object
	 * @return
	 */
	public static WebElement noWaitFindObject(WebDriver driver,WebObject object){
		
		return null;
	}	
}
