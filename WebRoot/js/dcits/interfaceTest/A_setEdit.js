$(document).ready(function(){
		$('input').removeAttr('checked'); 
		//获取要修改的测试集信息
		var setId= parent.$('#selectSetId').val();	
		$.get("set-find?setId="+setId,function(data){
			if(data.returnCode==0){
				var set = data.set;
				$("#setId").val(set.setId);
				$("#setIdText").text(set.setId);
				$("#setName").val(set.setName);				
		  		(set.status.toString()=="0") ? $("#status-0").prop("checked","checked") : $("#status-1").prop("checked","checked");
	/* 			if(msg.status.toString()=="0"){
					$("#status-0").prop("checked","checked");
				}else{
					$("#status-1").prop("checked","checked");
				} */
				$("#sceneNumText").text(set.sceneNum);
				$("#createTimeText").text(set.createTime);
				$("#createUserText").text(set.userName);
				$(".textarea").val(set.mark);				
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
		
		$("#form-set-edit").validate({
			rules:{
				setName:{
					required:true,
					minlength:2,
					maxlength:100
				},
				status:{
					required:true,
				},			
			},
			onkeyup:false,
			focusCleanup:true,
			success:"valid",
			submitHandler:function(form){
				$("#mark").val($(".textarea").val());
				var formData = $(form).serialize();
				$.get("set-edit?"+formData,function(data){
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