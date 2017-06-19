$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	//获取当前所有角色列表
	$.get("role-list",function(result){
		if(result.returnCode==0){
			var roles = result.data;
			var optionHtml='';
			for(var i=0;i<roles.length;i++){
				optionHtml+='<option value="'+roles[i].roleId+'">'+roles[i].roleName+'</option>';
			}
			$(".select").html(optionHtml);
			mainFunction();
		}else{
			layer.msg(result.msg,{icon:5});
		}
	});
	
	
	
});

function mainFunction(){
	var modeFlag=GetQueryString("modeFlag");
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".editFlag").css("display","block");
		var userId=GetQueryString("userId");
		$.post("user-get",{userId:userId},function(data){
			if(data.returnCode==0){
				var o=data.user;
				$("#userId").val(o.userId);
				$("#userIdText").text(o.userId);
				$("#username").val(o.username);
				$("#realName").val(o.realName);
				$("#roleId").val(o.role.roleId);
				$("#status").val(o.status);
				var statusMsg='';
				o.status=="0"?statusMsg="正常":statusMsg="锁定";
				$("#statusText").text(statusMsg);
				$("#createTime").val(o.createTime);
				$("#createTimeText").text(o.createTime);
				$("#lastLoginTime").val(o.lastLoginTime);
				$("#lastLoginTimeText").text(o.lastLoginTime)
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-user-edit").validate({
		rules:{
			username:{
				required:true,
				minlength: 2,
				maxlength: 20
			},
			realName:{
				required:true,
				minlength: 2,
				maxlength: 20
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("user-edit",formData,function(data){
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
}