var messageSceneId;
	var validateValueB;
	$(document).ready(function(){	
		messageSceneId = GetQueryString("messageSceneId");
		$.get('validate-getValidate',{messageSceneId:messageSceneId,fullValidateFlag:0},function(data){
			if(data.returnCode==0){
				$(".textarea").val(data.validateValue);
				$("#validateId").val(data.validateId);
				validateValueB = data.validateValue;
			}else if(data.returnCode==2){
				$(".textarea").val('');
				validateValueB = '';
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
		
	});
	
	//保存并选择此验证方式
	function save(){	
		//先更新
		var validateValue = $(".textarea").val();
		var validateId = $("#validateId").val();
		if(validateValueB!=validateValue){
			$.post('validate-fullValidateEdit',{validateId:validateId,validateValue:validateValue,messageSceneId:messageSceneId},function(data){
				if(data.returnCode==0){
					choose();
				}else{
					layer.alert(data.msg,{icon:5});
				}
			});
		}else{
			choose();
		}		
	}
	
	function choose(){
		$.get('messageScene-changeValidateRule',{messageSceneId:messageSceneId,validateRuleFlag:'2'},function(data){
			if(data.returnCode==0){
				layer.alert('操作成功!',{icon:1},function(index){
					parent.layer.close(parent.layer.getFrameIndex(window.name));
				});
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}