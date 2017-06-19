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
 * ÿ���û������κ�һ�������ӿڶ����뾭����������
 * ���������裺
 * 1���ж��Ƿ��¼
 * 2���жϵ�ǰ���õĲ����ӿ��Ƿ�����
 * 3���жϵ�ǰ�û��Ľ�ɫ�Ƿ���Ȩ�޵��øò����ӿ�
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
		//�жϵ�ǰ���õ��Ƿ��ڽӿ��б�������б���û�е��ýӿڵ���Ϣ,˵������һ��ͨ�ýӿ�,�κ��˶����Ե���
		for(OperationInterface op:ops){
			if(actionName.equals(op.getCallName())){
				isCommon = 1;
				//�жϵ�ǰ���õĲ����ӿ��Ƿ�����
				if(op.getStatus().equals("0")){
					User user = (User) StrutsMaps.getSessionMap().get("user");
					//�ж��û��Ƿ��¼
					if (user != null) {
						//adminȨ�����Ȩ��ͨ��ȫ������
						if(user.getRole().getRoleName().equals("admin")){
							return arg0.invoke();
						}
						//�ж��û��Ƿ���Ȩ��						
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
