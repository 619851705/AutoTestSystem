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
	 * 根据ID删除测试数据
	 * @param id
	 */
	public void delData(Integer id){
		testDataDao.delete(id);
	}
	
	/**
	 * 获取指定ID的测试数据信息
	 * @param id
	 * @return
	 */
	public TestData getData(Integer id){
		return testDataDao.get(id);
	}
	
	/**
	 * 更新数据json
	 * @param dataJson
	 * @param dataId
	 */
	public void updateDataJson(String dataJson,Integer dataId){
		testDataDao.updateDataValue(dataId, "paramsData", dataJson);
	}
	
	/**
	 * 更新数据状态
	 * @param status
	 * @param dataId
	 */
	public void updateDataStatus(String status,Integer dataId){
		testDataDao.updateDataValue(dataId, "status", status);
	}
	
	/**
	 * 保存测试数据并返回ID
	 * @param td
	 * @return
	 */
	public Integer saveData(TestData td){
		return testDataDao.save(td);
	}
	
	/**
	 * 判断是否有相同数据标识的数据
	 */
	public boolean findDataByDiscr(String discr,Integer messageSceneId){
		List<TestData> tds = testDataDao.findDataByDiscr(discr,messageSceneId);
		if(tds.size()>0){
			return true;
		}
		return false;
	}
}
