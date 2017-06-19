package com.dcits.test.web;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import com.dcits.bean.system.DataDB;
import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebConfig;
import com.dcits.bean.web.WebReport;
import com.dcits.bean.web.WebReportCase;
import com.dcits.bean.web.WebStep;
import com.dcits.exception.web.NoDataToValidateException;
import com.dcits.exception.web.NoQueryDBFindException;
import com.dcits.exception.web.NoRequireValueException;
import com.dcits.util.PracticalUtils;
import com.dcits.util.db.DBUtil;
import com.opera.core.systems.OperaDriver;

public class TestCore {
	//保存取值的map
	private static Map<String,Object> acquireParams = new HashMap<String,Object>();
	
	/**
	 * 根据传入的WebCase实例执行完整的测试流程并返回测试报告集合
	 * @param webcase
	 * @return
	 */
	public static List<WebReport> runTest(WebCase webcase,WebReportCase reportCase,String screenShotPath,WebConfig config,String username,Map<String,DataDB> dataDBS){
		//每次测试一个测试用例就清空acquireParams
		acquireParams.clear();
		List<WebReport> wrs=new ArrayList<WebReport>();		
		Set<WebStep> wss=webcase.getSteps();
		//创建指定的WebDriver实例
		WebDriver driver=createDriver(webcase.getBrowser(),config);
		if(config.getWindowSize().equals("1")){
			//最大化窗口
			driver.manage().window().maximize();
		}
		WebReport report=null;
		for(int i=1;i<=wss.size();i++){
			for(WebStep step:wss){
				if(step.getOrderNum()==i){
					report=testStep(step,driver,username,reportCase,screenShotPath,config.getElementWaitTime(),config.getResultWaitTime(),dataDBS);
					wrs.add(report);
					if(!report.getRunStatus().equals("success")&&config.getErrorInterruptFlag().equals("1")){
						driver.quit();
						return wrs;
					}
				}
			}
		}
		driver.quit();
		return wrs;
	}
	
	/**
	 * 根据传入的浏览器名称创建指定类型的WebDriver
	 * @param driverName
	 * @return
	 */
	public static WebDriver createDriver(String driverName,WebConfig config){
		WebDriver driver=null;
		switch (driverName) {
		case "IE":
			if(config.getIePath()!=null&&!config.getIePath().equals("")){							
				 System.setProperty("webdriver.ie.bin",config.getIePath()); 
			}
			driver=new InternetExplorerDriver();
			break;
		case "Chrome":
			if(config.getChromePath()!=null&&!config.getChromePath().equals("")){
				System.setProperty("webdriver.chrome.bin",config.getChromePath()); 
			}
			driver=new ChromeDriver();
			break;
		case "FireFox":
			if(config.getFirefoxPath()!=null&&!config.getFirefoxPath().equals("")){
				System.setProperty("webdriver.firefox.bin",config.getFirefoxPath()); 
			}
			driver=new FirefoxDriver();
			break;
		case "Opera":
			if(config.getOperaPath()!=null&&!config.getOperaPath().equals("")){
				System.setProperty("webdriver.opera.bin",config.getOperaPath()); 
			}
			driver=new OperaDriver();
			break;
		}		
		return driver;
	}
	
	/**
	 * 单独执行一个测试步骤
	 * @param step
	 * @param driver
	 * @param c
	 * @param username
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static WebReport testStep(WebStep step,WebDriver driver,String username,WebReportCase reportCase,String screenShotPath,Integer elementWaitTime,Integer resultWaitTime,Map<String,DataDB> dataDBS){
		WebReport report=null;
		String runStatus="";
		String mark="";
		String capturePath="";
		try {
			Object o=null;
			if(step.getWebObject()!=null){
				o=TestObject.findObject(driver, step.getWebObject(), (elementWaitTime/1000));	
			}	
			//验证的步骤,需要得到返回的值和预先设置的值进行比对,比对成功则该测试成功或者继续执行下一步骤,比对不成功或者没有比对值报错
			if(step.getStepMethod().equals("Validate")){
				if(step.getRequireParameter().equals("")||step.getRequireParameter()==null){
					throw new NoRequireValueException();
				}								
				Thread.sleep(resultWaitTime);
				mark=(String) CallMethod.callMethod(step, o, driver);
				//比对值-必要参数
				String validateStr = null;
				switch (step.getRequireParameterType()) {
				case "0":  //直接取值
					validateStr = step.getRequireParameter();
					break;
				case "1": //从acquireParams中获取
					validateStr = (String)acquireParams.get(step.getRequireParameter());
					break;
				default: //从数据库中获取值
					//1、从全局session中获取对应的数据库信息
					DataDB db = dataDBS.get(step.getRequireParameterType());
					if(db==null){
						throw new NoQueryDBFindException();
					}
					Connection con = DBUtil.getConnection(db.getDbType(), db.getDbUrl(), db.getDbName(), db.getDbUsername(), db.getDbPasswd());
					if(con==null){
						throw new SQLException();
					}
					//2、替换sql语句中的参数
					String sqlStr = PracticalUtils.replaceSqlStr(step.getRequireParameter(), acquireParams);
					//3、执行sql语句获取数据
					validateStr = DBUtil.getDBData(con, sqlStr);
					break;
				}
				if(validateStr==null){
					throw new NoDataToValidateException();
				}
				
				if(mark.equals(validateStr)){
					runStatus="success";
				}else{
					runStatus="fail";
				}
				mark="预期结果为:【"+validateStr+"】\n实际获取结果为:【"+mark+"】";
			//执行的步骤,直接执行
			}else if(step.getStepMethod().equals("Action")){
				CallMethod.callMethod(step, o, driver);
				runStatus="success";
			//只获取值,获取的值以便后面的步骤使用。
			//问题:获取的值应该存放在哪,如何使用?
			}else if(step.getStepMethod().equals("Acquire")){
				if(step.getRequireParameter().equals("")||step.getRequireParameter()==null){
					throw new NoRequireValueException();
				}
				Object o1 = CallMethod.callMethod(step, o, driver);
				if(o1 != null){
					runStatus="success";
					//除了list其他都转成String
					if(o1 instanceof List){
						acquireParams.put(step.getRequireParameter(), (ArrayList)o1);
						mark = "成功获取到值: "+PracticalUtils.arrayListToString((ArrayList)o1);
					}else{
						acquireParams.put(step.getRequireParameter(), (String)o1);
						mark = "成功获取到值: "+o1.toString();
					}					
				}else{
					runStatus="fail";
					mark = "获取结果失败";
				}				
			}
			if(step.getCapture().equals("0")){
				capturePath=String.valueOf(System.currentTimeMillis())+".png";
				File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);  
				FileUtils.copyFile(screenShotFile, new File(screenShotPath+capturePath));
			}
		}catch(UnhandledAlertException e){ 
			runStatus="fail";
			mark="页面弹出框未处理,无法进行下一步操作";
			e.printStackTrace();
		}catch(NoSuchElementException e){ 
			runStatus="fail";
			mark="无法定位元素,请检查!";
			e.printStackTrace();
		}catch(NoSuchWindowException e){ 
			runStatus="fail";
			mark="无法切换到指定的窗口,请检查!";
			e.printStackTrace();
		}catch(NoAlertPresentException e){ 
			runStatus="fail";
			mark="当前页面没有出现弹出框,请检查!";
			e.printStackTrace();
		}catch(NoSuchFrameException e){ 
			runStatus="fail";
			mark="无法切换到指定的frame";
			e.printStackTrace();
		}catch(UnexpectedTagNameException e){ 
			runStatus="fail";
			mark="没有找到指定的html标签";
			e.printStackTrace();
		}catch(StaleElementReferenceException e){ 
			runStatus="fail";
			mark="可能是页面刷新导致指定的测试对象已失效,请重新获取";
			e.printStackTrace();
		}catch(TimeoutException e){ 
			runStatus="fail";
			mark="操作超时";
			e.printStackTrace();
		}catch(NoRequireValueException e){ 
			runStatus="fail";
			mark="没有找到要使用的必要参数,请检查";
			e.printStackTrace();
		}catch(NoDataToValidateException e){ 
			runStatus="fail";
			mark="获取验证比对参数值失败,请检查该步骤的取值方式和必要参数";
			e.printStackTrace();
		}catch(SQLException e){ 
			runStatus="fail";
			mark="获取数据库连接失败,请检查查询数据库的连接信息";
			e.printStackTrace();
		}catch(NoQueryDBFindException e){ 
			runStatus="fail";
			mark="查询不到指定查询数据库的连接信息,请确认该数据库信息是否被删除?";
			e.printStackTrace();
		}catch (Exception e) {
			runStatus="error";
			mark=e.toString();
			e.printStackTrace();			
		}		
		report=new WebReport(step, reportCase,runStatus, mark, capturePath, username, new Timestamp(System.currentTimeMillis()));		
		return report;
	}
}
