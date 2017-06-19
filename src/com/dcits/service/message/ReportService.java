package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.TestReport;
import com.dcits.bean.message.TestResult;
import com.dcits.dao.message.TestReportDao;
import com.dcits.dao.message.TestResultDao;

@Service
public class ReportService {
	@Autowired
	private TestReportDao testReportDao;
	@Autowired
	private TestResultDao testResultDao;
	
	/**
	 * 查找已完成测试的测试报告
	 * @return
	 */
	public List<TestReport> findAll(){
		return testReportDao.findAll();
	}
	
	/**
	 * 查找指定的测试详情集合
	 * @param reportId
	 * @param mode
	 * @return
	 */
	public List<TestResult> findResult(Integer reportId,String mode){
		return testResultDao.findResultByReportId(reportId, mode);
	}
	
	/**
	 * 根据ID查找指定的report
	 * @param reportId
	 * @return
	 */
	public TestReport getReport(Integer reportId){
		return testReportDao.get(reportId);
	}
	
	/**
	 * 根据ID查找指定的result
	 * @param resultId
	 * @return
	 */
	public TestResult getResult(Integer resultId){
		return testResultDao.get(resultId);
	}
	
	/**
	 * 删除测试报告
	 * @param reportId
	 */
	public void delReport(Integer reportId){
		testReportDao.delete(reportId);
	}
	
}
