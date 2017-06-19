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
	 * ��������ɲ��ԵĲ��Ա���
	 * @return
	 */
	public List<TestReport> findAll(){
		return testReportDao.findAll();
	}
	
	/**
	 * ����ָ���Ĳ������鼯��
	 * @param reportId
	 * @param mode
	 * @return
	 */
	public List<TestResult> findResult(Integer reportId,String mode){
		return testResultDao.findResultByReportId(reportId, mode);
	}
	
	/**
	 * ����ID����ָ����report
	 * @param reportId
	 * @return
	 */
	public TestReport getReport(Integer reportId){
		return testReportDao.get(reportId);
	}
	
	/**
	 * ����ID����ָ����result
	 * @param resultId
	 * @return
	 */
	public TestResult getResult(Integer resultId){
		return testResultDao.get(resultId);
	}
	
	/**
	 * ɾ�����Ա���
	 * @param reportId
	 */
	public void delReport(Integer reportId){
		testReportDao.delete(reportId);
	}
	
}
