package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestReport;
@SuppressWarnings("unchecked")
@Repository
public class TestReportDao extends BaseDao<TestReport> {

	@Override
	public List<TestReport> findAll() {
		String hql="From TestReport t where t.finishFlag='Y'";
		return getSession().createQuery(hql).list();
	}
	
	
}
