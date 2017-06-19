$(document).ready(function(){
		$('.skin-minimal input').iCheck({
			checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
			increaseArea: '20%'
		});
		$('input').removeAttr('checked'); 
		//获取要修改的接口信息
		var interfaceId= parent.$('#selectInterfaceId').val();	
		$.get("interface-find?interfaceId="+interfaceId,function(data){
			if(data.returnCode==0){
				var msg = data.interface;
				$("#interfaceId").val(msg.interfaceId);
				$("#interfaceIdText").text(msg.interfaceId);
				$("#interfaceName").val(msg.interfaceName);				
		  		var iType = '';
		  		(msg.interfaceType.toString()=="SL") ? iType="受理类" : iType="查询类" ;
		  		$("#interfaceTypeText").text(iType);
				$("#interfaceCnName").val(msg.interfaceCnName);
				$("#requestUrlMock").val(msg.requestUrlMock);
				$("#requestUrlReal").val(msg.requestUrlReal);
				if(msg.status.toString()=="0"){
					$("#status-0").prop("checked","true");
				}else{
					$("#status-1").prop("checked","true");
				}
				$("#createTimeText").text(msg.createTime);
				$("#createUserText").text(msg.user.realName);
				$("#lastModifyUserText").text(msg.lastModifyUser);				
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
		$('.skin-minimal input').iCheck({
			checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
			increaseArea: '20%'
		});
		
		$("#form-interface-edit").validate({
			rules:{
				interfaceType:{
					required:true,
				},
				interfaceCnName:{
					required:true,
					minlength:2,
					maxlength:100
				},
				requestUrlMock:{
					required:true,
					//url:true
				},
				requestUrlReal:{
					required:true,
					//url:true
				},
				status:{
					required:true,
				},			
			},
			onkeyup:false,
			focusCleanup:true,
			success:"valid",
			submitHandler:function(form){
				var formData = $(form).serialize();
				$.get("interface-update?"+formData,function(data){
					if(data.returnCode==0){										
						var index = parent.layer.getFrameIndex(window.name);
						parent.$('#btn-refresh').click();
						parent.layer.close(index);
						parent.layer.msg('更新成功',{icon:1,time:1000});
					}else if(data.returnCode==1){
						layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
					}else{
						layer.alert(data.msg, {icon: 5});
					}			
				});			
			}
		});
	});