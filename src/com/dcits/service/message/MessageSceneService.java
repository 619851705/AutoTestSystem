package com.dcits.service.message;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.TestSet;
import com.dcits.dao.message.MessageSceneDao;

@Service
public class MessageSceneService {
	
	@Autowired
	private MessageSceneDao msDao;
	
	
	/**
	 * ��ȡ��ǰ���п��õĲ��Գ���
	 * @return
	 */
	
	public List<MessageScene> findAll(){
		return msDao.findAll();		
	}
	
	
	/**
	 * ���ݱ���ID��ȡӵ�еĲ��Գ���
	 * @param messageId
	 * @return
	 */
	public List<MessageScene> findByMessageId(Integer messageId){
		return msDao.findByMessageId(messageId);
	}
	
	/**
	 * ֱ��ɾ��,ɾ��ǰ��Ҫ���������ϵ
	 * @param id
	 */
	public void delMessageScene(Integer id){
			MessageScene ms=msDao.get(id);
			Set<TestSet> tss=ms.getTestSets();
			for(TestSet ts:tss){
				ts.getMs().remove(ms);
			}		
			msDao.delete(id);
		
	}
	
	/**
	 * ����ָ������
	 * @param id
	 * @param typeName
	 * @param typeValue
	 */
	public void editMessageScene(Integer id,String typeName,String typeValue){
		String sql="update MessageScene set "+typeName+"='"+typeValue+"' where messageSceneId="+id;
		msDao.update(sql);
	}
	
	
	/**
	 * ��ȡָ��Id��messageScene
	 * @param id
	 * @return
	 */
	public MessageScene getMessageScene(Integer id){
		return msDao.get(id);
	}
	
	/**
	 * ���泡��
	 * @param ms
	 */
	public void saveMessageScene(MessageScene ms){
		msDao.save(ms);
	}
	
	/**
	 * ����ָ��messageScene����֤����ʽ
	 * @param messageSceneId
	 * @param validateRuleFlag
	 */
	public void changeValidateFlag(Integer messageSceneId,String validateRuleFlag){
		msDao.updateValidateFlag(messageSceneId, validateRuleFlag);
	}
	
}
