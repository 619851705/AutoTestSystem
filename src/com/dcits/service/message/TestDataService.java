package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.TestData;
import com.dcits.dao.message.TestDataDao;


@Service
public class TestDataService {

	@Autowired
	private TestDataDao testDataDao;
	
	
	/**
	 * ����IDɾ����������
	 * @param id
	 */
	public void delData(Integer id){
		testDataDao.delete(id);
	}
	
	/**
	 * ��ȡָ��ID�Ĳ���������Ϣ
	 * @param id
	 * @return
	 */
	public TestData getData(Integer id){
		return testDataDao.get(id);
	}
	
	/**
	 * ��������json
	 * @param dataJson
	 * @param dataId
	 */
	public void updateDataJson(String dataJson,Integer dataId){
		testDataDao.updateDataValue(dataId, "paramsData", dataJson);
	}
	
	/**
	 * ��������״̬
	 * @param status
	 * @param dataId
	 */
	public void updateDataStatus(String status,Integer dataId){
		testDataDao.updateDataValue(dataId, "status", status);
	}
	
	/**
	 * ����������ݲ�����ID
	 * @param td
	 * @return
	 */
	public Integer saveData(TestData td){
		return testDataDao.save(td);
	}
	
	/**
	 * �ж��Ƿ�����ͬ���ݱ�ʶ������
	 */
	public boolean findDataByDiscr(String discr,Integer messageSceneId){
		List<TestData> tds = testDataDao.findDataByDiscr(discr,messageSceneId);
		if(tds.size()>0){
			return true;
		}
		return false;
	}
}
