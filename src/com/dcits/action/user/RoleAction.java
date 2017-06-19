package com.dcits.action.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.OperationInterface;
import com.dcits.bean.user.Role;
import com.dcits.service.user.OperationInterfaceService;
import com.dcits.service.user.RoleService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class RoleAction extends ActionSupport implements ModelDriven<Role>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private RoleService service;
	@Autowired
	private OperationInterfaceService opService;
	
	private Integer roleId;
	
	private String delOpIds;
	private String addOpIds;
	//ajax���÷��ص�map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Role role = new Role();
	
	private Integer opType;
	
	//չʾ��ǰ�����еĽ�ɫ
	public String list(){
		List<Role> roles=service.findAll();
		for(Role r:roles){
			r.setOiNum();
		}
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", roles);
		return SUCCESS;
	}
	
	//ɾ����ɫ,���ǲ���ɾ��Ԥ�õĹ���Ա�˻�
	public String del(){
			Role role = service.get(roleId);
			String roleName = role.getRoleName();
			if(roleName.equals("admin")||roleName.equals("default")){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "����ɾ����������Ա��ɫ����Ĭ�Ͻ�ɫ");
				return SUCCESS;
			}
			service.del(roleId);
			//ɾ��������ɫ,���øý�ɫ���û������default��ɫ
			jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//����roleId����ָ����role��Ϣ
	public String get(){
		Role role = service.get(roleId);
		jsonMap.put("role", role);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�༭��������role��Ϣ
	public String edit(){
		//�ж�roleName�ĺϷ���:�����ظ�
		Role r = service.findRoleByName(role.getRoleName());
		if(r!=null){
			if((role.getRoleId()!=null&&r.getRoleId()!=role.getRoleId())||role.getRoleId()==null){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "�ý�ɫ���Ѵ���,�����!");
				return SUCCESS;
			}			
		}
		if(role.getRoleId()!=null){
			//�޸�
			role.setOis(service.get(role.getRoleId()).getOis());
		}
		service.edit(role);		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ��ǰ���еĲ����ӿ��б�
	//���ҶԵ�ǰ��ɫ��ӵ�еĲ����ӿڴ���
	@SuppressWarnings("unchecked")
	public String getNodes(){		
		List<OperationInterface> ops = (List<OperationInterface>) StrutsMaps.getApplicationMap().get("ops");				
		Role role = service.get(roleId);
		Set<OperationInterface> ownOps = role.getOis();
		
		for(OperationInterface op:ops){
			op.setIsOwn(false);			
			for(OperationInterface op1:ownOps){
				if((int)op1.getOpId()==(int)op.getOpId()){
					op.setIsOwn(true);
				}
			}
		}
				
		jsonMap.put("interfaces", ops);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//���½�ɫ��Ȩ����Ϣ
	//���½�ɫ������ӿڵĹ�����ϵ
	public String updateRolePower(){
		Role role = service.get(roleId);
		Set<OperationInterface> ops = role.getOis();
		//�������ӵ�Ȩ��
		if(addOpIds!=null&&!addOpIds.equals("")){
			String[] addOpArray = addOpIds.split(",");
			for(String s:addOpArray){
				ops.add(opService.get(Integer.valueOf(s)));
			}
			
		}
		//����ɾ����Ȩ��
		if(delOpIds!=null&&!delOpIds.equals("")){
			String[] delOpArray = delOpIds.split(",");
			for(String s:delOpArray){
				ops.remove(opService.get(Integer.valueOf(s)));
			}
		}
		role.setOis(ops);
		service.edit(role);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//չʾ�����ӿ��б�Type
	public String listOp(){
		Integer opId = 0;
		switch (opType) {
		case 1:
			opId = 2;
			break;
		case 2:
			opId = 85;
			break;
		case 3:
			jsonMap.put("data", new HashSet<OperationInterface>());
			jsonMap.put("returnCode", 0);
			return SUCCESS;
		case 4:
			opId = 63;
			break;
		case 5:
			opId = 70;
			break;
		}
		Set<OperationInterface> ops = opService.get(opId).getAllOis();
		for(OperationInterface op:ops){
			op.setParentOpName();
		}
		
		jsonMap.put("data", ops);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//////////////////////////////////GET-SET///////////////////////////////////////////////////////
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	
	public void setAddOpIds(String addOpIds) {
		this.addOpIds = addOpIds;
	}
	
	public void setDelOpIds(String delOpIds) {
		this.delOpIds = delOpIds;
	}
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	@Override
	public Role getModel() {
		// TODO Auto-generated method stub
		return role;
	}
}
