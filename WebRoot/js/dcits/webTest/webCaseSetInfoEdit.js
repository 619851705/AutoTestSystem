$(function(){
	var setId=GetQueryString("setId");
	$.get("caseSet-getSet",{setId:setId},function(data){
		if(data.returnCode==0){
			$("#setName").val(data.caseSet.setName);
			$("#setId").val(data.caseSet.setId);
			$("#setDesc").val(data.caseSet.setDesc);
			$(":radio[value='"+data.caseSet.status+"']").attr("checked", true);
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
	

});

function saveSetInfo(){
	var saveInfo=$("#form-webCaseSetInfo-edit").serializeArray();
	$.post("caseSet-editSet",saveInfo,function(data){
		if(data.returnCode==0){			
			parent.layer.close(parent.layer.getFrameIndex(window.name));
			parent.layer.msg('更新成功,请刷新页面查看效果',{icon:1,time:2000});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}