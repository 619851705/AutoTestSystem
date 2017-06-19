package com.dcits.interceptor;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;

import com.dcits.bean.user.OperationInterface;
import com.dcits.bean.user.User;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 每个用户调用任何一个操作接口都必须经过本拦截器
 * 拦截三步骤：
 * 1、判断是否登录
 * 2、判断当前调用的操作接口是否正常
 * 3、判断当前用户的角色是否有权限调用该操作接口
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Controller
public class CallMethodInterceptor extends ActionSupport implements Interceptor {
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		@SuppressWarnings("unchecked")
		List<OperationInterface> ops = (List<OperationInterface>) StrutsMaps.getApplicationMap().get("ops");
		String actionName = arg0.getProxy().getActionName();
		int isCommon = 0;
		//判断当前调用的是否在接口列表中如果列表中没有调用接口的信息,说明这是一个通用接口,任何人都可以调用
		for(OperationInterface op:ops){
			if(actionName.equals(op.getCallName())){
				isCommon = 1;
				//判断当前调用的操作接口是否正常
				if(op.getStatus().equals("0")){
					User user = (User) StrutsMaps.getSessionMap().get("user");
					//判断用户是否登录
					if (user != null) {
						//admin权限最高权限通过全部请求
						if(user.getRole().getRoleName().equals("admin")){
							return arg0.invoke();
						}
						//判断用户是否有权限						
/*						Set<OperationInterface> ops1 = (Set<OperationInterface>) StrutsMaps.getApplicationMap().get(user.getRole().getRoleName());
*/						Set<OperationInterface> ops1 = user.getRole().getOis();
						for(OperationInterface op1:ops1){
							if(op1.getCallName().equals(actionName)){
								return arg0.invoke();
							}
						}						
					} else {
						return "usernotlogin";
					}					
				}else{
					return "opisdisable";
				}
			}
		}				
		if(isCommon==0){
			return arg0.invoke();
		}
		return "usernotpower";
	}
}
