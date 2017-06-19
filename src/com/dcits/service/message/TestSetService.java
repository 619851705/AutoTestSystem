package com.dcits.service.message;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.TestSet;
import com.dcits.dao.message.TestSetDao;

@Service
public class TestSetService {
	
	@Autowired
	private TestSetDao testSetDao;
	
	
	/**
	 * 查找可用和禁用状态的测试集
	 * @return
	 */
	public List<TestSet> findAll(){
		return testSetDao.findAll();
	}
	
	
	/**
	 * 根据id删除测试集
	 * @param id
	 */
	public void delSet(Integer id){
		testSetDao.delete(id);
	}
	
	/**
	 * 根据ID改变测试集状态
	 * @param id
	 * @param status
	 */
	public void changeSetStatus(Integer id,String status){
		testSetDao.changeSetStatus(id, status);
	}
	
	/**
	 * 保存新的测试集,返回ID
	 * @param set
	 * @return
	 */
	public Integer saveSet(TestSet set){
		return testSetDao.save(set);
	}
	
	/**
	 * 根据测试集名称查找测试集唯一
	 * @param setName
	 * @return
	 */
	public TestSet findByName(String setName){
		return testSetDao.findSetByName(setName);
	}
	
	/**
	 * 根据ID查找测试集
	 * @param id
	 * @return
	 */
	public TestSet findById(Integer id){
		return testSetDao.get(id);
	}
	
	/**
	 * 编辑更新set,也可以插入新的set
	 * @param set
	 */
	public void editSet(TestSet set){
		testSetDao.edit(set);
	}
	
	/**
	 * 删除指定的测试集和测试场景的关联关系
	 * @param setId
	 * @param messageSceneId
	 */
	public void removeScene(Integer setId,Integer messageSceneId){
		TestSet ts=testSetDao.get(setId);
		Set<MessageScene> mss=ts.getMs();
		MessageScene delms=new MessageScene();
		for(MessageScene ms:mss){
			if(ms.getMessageSceneId()==messageSceneId){
				delms=ms;
			}
		}
		mss.remove(delms);
	}
	
	/**
	 * 获取所有场景,不包括已经存在于测试集和接口或者报文状态不为0的场景
	 * @param setId
	 * @return
	 */
	public List<MessageScene> getAllSceneBySetId(Integer setId){
		return testSetDao.getAllScene(setId);
	}
	
	/**
	 * 增加指定场景到测试集
	 * @param messageSceneId
	 * @param setId
	 */
	public void addScene(Integer messageSceneId,Integer setId){
		testSetDao.addScene(messageSceneId, setId);
	}
	
	/**
	 * 根据用户查找拥有的测试集
	 * @param userId
	 * @return
	 */
	public List<TestSet> getMySet(Integer userId){
		return testSetDao.findSetByUser(userId);
	}
	
}
