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
	 * 获取当前所有可用的测试场景
	 * @return
	 */
	
	public List<MessageScene> findAll(){
		return msDao.findAll();		
	}
	
	
	/**
	 * 根据报文ID获取拥有的测试场景
	 * @param messageId
	 * @return
	 */
	public List<MessageScene> findByMessageId(Integer messageId){
		return msDao.findByMessageId(messageId);
	}
	
	/**
	 * 直接删除,删除前需要解除关联关系
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
	 * 更新指定的项
	 * @param id
	 * @param typeName
	 * @param typeValue
	 */
	public void editMessageScene(Integer id,String typeName,String typeValue){
		String sql="update MessageScene set "+typeName+"='"+typeValue+"' where messageSceneId="+id;
		msDao.update(sql);
	}
	
	
	/**
	 * 获取指定Id的messageScene
	 * @param id
	 * @return
	 */
	public MessageScene getMessageScene(Integer id){
		return msDao.get(id);
	}
	
	/**
	 * 保存场景
	 * @param ms
	 */
	public void saveMessageScene(MessageScene ms){
		msDao.save(ms);
	}
	
	/**
	 * 更新指定messageScene的验证规则方式
	 * @param messageSceneId
	 * @param validateRuleFlag
	 */
	public void changeValidateFlag(Integer messageSceneId,String validateRuleFlag){
		msDao.updateValidateFlag(messageSceneId, validateRuleFlag);
	}
	
}
