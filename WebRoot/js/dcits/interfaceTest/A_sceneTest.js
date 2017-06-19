var messageSceneId;
	$(document).ready(function(){	
		messageSceneId= parent.$('#selectMessageSceneId').val();
		$('#selectMessageSceneId').val(messageSceneId);
		$.get("messageScene-get",{messageSceneId:messageSceneId},function(data){
			if(data.returnCode==0){
				var selectUrlHtml=$("#selectUrl");
				$.each(data.urls,function(i,n){
					selectUrlHtml=selectUrlHtml.append("<option value='"+n+"'>"+n+"</option>");
				});				
				var selectDataHtml=$("#selectData");
				$.each(data.testData,function(i,n){
					selectDataHtml=selectDataHtml.append("<option value='"+i+"'>"+n.dataDiscr+"</option>");
				});
				$(".textarea").val(data.testData[0].paramsData);
				$("#selectTestDataId").val(data.testData[0].dataId);
				$("#selectData").change(function(){
					var p1=$(this).children('option:selected').val();
					$(".textarea").val(data.testData[p1].paramsData);
					$("#selectTestDataId").val(data.testData[p1].dataId);
				});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon:5});
			}
		});
	});
	
	//增加数据
	function addTestData(){
		parent.layer_show('增加测试数据','A_addDataJson.html?messageSceneId='+messageSceneId,'800','500');		
	}
	//测试
	function sceneTest(){
		
		var requestUrl=$("#selectUrl").val();
		var requestMessage=$(".textarea").val();
		
		
		if(requestUrl==""||requestUrl==null||requestMessage==""||requestMessage==null){
			layer.msg('请选择正确的接口地址和测试数据,你也可以手动添加新的测试数据并重新打开该页面!',{icon:2,time:1500});
			return;
		}
		var dataId=$("#selectTestDataId").val();
		var index = layer.load();
		$.post("test-sceneTest",{messageSceneId:messageSceneId,dataId:dataId,requestUrl:requestUrl,requestMessage:requestMessage},function(data){
			if(data.returnCode==0){
				layer.close(index);
				var color="";
				var falg="";
				if(data.result.runStatus=="0"){
					color="green";
					flag="success";
				}else if(data.result.runStatus=="1"){
					color="red";
					flag="fail";
				}else{
					color="red";
					flag="stop";
				}
				
				var showHtml='<table class="table table-bg"><tr>'+
				'<td>标记:</td><td><span style="color:'+color+';">'+flag+'</span></td></tr><tr><td>耗时:</td>'+
				'<td>'+data.result.useTime+'ms</td></tr><tr><td>状态码:</td><td>'+data.result.statusCode+'</td></tr><tr><td>返回:</td><td>'+
				'</td></tr><tr><td colspan="2"><textarea style="height: 180px;" class="textarea radius">'+data.result.responseMessage+'</textarea></td></tr><tr><td>测试备注:</td><td>'+
				'</td></tr><tr><td colspan="2"><textarea style="height: 180px;" class="textarea radius">'+data.result.mark+'</textarea></td></tr></table>';
				parent.layer.open({
					  title: '测试结果',
					  shade: 0,
					  type: 1,
					  skin: 'layui-layer-rim', //加上边框		
					  area: ['700px', '400px'], //宽高
					  content: showHtml
					});
			}else{
				layer.close(index);
				layer.alert(data.msg, {icon:5});
			}
		});
	}