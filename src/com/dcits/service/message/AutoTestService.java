package com.dcits.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.TestConfig;
import com.dcits.bean.message.TestProcess;
import com.dcits.bean.message.TestReport;
import com.dcits.bean.message.TestResult;
import com.dcits.dao.message.TestConfigDao;
import com.dcits.dao.message.TestProcessDao;
import com.dcits.dao.message.TestReportDao;
import com.dcits.dao.message.TestResultDao;

@Service
public class AutoTestService {
	
	@Autowired
	private TestReportDao testReportDao;
	@Autowired
	private TestResultDao testResultDao;
	@Autowired
	private TestProcessDao testProcessDao;
	@Autowired
	private TestConfigDao testConfigDao;
	
	
	/**
	 * 测试报告的增加
	 * @param report
	 */
	public Integer addReport(TestReport report){
		return testReportDao.save(report);
	}
	
	/**
	 * 测试结果增加
	 * @param result
	 */
	public Integer addResult(TestResult result){
		return testResultDao.save(result);
	}
	
	/**
	 * 增加测试进度表
	 * @param process
	 * @return
	 */
	public Integer addProcess(TestProcess process){
		return testProcessDao.save(process);
	}
	
	/**
	 * 获取当前的测试进度信息
	 * @param processId
	 * @return
	 */
	public TestProcess getProcess(Integer processId){
		return testProcessDao.get(processId);
	}
	
	/**
	 * 更新指定的测试进度表
	 * @param id
	 * @param currInfo
	 * @param currPercent
	 * @param flag
	 */
	public void updateProcess(Integer id,String currInfo,String currPercent,String flag){
		testProcessDao.updateProcess(id, currInfo, currPercent, flag);
	}
	
	/**
	 * 根据指定userId查找测试配置
	 * @param userId
	 * @return
	 */
	public TestConfig getConfigByUserId(Integer userId){
		return testConfigDao.getConfigByUserId(userId);
	}
	
	/**
	 * 更新config
	 * @param config
	 */
	public void updateTestConfig(TestConfig config){
		testConfigDao.edit(config);
	}
	
	/**
	 * 增加测试config
	 * @param config
	 * @return
	 */
	public Integer addTestConfig(TestConfig config){
		return testConfigDao.save(config);
	}
	
	/**
	 * 获取指定的测试报告
	 * @param reportId
	 * @return
	 */
	public TestReport findReport(Integer reportId){
		return testReportDao.get(reportId);
	}
	
	/**
	 * 更新测试报告
	 * @param report
	 */
	public void updateReport(TestReport report){
		testReportDao.edit(report);
	}
	
}
