$(function(){
	
	var modeFlag=GetQueryString("modeFlag");
	
	$("#taskType").change(function(){
		$("#relatedId").val("");
		$("#setNameText").text("");	
	});
	
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".row").eq(0).css("display","block");
		var taskId=GetQueryString("taskId");
		$.post("task-get",{taskId:taskId},function(data){
			if(data.returnCode==0){
				var o = data.task;
				$("#taskId").val(o.taskId);
				$("#createTime").val(o.createTime);
				$("#runCount").val(o.runCount);
				$("#status").val(o.status);
				$("#lastFinishTime").val(o.lastFinishTime);
				$("#taskCronExpression").val(o.taskCronExpression);
				$("#taskName").val(o.taskName);					
				$("#taskType").val(o.taskType);			
				$("#relatedId").val(o.relatedId);
				$("#setNameText").text(o.setName);				
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-task-edit").validate({
		rules:{
			taskName:{
				required:true,
				minlength:2,
				maxlength:255
			},
			taskType:{
				required:true
			},
			relatedId:{
				required:true
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("task-edit",formData,function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});

function chooseSet(){
	var type = $("#taskType").val();
	if(type=="0"){
		layer.confirm("选择接口自动化测试数据:",{title:"选择提示",btn:["全量数据","选择测试集"]},
		function(index){
			$("#relatedId").val("0");
			$("#setNameText").text("全量测试");	
			layer.close(index);
		},
		function(index){
			layer_show("选择测试集", "chooseTaskSet.html?taskType="+type, "760", "540");
			layer.close(index);
		});
	}else{
		layer_show("选择测试集", "chooseTaskSet.html?taskType="+type, "760", "540");	
	}
	
	
	
}