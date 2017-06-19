$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#ctlBtn_rb").on("click",function(){	
		var v = $("#ifPublic").val();
		layer_show("rb文件上传", "fileUpload.html?mode=rb&fileKind="+v, '600', '400');		
	});
	
	$("#ctlBtn_feature").on("click",function(){		
		layer_show("feature文件上传", "fileUpload.html?mode=feature&fileKind=1", '600', '400');
	});
	
	$("#ifPublic").change(function(){
		var v = $(this).val();
		if(v=="0"){
			$("#featureFile").css("display", "none");
		}else{
			$("#featureFile").css("display", "block");
		}
		$("#compliteUploadFlag").val("");
		$("#fileName_rb").text("");
    	$("#uploadBtnRbText").text("上传ruby文件");
    	$("#fileName_feature").text("");           
    	$("#uploadBtnFeaText").text("上传feature文件");
		
	});
	
	var modeFlag=GetQueryString("modeFlag");
	
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".editFlag").css("display","block");
		var scriptId=GetQueryString("scriptId");
		$.post("webScript-getScript",{scriptId:scriptId},function(data){
			if(data.returnCode==0){
				var c=data.webScript;
				$("#scriptId").val(c.scriptId);
				$("#scriptIdText").text(c.scriptId);
				$("#scriptName").val(c.scriptName);
				$("#scriptAuthor").val(c.scriptAuthor);
				$("#uploadBtnRbText").text("重新上传ruby文件");
				$("#uploadBtnFeaText").text("重新上传feature文件");
				$("#fileName_feature").text(c.scriptPath+".feature");
				
				if(c.ifPublic=="0"){
					$("#featureFile").css("display", "none");
				}
								
				$("#scriptPath").val(c.scriptPath);
				$("#scriptDesc").val(c.scriptDesc);
				$("#createTime").val(c.createTime);
				$("#createTimeText").text(c.createTime);	
				if(c.lastRunTime==null||c.lastRunTime=="null"){
					c.lastRunTime="";
				}
				$("#lastRunTime").val(c.lastRunTime);
				$("#lastRunTimeText").text(c.lastRunTime);	
				$("#fileName_rb").text(c.scriptPath+".rb");				
				$("#compliteUploadFlag").val("0");
				$("#ifPublic").val(c.ifPublic);
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-webScript-edit").validate({
		rules:{
			scriptName:{
				required:true,
				minlength:2,
				maxlength:255
			},
			scriptAuthor:{
				required:true,
				minlength:2,
				maxlength:100
			},
			scriptFile:{
				required:true
			},
			scriptPath:{
				required:true
			},
			compliteUploadFlag:{
				required:true
			}
		},
		messages:{
			scriptPath:"你还没有上传脚本文件或者feature文件!",
			compliteUploadFlag:"你必须上传完整的文件!"
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){			
			var formData = $(form).serialize();
			$.post("webScript-editScript",formData,function(data){
				if(data.returnCode==0){		
					if($("#ifPublic").val()=="1"){
						parent.$('#btn-refresh').click();
					}
					var index = parent.layer.getFrameIndex(window.name);					
					parent.layer.close(index);
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});