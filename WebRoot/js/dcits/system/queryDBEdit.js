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
		var dbId=GetQueryString("dbId");
		$.post("db-get",{dbId:dbId},function(data){
			if(data.returnCode==0){
				var o=data.dataDB;
				$("#dbId").val(o.dbId);
				$("#dbIdText").text(o.dbId);
				$("#dbType").val(o.dbType);
				$("#dbUrl").val(o.dbUrl);
				$("#dbName").val(o.dbName);				
				$("#dbUsername").val(o.dbUsername);
				$("#dbPasswd").val(o.dbPasswd);
				$("#dbMark").val(o.dbMark);	
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-queryDB-edit").validate({
		rules:{
			dbName:{
				required:true,
				minlength:1,
				maxlength:200
			},
			dbUrl:{
				required:true
			},
			dbUsername:{
				required:true
			},
			dbPasswd:{
				required:true
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("db-edit",formData,function(data){
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