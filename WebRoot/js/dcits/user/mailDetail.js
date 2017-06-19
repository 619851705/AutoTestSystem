var mailId;
var modeFlag;
$(function(){
	$(".editFlag").hide();
	mainFunction();	
	
});

function mainFunction(){
	modeFlag=GetQueryString("modeFlag");
	//modeFlag为0则 为查看邮件
	//modeFlag为1则为查看并可以编辑修改
	mailId=GetQueryString("mailId");
	if(mailId!=null){
		$.post("mail-get",{mailId:mailId},function(data){
			if(data.returnCode==0){
				var o=data.mail;
				$("#mailId").val(o.mailId);
				$("#sendUserName").text(o.sendUserName);
				$("#receiveUserName").text(o.receiveUserName);
				$("#receiveUserId").val(data.receiveUserId);
				$("#sendTimeText").text(o.sendTime);
				if(o.ifValidate=="0"){		
					$("#ifValidate").attr("checked",true);
					$(".switch-animate").addClass("switch-on");								
				}
				$("#mailInfo").val(o.mailInfo);
				//可编辑的话		
			}else{
				layer.alert(data.msg,{icon:5});
			}
			
		});
	}	
	if(modeFlag=="1"){
		$(".editFlag").show();
		$(".viewFlag").hide();
		var addBtn = '&nbsp;&nbsp;<a href="javascript:;" onclick="chooseReceiveUser();" class="btn btn-danger radius">选择</a>';
		$("#receiveUserName").after(addBtn);
		$("#mailInfo").attr("readonly",false);
	}

}


function chooseReceiveUser(){
	layer_show("选择收件用户", "chooseReceiveUser.html", "800", "550");	
}

function sendMail(){
	saveEditMail(0);
}

function saveEditMail(flag){
	mailId = $("#mailId").val();
	var receiveUserId = $("#receiveUserId").val();
	var mailInfo = $("#mailInfo").val();
	var ifValidate = "1";
	if($("#ifValidate").is(':checked')){
		ifValidate = "0";
	}
	$.post("mail-save",{
		receiveUserId:receiveUserId,
		mailInfo:mailInfo,
		ifValidate:ifValidate,
		mailId:mailId
	},function(data){
		if(data.returnCode==0){
			if(mailId==null||mailId==""){
				$("#mailId").val(data.mailId);
				mailId=data.mailId;
			}
			if(flag==0){				
				$.post("mail-changeStatus",{mailId:mailId,statusName:"sendStatus",status:"0"},function(data){
					if(data.returnCode==0){
						parent.layer.msg('发送成功!',{icon:1,time:1500});
						parent.$("#btn-refresh-send").click();
						parent.layer.close(parent.layer.getFrameIndex(window.name));
					}else{
						layer.alert(data.msg, {icon: 5});
					}
					
				});
			}
			$("#tips").text("保存成功!时间："+ (new Date()).toLocaleTimeString());	
			parent.$("#btn-refresh-send").click();
			
		}else{
			$("#tips").text("保存失败! 时间："+ (new Date()).toLocaleTimeString());
		}
	});
}