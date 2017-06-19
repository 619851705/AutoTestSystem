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
	 * ���Ա��������
	 * @param report
	 */
	public Integer addReport(TestReport report){
		return testReportDao.save(report);
	}
	
	/**
	 * ���Խ������
	 * @param result
	 */
	public Integer addResult(TestResult result){
		return testResultDao.save(result);
	}
	
	/**
	 * ���Ӳ��Խ��ȱ�
	 * @param process
	 * @return
	 */
	public Integer addProcess(TestProcess process){
		return testProcessDao.save(process);
	}
	
	/**
	 * ��ȡ��ǰ�Ĳ��Խ�����Ϣ
	 * @param processId
	 * @return
	 */
	public TestProcess getProcess(Integer processId){
		return testProcessDao.get(processId);
	}
	
	/**
	 * ����ָ���Ĳ��Խ��ȱ�
	 * @param id
	 * @param currInfo
	 * @param currPercent
	 * @param flag
	 */
	public void updateProcess(Integer id,String currInfo,String currPercent,String flag){
		testProcessDao.updateProcess(id, currInfo, currPercent, flag);
	}
	
	/**
	 * ����ָ��userId���Ҳ�������
	 * @param userId
	 * @return
	 */
	public TestConfig getConfigByUserId(Integer userId){
		return testConfigDao.getConfigByUserId(userId);
	}
	
	/**
	 * ����config
	 * @param config
	 */
	public void updateTestConfig(TestConfig config){
		testConfigDao.edit(config);
	}
	
	/**
	 * ���Ӳ���config
	 * @param config
	 * @return
	 */
	public Integer addTestConfig(TestConfig config){
		return testConfigDao.save(config);
	}
	
	/**
	 * ��ȡָ���Ĳ��Ա���
	 * @param reportId
	 * @return
	 */
	public TestReport findReport(Integer reportId){
		return testReportDao.get(reportId);
	}
	
	/**
	 * ���²��Ա���
	 * @param report
	 */
	public void updateReport(TestReport report){
		testReportDao.edit(report);
	}
	
}
