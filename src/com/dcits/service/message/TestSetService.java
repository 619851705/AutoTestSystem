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
	 * ���ҿ��úͽ���״̬�Ĳ��Լ�
	 * @return
	 */
	public List<TestSet> findAll(){
		return testSetDao.findAll();
	}
	
	
	/**
	 * ����idɾ�����Լ�
	 * @param id
	 */
	public void delSet(Integer id){
		testSetDao.delete(id);
	}
	
	/**
	 * ����ID�ı���Լ�״̬
	 * @param id
	 * @param status
	 */
	public void changeSetStatus(Integer id,String status){
		testSetDao.changeSetStatus(id, status);
	}
	
	/**
	 * �����µĲ��Լ�,����ID
	 * @param set
	 * @return
	 */
	public Integer saveSet(TestSet set){
		return testSetDao.save(set);
	}
	
	/**
	 * ���ݲ��Լ����Ʋ��Ҳ��Լ�Ψһ
	 * @param setName
	 * @return
	 */
	public TestSet findByName(String setName){
		return testSetDao.findSetByName(setName);
	}
	
	/**
	 * ����ID���Ҳ��Լ�
	 * @param id
	 * @return
	 */
	public TestSet findById(Integer id){
		return testSetDao.get(id);
	}
	
	/**
	 * �༭����set,Ҳ���Բ����µ�set
	 * @param set
	 */
	public void editSet(TestSet set){
		testSetDao.edit(set);
	}
	
	/**
	 * ɾ��ָ���Ĳ��Լ��Ͳ��Գ����Ĺ�����ϵ
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
	 * ��ȡ���г���,�������Ѿ������ڲ��Լ��ͽӿڻ��߱���״̬��Ϊ0�ĳ���
	 * @param setId
	 * @return
	 */
	public List<MessageScene> getAllSceneBySetId(Integer setId){
		return testSetDao.getAllScene(setId);
	}
	
	/**
	 * ����ָ�����������Լ�
	 * @param messageSceneId
	 * @param setId
	 */
	public void addScene(Integer messageSceneId,Integer setId){
		testSetDao.addScene(messageSceneId, setId);
	}
	
	/**
	 * �����û�����ӵ�еĲ��Լ�
	 * @param userId
	 * @return
	 */
	public List<TestSet> getMySet(Integer userId){
		return testSetDao.findSetByUser(userId);
	}
	
}
