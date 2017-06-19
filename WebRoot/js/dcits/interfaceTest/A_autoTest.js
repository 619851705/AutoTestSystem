var configData;
var modeFlag=0;//0为全量测试
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
	$.get("test-getConfig",function(data){
		if(data.returnCode==0){
			configData=data.config;
			resetOptions();		
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
	
	
	
});

var index;
//开始测试集测试-选择测试集
function chooseTestSet(){
	//获取该用户创建的测试集
	 $.ajax({
		url:"set-getMy",
		type:"GET",
		success:function(data){
			if(data.returnCode==0){
				var html="";
				$.each(data.sets,function(i,n){
					html+='<tr class="text-c"><td>'+n.setId+'</td><td class="ellipsis">'+ '<span title="' + n.setName + '">' + n.setName + '</span>'
					+'</td><td>'+n.sceneNum+'</td><td><a href="javascript:;" onclick="prepareSetTest(\''+n.setId+'\',\''+n.sceneNum+'\')" class="btn btn-danger size-S radius">选择</a></td></tr>';
				});
				$("#tbodyP").html(html);
				html=$("#dynamicHtml").html();
				index=layer.open({
					  title: '选择测试集',
					  type: 1,
					  skin: 'layui-layer-rim', //加上边框		
					  area: ['800px', '400px'], //宽高
					  content: html
					});
			}else if(data.returnCode==2){
				layer.msg('你还没有创建测试集',{icon:2,time:1500});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon:5});
			}
		}
	}); 

}

function prepareSetTest(setId,sceneNum){
	modeFlag=setId;
	if(sceneNum<1){
		layer.msg('该测试集没有可用的测试场景,请重新选择',{icon:5,time:2000});
		return;
	}
	layer.close(index);	
	//需要检查数据
	if(configData.checkDataFlag=="0"){	
		checkData(setId);
	}else{
		sendTestRequest();
	}
	
	
}
//数据检查
function checkData(setId){
	$.post("test-validateData",{setId:setId},function(data){
		if(data.returnCode==0){	
			//发送开始测试的请求
			sendTestRequest();
		}else if(data.returnCode==2){
			layer.confirm('检测到共有'+data.size+'个场景缺少测试数据,是否需要增加数据?[如不需要检查数据,请更改测试设置]', {icon: 5, title:'检查数据提示'}, function(index){				  				  
				  $("#selectSetId").val(setId);
				  var index1 = layer.open({
			            type: 2,
			            title: "测试执行-编辑场景-请添加数据",
			            content: "A_setSceneList.html",
			        });
			        layer.full(index1);
			        layer.close(index);
				});			
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});
	
}

var reportId;
var scenes;
//发送测试请求
function sendTestRequest(){
	$.get("test-createReport?setId="+modeFlag,function(data){
		if(data.returnCode==0){
			reportId=data.reportId;
			scenes=data.scenes;
			startTest();
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});
}

//开始测试
function startTest(){
	//判断是否需要后台执行
	if(configData.backgroundExecFlag=="1"){
		commonTest();
	}else if(configData.backgroundExecFlag=="0"){
		backgroundTest();
	}else{
		layer.msg('值未设置',{icon:2,time:1500});
	}
}

//后台执行测试
function backgroundTest(){
	//发送后台测试请求
	$.ajax({
		type:"POST",
		url:"test-backgroundTest",
		dataType:"json",
		data:{setId:modeFlag,reportId:reportId}
	});
	layer.alert('已发送后台测试请求,你可以查看测试进度,测试成功之后会发送提醒邮件,请注意查收！',{icon:1});
}

//非后台执行
function commonTest(){
	//非后台执行需要单独一个一个执行
	$("#testTips").show();
	var testedNum=0;
	var sendData="";
	var n;
	$("#testMark").text("开始测试...");
	$("#testProcess").text("测试进度:"+testedNum+"/"+scenes.length);
	for(var i=0;i<scenes.length;i++){
		n=scenes[i];
		testedNum++;
		$("#testProcess").text("测试进度:"+testedNum+"/"+scenes.length);
		$("#testMark").text("正在测试"+n.interfaceName+"-"+n.messageName+"-"+n.sceneName);		
		$.ajax({
			url:"test-commonTest?reportId="+reportId+"&messageSceneId="+n.messageSceneId,
			async: false,
			success:function(data){
			if(data.returnCode!=0){
				layer.alert(data.msg, {icon:5});
			}
		}
		});       	
		}
	sendData="&finishFlag=0";
	$("#testMark").text("正在更新测试记录...");
	$.get("test-commonTest?reportId="+reportId+sendData,function(data){
		if(data.returnCode==0){
			$("#testMark").text("测试完成,请至测试报告页面查看测试报告");
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});	
}

//全量测试
function prepareTotalTest(){
	modeFlag=0;
	$.get("test-validatePower",function(data){
		if(data.returnCode==0){
			if(data.role!="admin"){
				layer.msg('你还没有执行的权限',{icon:2,time:1500});
			}else{
				if(configData.checkDataFlag=="0"){	
						checkData(modeFlag);
						}else{
						sendTestRequest();
						}
			}
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});		
}

//重置配置内容,调用接口
function resetOptions(){
	$("#requestUrlFlag").val(configData.requestUrlFlag);
	$("#connectTimeOut").val(configData.connectTimeOut);
	$("#readTimeOut").val(configData.readTimeOut);
	$("#httpMethodFlag").val(configData.httpMethodFlag);
	$("#validateString").val(configData.validateString);
	$("#checkDataFlag").val(configData.checkDataFlag);
	$("#backgroundExecFlag").val(configData.backgroundExecFlag);
}

//更新配置信息
function updateTestOptions(){
	var updateConfigData=$("#form-article-add").serializeArray();
	$.post("test-updateConfig",updateConfigData,function(data){
		if(data.returnCode==0){
			configData=data.config;
			layer.msg('更新成功',{icon:1,time:1500});
		}else if(data.returnCode==1){
			layer.msg('服务器内部错误,请稍后再试',{icon:2,time:1500});
		}else{
			layer.alert(data.msg, {icon:5});
		}
	});
}