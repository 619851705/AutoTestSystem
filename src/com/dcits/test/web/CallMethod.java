package com.dcits.test.web;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.dcits.bean.web.WebStep;

/**
 * 对对象的调用方法,比如click、go_to、setValue等
 * 是对selenium中的方法另一种封装
 * @author Administrator
 *
 */
public class CallMethod {	
	public static Object callMethod(WebStep step,Object o,WebDriver driver){
		try {
			switch (step.getCallMethod()) {
			case "click":
				click((WebElement)o);
				return null;
			case "go_to":
				go_to((String)o,driver);
				return null;
			case "sendKeys":
				sendKeys((WebElement)o,step.getRequireValue());
				return null;
			case "getTitle":
				return getTitle(driver);
			case "getText":
				return getText((WebElement)o);
			case "toFrame":
				switchToFrame(driver, step.getRequireValue());
				return null;
			case "toDefaltFrame":			
				switchToDefault(driver);
				return null;
			case "toWindow":
				switchToWindow(driver,step.getRequireValue());
				return null;
			case "getHandle":			
				return getWindowHandle(driver);
			case "getAllHandle":
				return getWindowHandles(driver);
			case "dismissDialog":
				dismissDialog(driver);
				return null;
			case "acceptDialog":
				acceptDialog(driver);
				return null;
			case "getDialogText":
				return getDialogText(driver);
			case "sendToDialog":
				sendKeyDialog(driver,step.getRequireValue());
				return null;
			case "getAttribute":
				return getAttribute((WebElement)o,step.getRequireValue());
			case "judgeSelected":
				return judgeSelected((WebElement)o);
			case "clear":
				clear((WebElement)o);
				return null;
			case "selectByValue":
				selectByValue((WebElement)o,step.getRequireValue());
				return null;
			case "selectByOptionText":
				selectByOptionText((WebElement)o,step.getRequireValue());
				return null;
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	//获取文本内容
	//这个是获取不到input的value值的,这个需要注意,只能获取中间的text
	public static String getText(WebElement element){
		return element.getText();
	}
	
	//获取元素指定的属性值,例如value name,id,class等,必须保证该元素拥有这个属性否则报错
	public static String getAttribute(WebElement element,String attributeName){
		return element.getAttribute(attributeName);
	}
	
	//点击操作
	//包括radio  checkbox  button link 等标签
	public static void click(WebElement element){
		element.click();
		
	}
	
	//打开url操作
	public static void go_to(String url,WebDriver driver){
		driver.get(url);
	}
	
	//发送填写内容
	public static void sendKeys(WebElement element,String keys){
		element.sendKeys(keys);
	}
	
	//获取当前窗口的title
	public static String getTitle(WebDriver driver){
		return driver.getTitle();
	}
	
	//切换定位至指定id或者name的frame
	public static void switchToFrame(WebDriver driver,String frameIdOrName){
		driver.switchTo().frame(frameIdOrName);
	}
	
	//返回到最顶层的iframe
	public static void switchToDefault(WebDriver driver){
		driver.switchTo().defaultContent();		
	}
	
	//切换窗口,参数是窗口名称或者窗口的句柄handle
	public static void switchToWindow(WebDriver driver,String nameOrHandle){
		driver.switchTo().window(nameOrHandle);
	}
	
	//获取当前窗口的句柄
	public static String getWindowHandle(WebDriver driver){
		return driver.getWindowHandle();
	}
	
	//获取当前所有的打开的窗口的句柄
	public static List<String> getWindowHandles(WebDriver driver){
		return new ArrayList<String>(driver.getWindowHandles());
	}
	
	//X掉关闭或者取消掉对话框
	public static void dismissDialog(WebDriver driver){
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}
	
	//点击对话框的确认按钮
	public static void acceptDialog(WebDriver driver){
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	//获取对话框中文本
	public static String getDialogText(WebDriver driver){
		Alert alert = driver.switchTo().alert();
    	String a = alert.getText();
    	alert.accept();
    	return a;		
	}
	
	//发送字符串给prompt
	public static void sendKeyDialog(WebDriver driver,String keys){
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(keys);
		alert.accept();
	}
	
	//判断元素对象是否被选中
	//radio单选框、CheckBox多选框是否被选中,选中返回字符串true没选中返回字符串false
	public static String judgeSelected(WebElement element){
		return String.valueOf(element.isSelected());		
	}
	
	//清除操作-清空或者取消选中
	//可操作对象：文本框  文本域  radio checkbox  	
	public static void clear(WebElement element){
		element.clear();
	}
	
	//根据下拉框的option的value属性来指定下拉选项
	public static void selectByValue(WebElement element,String value){
		Select select = new Select(element);
		select.selectByValue(value);
	}
	
	//根据下拉框的option的可见文本值来指定下拉选项
	public static void selectByOptionText(WebElement element,String text){
		Select select = new Select(element);
		select.selectByVisibleText(text);
	}
}
