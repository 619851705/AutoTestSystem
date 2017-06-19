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
		var roleId=GetQueryString("roleId");
		$.post("role-get",{roleId:roleId},function(data){
			if(data.returnCode==0){
				var o=data.role;
				$("#roleId").val(o.roleId);
				$("#roleIdText").text(o.roleId);
				$("#roleGroup").val(o.roleGroup);
				$("#roleName").val(o.roleName);
				$("#mark").val(o.mark);	
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-role-edit").validate({
		rules:{
			roleGroup:{
				required:true,
			},
			roleName:{
				isEnglish:true,
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
			$.post("role-edit",formData,function(data){
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