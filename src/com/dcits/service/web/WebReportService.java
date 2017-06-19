package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebReport;
import com.dcits.bean.web.WebReportCase;
import com.dcits.bean.web.WebReportSet;
import com.dcits.dao.web.WebReportCaseDao;
import com.dcits.dao.web.WebReportDao;
import com.dcits.dao.web.WebReportSetDao;

@Service
public class WebReportService {
	@Autowired
	private WebReportDao webReportDao;
	@Autowired
	private WebReportSetDao webReportSetDao;
	@Autowired
	private WebReportCaseDao webReportCaseDao;
	
	/**
	 * 增加测试报告和测试用例之间的关联关系
	 * @param reportSet
	 * @return
	 */
	public Integer addReportCase(WebReportCase reportCase){
		return webReportCaseDao.save(reportCase);
	}
	
	/**
	 * 增加测试用例报告、测试报告和测试用例集之间的关系
	 * @param reportSet
	 * @return
	 */
	public Integer addReportSet(WebReportSet reportSet){
		return webReportSetDao.save(reportSet);
	}
	/**
	 * 增加测试结果,一条测试结果表现的是一个测试用例中一个步骤
	 * @param report
	 * @return
	 */
	public Integer addReport(WebReport report){
		return webReportDao.save(report);
	}
	
	/**
	 * 查找当前用户的测试用例报告
	 * 指单个测试用例的测试报告,测试集的测试报告另外查询
	 * @param userId
	 * @return
	 */
	public List<WebReportCase> findAllReportCase(Integer userId){
		return webReportCaseDao.findMyReportCase(userId);
	}
	
	/**
	 * 查找所有的reportSet
	 * 不根据用户区分,因为只有管理员才能查询,且不论哪个管理员
	 * @return
	 */
	public List<WebReportSet> findAllReportSet(){
		return webReportSetDao.findAll();
	}
	
	/**
	 * 删除reportSet将会删除下面的所有测试报告
	 * 包括reportCase和report
	 * @param reportSetId
	 */
	public void delReportSet(Integer reportSetId){
		webReportSetDao.delete(reportSetId);
	}
	
	/**
	 * 删除reportCase会删除下面的report
	 * @param reportCaseId
	 */
	public void delReportCase(Integer reportCaseId){
		webReportCaseDao.delete(reportCaseId);
	}
	
	/**
	 * 查找指定的reportSet
	 * @param reportSetId
	 * @return
	 */
	public WebReportSet getReportSet(Integer reportSetId){
		return webReportSetDao.get(reportSetId);
	}
	
	/**
	 * 查找指定的测试用例报告详细
	 * @param reportCaseId
	 * @return
	 */
	public WebReportCase getReportCase(Integer reportCaseId){
		return webReportCaseDao.get(reportCaseId);
	}
}
