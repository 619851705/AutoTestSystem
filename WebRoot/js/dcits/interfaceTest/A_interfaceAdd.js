$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-interface-add").validate({
		rules:{
			interfaceName:{
				required:true,
				minlength:2,
				maxlength:100
			},
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
				//url:true,
			},
			requestUrlReal:{
				required:true,
				//url:true,
			},
			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.get("interface-add?"+formData,function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
					//parent.layer.msg('增加成功',{icon:1,time:1000});
				}else if(data.returnCode==1){
					layer.msg('增加失败,请稍后重试',{icon:2,time:1500});
				}else if(data.returnCode==2){
					layer.msg('增加失败:此接口名已存在!',{icon:2,time:2000});
				}else if(data.returnCode==3){
					layer.confirm('对应该名称的接口已存在于回收列表中,是否需要恢复使用该接口?',function(index){
						$.get("interface-recover?interfaceId="+data.interfaceId,function(data2){
							if(data2.returnCode==0){
								var index = parent.layer.getFrameIndex(window.name);
								parent.$('#btn-refresh').click();
								parent.layer.close(index);
							}else{
								layer.msg('恢复失败,请稍后重试',{icon:2,time:1500});
							}
						});						
					});
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});