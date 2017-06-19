package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestResult;
@SuppressWarnings("unchecked")
@Repository
public class TestResultDao extends BaseDao<TestResult> {
	//根据reportId查找指定测试状态的测试结果
	public List<TestResult> findResultByReportId(Integer reportId,String mode){
		String hql="From TestResult t where t.testReport.reportId=:reportId and t.runStatus=:mode";
		return getSession().createQuery(hql).setString("mode",mode).setInteger("reportId", reportId).list();
	}
}
