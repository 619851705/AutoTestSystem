var mockId;
$(function(){
	mockId=parent.$("#selectMockId").val();
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$.get("mock-get?mockId="+mockId,function(data){
		if(data.returnCode==0){
			var mock=data.mock;
			$("#mockIdText").text(mock.mockId);
			$("#mockId").val(mock.mockId);
			$("#interfaceName").val(mock.interfaceName);
			$("#interfaceNameText").text(mock.interfaceName);
			$("#mockUrl").val(mock.mockUrl);
			$.get("global-getWebSettings",function(data){
	       		if(data.returnCode==0){			
	       			$("#mockUrlText").text(data.home+mock.mockUrl);
	       		}else{
	       			$("#mockUrlText").text(mock.mockUrl);
	       		}
	       	});			
			$("#ifValidate").val(mock.ifValidate);
			$("#status").val(mock.status);
			$("#createTimeText").text(mock.createTime);
			$("#createUserNameText").text(mock.createUserName);
			$("#callCountText").text(mock.callCount);
			$("textarea").eq(0).val(mock.requestJson);
			$("textarea").eq(1).val(mock.responseJson);
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请重试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
	
	$("#form-mock-edit").validate({
		rules:{
			interfaceName:{
				required:true,
				minlength:2,
				maxlength:100
			},
			requestJson:{
				required:true
			},
			responseJson:{
				required:true
			},			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("mock-edit",formData,function(data){
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

function formatJson(obj){
	var iputObj=$(obj).prev("input")[0];
	var textareaObj=$(obj).nextAll("textarea")[0];
	var jsonStr=$(textareaObj).val();
	if(jsonStr==null||jsonStr==""){
		layer.msg('你还没有输入任何内容呢',{icon:5,time:1500});
		return false;
	}
	$.post("message-format",{jsonStr:jsonStr},function(data){
		if(data.returnCode==0){
			$(textareaObj).val(data.returnJson);
			$(iputObj).val(data.returnJson);
		}else if(data.returnCode==2){
			layer.msg('json格式错误,请检查',{icon:5,time:1500});
		}else{
			layer.msg(data.msg,{icon:5,time:1500});
		}
	});
}