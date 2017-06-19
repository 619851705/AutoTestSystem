var configData;
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$.get("webConfig-get",function(data){
		if(data.returnCode==0){
			configData=data.config;
			$.each(data.config,function(key,value){
					$("#"+key).val(value);				
			});
		}else{
			layer.alert(data.msg,{icon:5});
		}
		
	});
	
	$("#form-webConfig-edit").validate({
		rules:{
			elementWaitTime:{
				number:true,				
				min:0,
				max:60000
			},
			resultWaitTime:{
				number:true,
				min:0,
				max:60000
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serializeArray();
			$.post("webConfig-edit",formData,function(data){
				if(data.returnCode==0){										
					configData=data.config;
					layer.msg('更新成功',{icon:1,time:1500});
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请重试',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});

function resetConfig(){
	$.each(configData,function(key,value){
		$("#"+key).val(value);				
});
}