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
	//ajax调用返回的map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Role role = new Role();
	
	private Integer opType;
	
	//展示当前的所有的角色
	public String list(){
		List<Role> roles=service.findAll();
		for(Role r:roles){
			r.setOiNum();
		}
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", roles);
		return SUCCESS;
	}
	
	//删除角色,但是不能删除预置的管理员账户
	public String del(){
			Role role = service.get(roleId);
			String roleName = role.getRoleName();
			if(roleName.equals("admin")||roleName.equals("default")){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "不能删除超级管理员角色或者默认角色");
				return SUCCESS;
			}
			service.del(roleId);
			//删除其他角色,配置该角色的用户变更成default角色
			jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//根据roleId查找指定的role信息
	public String get(){
		Role role = service.get(roleId);
		jsonMap.put("role", role);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//编辑或者新增role信息
	public String edit(){
		//判断roleName的合法性:不能重复
		Role r = service.findRoleByName(role.getRoleName());
		if(r!=null){
			if((role.getRoleId()!=null&&r.getRoleId()!=role.getRoleId())||role.getRoleId()==null){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "该角色名已存在,请更换!");
				return SUCCESS;
			}			
		}
		if(role.getRoleId()!=null){
			//修改
			role.setOis(service.get(role.getRoleId()).getOis());
		}
		service.edit(role);		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取当前所有的操作接口列表
	//并且对当前角色已拥有的操作接口打标记
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
	
	//更新角色的权限信息
	//更新角色与操作接口的关联关系
	public String updateRolePower(){
		Role role = service.get(roleId);
		Set<OperationInterface> ops = role.getOis();
		//更新增加的权限
		if(addOpIds!=null&&!addOpIds.equals("")){
			String[] addOpArray = addOpIds.split(",");
			for(String s:addOpArray){
				ops.add(opService.get(Integer.valueOf(s)));
			}
			
		}
		//更新删除的权限
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
	
	
	//展示操作接口列表按Type
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
