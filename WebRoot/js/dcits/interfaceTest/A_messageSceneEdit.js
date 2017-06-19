var messageSceneId;
   $(document).ready(function(){
	   refreshData();
   });
	
   
   
   function refreshData(){
	   $("#noDataTip").text("");
	   messageSceneId=parent.$("#selectMessageSceneId").val();
	   $.get("messageScene-get",{messageSceneId:messageSceneId},function(data){
		   if(data.returnCode==0){
   				$("#sceneInfo").text(data.messageScene.sceneName);
   				var datas=data.testData;
   				if(datas.length>0){
   					var html="";
   					$.each(data.testData,function(i,n){
   						var bstatus;
                    	var btnstyle;
                    	if(n.status=="0"){
                    		bstatus = "未使用";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "已使用";
                    		btnstyle = "disabled";
                    	}
                        var statucContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
   	   					html+='<tr class="text-c"><td>'+n.dataId+'</td><td class="ellipsis">'+'<span title="' + n.dataDiscr + '">' + n.dataDiscr + '</span>'+'</td><td><a href="javascript:;" onclick="messageScene_showJson(\''+n.dataId+'\')" class="btn btn-info size-S radius">查看</a></td><td>'+statucContent+'</td><td><a href="javascript:;" onclick="messageScene_delData(this,\''+n.dataId+'\')" class="btn btn-danger size-S radius">删除</a></td></tr>';
   	   				});
   					$("#tbodyP").html(html);
   				}else{
   					$("#noDataTip").text("没有测试数据,点击按钮增加");
   				} 				
   			}else if(data.returnCode==1){
   				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
   			}else{
   				layer.alert(data.msg, {icon: 5});
   			}
	   });
   }
   //删除数据
	function messageScene_delData(obj,id){
		layer.confirm('确认要删除吗？',function(index){
			layer.close(index);
        	$.post("messageScene-delData",{dataId:id},function(data){
        		if(data.returnCode==0){
        			$(obj).parents("tr").remove();
                    layer.msg('已删除',{icon:1,time:1000});
        		}else if(data.returnCode==1){
        			layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
        		}else{
        			layer.alert(data.msg, {icon: 5});
        		}
        	});
            
        });
	}
   
   //查看json数据
   function messageScene_showJson(id){
	    parent.$("#selectSceneDataId").val(id);
   		parent.layer_show('数据入参','A_dataJson.html','800','500');
   }
   
   //增加测试数据
   function messageScene_addData(){
	  // layer_show('增加记录','A_addDataJson.html?messageSceneId='+messageSceneId,'760','540');
	   layer.open({
		type: 2,
		area: ['760px', '540px'],
		fix: true, //固定
		maxmin: false,
		shade:0.4,
		title: '增加记录',
		content: 'A_addDataJson.html?messageSceneId='+messageSceneId
	   });
   }
   
   //批量导入
   function BatchImport(){
	   layer.msg('批量导入暂不可用,请使用单条数据添加');
   }
   
   //更改mark备注
   function showMark(){
	   layer.alert("更改备注的操作");
   }