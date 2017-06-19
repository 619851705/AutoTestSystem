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
	 * ���Ӳ��Ա���Ͳ�������֮��Ĺ�����ϵ
	 * @param reportSet
	 * @return
	 */
	public Integer addReportCase(WebReportCase reportCase){
		return webReportCaseDao.save(reportCase);
	}
	
	/**
	 * ���Ӳ����������桢���Ա���Ͳ���������֮��Ĺ�ϵ
	 * @param reportSet
	 * @return
	 */
	public Integer addReportSet(WebReportSet reportSet){
		return webReportSetDao.save(reportSet);
	}
	/**
	 * ���Ӳ��Խ��,һ�����Խ�����ֵ���һ������������һ������
	 * @param report
	 * @return
	 */
	public Integer addReport(WebReport report){
		return webReportDao.save(report);
	}
	
	/**
	 * ���ҵ�ǰ�û��Ĳ�����������
	 * ָ�������������Ĳ��Ա���,���Լ��Ĳ��Ա��������ѯ
	 * @param userId
	 * @return
	 */
	public List<WebReportCase> findAllReportCase(Integer userId){
		return webReportCaseDao.findMyReportCase(userId);
	}
	
	/**
	 * �������е�reportSet
	 * �������û�����,��Ϊֻ�й���Ա���ܲ�ѯ,�Ҳ����ĸ�����Ա
	 * @return
	 */
	public List<WebReportSet> findAllReportSet(){
		return webReportSetDao.findAll();
	}
	
	/**
	 * ɾ��reportSet����ɾ����������в��Ա���
	 * ����reportCase��report
	 * @param reportSetId
	 */
	public void delReportSet(Integer reportSetId){
		webReportSetDao.delete(reportSetId);
	}
	
	/**
	 * ɾ��reportCase��ɾ�������report
	 * @param reportCaseId
	 */
	public void delReportCase(Integer reportCaseId){
		webReportCaseDao.delete(reportCaseId);
	}
	
	/**
	 * ����ָ����reportSet
	 * @param reportSetId
	 * @return
	 */
	public WebReportSet getReportSet(Integer reportSetId){
		return webReportSetDao.get(reportSetId);
	}
	
	/**
	 * ����ָ���Ĳ�������������ϸ
	 * @param reportCaseId
	 * @return
	 */
	public WebReportCase getReportCase(Integer reportCaseId){
		return webReportCaseDao.get(reportCaseId);
	}
}
