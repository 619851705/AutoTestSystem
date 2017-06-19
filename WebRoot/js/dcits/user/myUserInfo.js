$(function(){
	mainFunction();
});

//修改密码
function modifyPasswd(){
	layer.prompt({
		  formType: 1,
		  value: '',
		  title: '验证旧密码'
		}, function(value, index, elem){
		  $.post("user-verifyPasswd",{password:value},function(data){
			 if(data.returnCode==0){
				 layer.prompt({
					  formType: 1,
					  value: '',
					  title: '请输入新密码'
					}, function(value1, index1, elem){
						layer.confirm('确定要将密码修改为"'+value1+'"吗？',function(index2){
				    		$.get("user-modifyPasswd",{password:value1},function(data){
				    			if(data.returnCode==0){
				                    layer.msg('密码修改成功,设置自动登录的需要重新输入密码!',{icon:1,time:1500});
				        		}else{
				        			layer.alert(data.msg, {icon: 5});
				        		}
				    		});
				    		
				    	});
					});
			 }else{
				 layer.alert(data.msg,{icon:5});
			 }
		  });
		});
}


function mainFunction(){
		$.post("user-getLoginUserInfo",function(data){
			if(data.returnCode==0){
				var o=data.data;
				$("#userIdText").text(o.userId);
				$("#usernameText").text(o.username);
				$("#realName").val(o.realName);
				$("#roleNameText").text(o.role.roleName);
				var statusMsg='';
				o.status=="0"?statusMsg="正常":statusMsg="锁定";
				$("#statusText").text(statusMsg);
				$("#createTimeText").text(o.createTime);
				$("#lastLoginTimeText").text(o.lastLoginTime);
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});

	$("#form-user-edit").validate({
		rules:{
			realName:{
				isChinese:true,
				minlength: 2,
				maxlength: 6
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			$.post("user-editMyName",{realName:$("#realName").val()},function(data){
				if(data.returnCode==0){		
					parent.$("#real_name").text($("#realName").val());
					layer.msg('更新成功',{icon:1,time:1500});
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
}