$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	var modeFlag=GetQueryString("modeFlag");
	
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".row").eq(0).css("display","block");
		var caseId=GetQueryString("caseId");
		$.post("webCase-getCase",{caseId:caseId},function(data){
			if(data.returnCode==0){
				var c=data.webCase;
				$("#caseId").val(c.caseId);
				$("#caseIdText").text(c.caseId);
				$("#caseName").val(c.caseName);
				$("#browser").val(c.browser);
				$("#caseDesc").val(c.caseDesc);
										
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-webCase-edit").validate({
		rules:{
			caseName:{
				required:true,
				minlength:2,
				maxlength:200
			},
			browser:{
				required:true
			},
			caseDesc:{
				required:true
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("webCase-editCase",formData,function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请重试',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});