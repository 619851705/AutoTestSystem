var messageId;
	$(document).ready(function(){	
		messageId= parent.$('#selectMessageId').val();
		getJsonStr();
		$("#copyJson").zclip({
			path: "../../libs/ZeroClipboard.swf",
			copy: function(){
			return $(".textarea").val();
			},
			afterCopy:function(){/* 复制成功后的操作 */
				layer.msg('复制成功,CTRL+V粘贴',{icon:1,time:1500});
	        }
		});
	});
	
	function getJsonStr(){	
		console.log(messageId);
		$.get("message-getParamsJson",{messageId:messageId},function(data){
			if(data.returnCode==0){
				$(".textarea").val(data.jsonStr);
			}else if(data.returnCode==2){
				$("#noDataTip").text("没有查询到指定报文的入参JSON串");
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});	
	}