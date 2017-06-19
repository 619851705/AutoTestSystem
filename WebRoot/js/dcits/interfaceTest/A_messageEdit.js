$(function(){
	var messageId=parent.$("#selectMessageId").val();
	
	$("#messageName").focus(function(){
		$("#formTips").text("输入报文名称,确保该名称不与已存在的报文名重复,推荐以该报文的详细功能命名,如\"根据用户账号查询订单列表\"");
	});
	$("#messageName").blur(function(){
		$("#formTips").text("");
	});
	$("#requestUrl").focus(function(){
		$("#formTips").text("该请求地址的优先级高于在其接口中配置的请求地址,如不填写,将会默认使用接口中的地址");
	});
	$("#requestUrl").blur(function(){
		$("#formTips").text("");
	});
	$(".textarea").focus(function(){
		$("#formTips").text("输入该种类型报文的完整入参JSON串,验证入参中的节点必须在所属接口中已经被定义");
	});
	$(".textarea").blur(function(){
		$("#formTips").text("");
	});
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	
	$.post("message-get",{messageId:messageId},function(data){
		if(data.returnCode==0){
			$("#messageId").val(data.message.messageId);
			$("#messageIdText").text(data.message.messageId);
			$("#messageName").val(data.message.messageName);
			$("#interfaceId").val(data.interfaceId);
			$("#interfaceNameText").text(data.interfaceName);
			$(".textarea").val(data.message.parameterJson);
			$("#requestUrl").val(data.message.requestUrl);
			if(data.message.status.toString()=="0"){
				$("#status-0").prop("checked","checked");
			}else{
				$("#status-1").prop("checked","checked");
			}
			$("#createTimeText").text(data.message.createTime);
			$("#createUserText").text(data.createUser);
			$("#lastModifyUserText").text(data.message.lastModifyUser);			
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});
	
	
	$("#form-message-edit").validate({
		rules:{
			messageName:{
				required:true,
				minlength:2,
				maxlength:100
			},
			interfaceId:{
				required:true,
			},
			jsonStr:{
				required:true,
			},
			status:{
				required:true,
			},
		},
		messages:{
			interfaceId:"请选择一个接口",
			jsonStr:"请点击验证报文入参",
			messageName:{
				required:"必须输入该报文的名称",
				minlength:"名称太短了",
				maxlength:"名称太长了"
			},
			status:"必须选择一个状态"
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			$.post("message-edit",$(form).serialize(),function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
					parent.layer.msg('修改成功',{icon:1,time:1000});
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon:5});
				}			
			});			
		}
	});
});

//选择接口
function chooseInterface(){
	layer_show('选择接口','A_chooseInterface.html','800','400');
}

var jsonStr;
//验证入参报文
function validateParams(){
	jsonStr=$(".textarea").val();
	if(jsonStr==null||jsonStr==""){
		layer.msg('你还没有输入任何内容呢',{icon:5,time:1500});
		return false;
	}
	var interfaceId = $("#interfaceId").val();
	if(interfaceId==null||interfaceId==""){
		layer.msg('请先选择一个接口!',{icon:5,time:1500});
		return false;
	}
	$.post("message-validateJson",{jsonStr:jsonStr,interfaceId:interfaceId},function(data){
		if(data.returnCode==0){
			layer.msg('验证通过,请执行下一步操作',{icon:1,time:1500});
			$("#jsonStr").val(jsonStr);
		}else if(data.returnCode==1){
			layer.alert('服务器内部错误,请稍后再试', {icon: 2});
		}else if(data.returnCode==2){
			layer.msg('JSON格式错误,请检查',{icon:5,time:1500});
		}else if(data.returnCode==3){
			layer.alert(data.msg,{icon:5});
		}else{
			layer.msg(data.msg,{icon:5,time:1500});
		}
	});
}
//格式化JSON
function formatJson(){
	jsonStr=$(".textarea").val();
	if(jsonStr==null||jsonStr==""){
		layer.msg('你还没有输入任何内容呢',{icon:5,time:1500});
		return false;
	}
	$.post("message-format",{jsonStr:jsonStr},function(data){
		if(data.returnCode==0){
			$(".textarea").val(data.returnJson);
		}else if(data.returnCode==2){
			layer.msg('格式错误,请检查',{icon:5,time:1500});
		}else{
			layer.msg(data.msg,{icon:5,time:1500});
		}
	});
}