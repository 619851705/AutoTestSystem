var messageId;
	$(document).ready(function(){	
		messageId= GetQueryString("messageId")
	});
	
	//重置
	function reset(){
		$(".textarea").val("");
		$(".input-text").val("");
	}
	//保存数据
	function saveMessageScene(){
		var mark=$(".textarea").val();
		var sceneName=$(".input-text").val();
		if(mark!=null&&mark!=""&&sceneName!=null&&sceneName!=""){
			$.post("messageScene-save",{messageId:messageId,mark:mark,sceneName:sceneName},function(data){
				if(data.returnCode==0){
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon:5});
				}
			});
		}else{
			layer.msg('请填写完整再提交保存',{icon:2,time:1500});
		}
	}