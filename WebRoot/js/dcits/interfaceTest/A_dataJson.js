var dataId;
	$(document).ready(function(){	
		dataId= parent.$('#selectSceneDataId').val();
		getDataJson();
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
	
	//获取数据json
	function getDataJson(){	
		$.get("messageScene-getData",{dataId:dataId},function(data){
			if(data.returnCode==0){
				$(".textarea").val(data.dataJson);
			}else if(data.returnCode==2){
				$("#noDataTip").text("没有查询到指定的json");
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});	
	}
	
	//更新数据
	function updateDataJson(){
		var dataJson=$(".textarea").val();
		var messageSceneId=parent.$("#selectMessageSceneId").val();
		//console.log(messageSceneId);
		$.post("messageScene-updateDataJson",{dataJson:dataJson,messageSceneId:messageSceneId,dataId:dataId},function(data){
			if(data.returnCode==0){
				$(".textarea").val(data.dataJson);
				if(data.status=="0"){
					layer.msg('更新成功',{icon:1,time:1500});
				}else{
					layer.confirm('更新成功！\n该数据目前为已使用状态,是否手动置为可使用?',function(index){
			            $.post("messageScene-updateDataStatus",{dataId:dataId,typeValue:"0"},function(data){
			            	if(data.returnCode==0){
			            		layer.msg('更新成功',{icon:1,time:1500});
			            	}else if(data.returnCode==1){
			            		layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			            	}else{
			            		layer.alert(data.msg, {icon: 5});
			            	}
			            });
			        });	
				}							
			}else if(data.returnCode==2){
				layer.msg('json格式错误',{icon:2,time:1500});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else if(data.returnCode==3){
				$(".textarea").val(data.dataJson);
				layer.msg('json入参节点出错,请检查',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}