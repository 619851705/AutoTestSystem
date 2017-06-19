package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestResult;
@SuppressWarnings("unchecked")
@Repository
public class TestResultDao extends BaseDao<TestResult> {
	//����reportId����ָ������״̬�Ĳ��Խ��
	public List<TestResult> findResultByReportId(Integer reportId,String mode){
		String hql="From TestResult t where t.testReport.reportId=:reportId and t.runStatus=:mode";
		return getSession().createQuery(hql).setString("mode",mode).setInteger("reportId", reportId).list();
	}
}
