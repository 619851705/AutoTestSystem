var hiddenText = '<input type="hidden" class="input-text" name="requireParameter" id="requireParameter" value=""/>';
var visibleText = '<input type="text" class="input-text" name="requireParameter" id="requireParameter"/>';
var index_selectDb;
var originalData = null;
var callMethods;
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});	 
	 $.get("../../js/json/callMethods.json",function(data){
		 callMethods = data;
	});
	$(".step-method").on("click",function(){
		 $(this).addClass("btn-danger").siblings().removeClass("btn-danger");		 
		 var t=$(this).attr("name");
		 var msgTip = "";		
		 switch(t){
		 	case "Action":
		 		msgTip = "执行步骤,直接执行指定的操作,可能需要参数";
		 		$(".getParamValue").html(hiddenText);
		 		$("#requireParameter").val("-");
		 		$("#requireParameterType").val("");
		 		break;
			case "Validate":
				msgTip = "验证步骤,将本次操作得到的返回值和预先设置值进行比对,比对成功则该测试用例执行成功或者继续执行下一个步骤,比对不成功或者没有比对值此步骤将会被标记成Fail。此步骤需要一个比对参数(必要参数),该参数支持自定义值、之前步骤的接收参数值和指定数据库查询值。具体使用方法请参考使用手册。";
				$("#requireParameterType").val("0");
				//选择对比参数获取方式
				layer.confirm('请选择预期验证数据的获取方式',{title:'提示',btn:['字符串','取值参数','数据库'],
					btn3:function(index){
						$.ajax({
							type:"GET",
							url:"db-list",
							//async: false,
							success:function(data){
								if(data.returnCode==0){
									if(data.data.length<1){
										layer.alert('没有可用的查询数据库连接信息,请在系统设置模块添加可用的数据库信息',{icon:5});
										return false;
									}
									var selectHtml = '<div class="row cl" style="width:340px;margin:15px;"><div class="form-label col-xs-2"><input type="button" class="btn btn-primary radius" onclick="selectDB();" value="选择"/></div><div class="formControls col-xs-10"><span class="select-box radius mt-0"><select class="select" size="1" name="selectDb" id="selectDb">';
									$.each(data.data,function(i,n){
										selectHtml += '<option value="'+n.dbId+'">'+n.dbMark+"-"+n.dbName+'</option>';									
									});
									selectHtml += '</select></span></div></div>';
									index_selectDb = layer.open({
								        type: 1,
								        title: "选择数据库",
								        area: ['355px', '110px'],
								        content:selectHtml
								    });
								}else{
									layer.alert(data.msg,{icon:5});
								}
							}							
						});			
						layer.close(index);
					}}
					,function(index){
						$(".getParamValue").html(visibleText);
						$("#requireParameterType").val("0");
						layer.close(index);
					}
					,function(index){
						var acquireStr = parent.$("#acquireStr").val();
						var strArray = acquireStr.split(",");
						if(strArray.length<2){
							layer.msg("没有找到取值步骤！");
							$(".getParamValue").html(hiddenText);
							return false;
						}												
						var selectHtml = '<span class="select-box radius mt-0"><select class="select" size="1" name="requireParameter" id="requireParameter">';
						$.each(strArray,function(i,n){
							if(n!=""){
								selectHtml += '<option value="'+n+'">'+(n.toString().replace(/</ig, "&lt;").replace(/>/ig, "&gt;"))+'</option>';
							}
							
						});
						selectHtml += '</select></span>	';
						$(".getParamValue").html(selectHtml);
						$("option").eq(0).attr("selected","selected");
						$("#requireParameterType").val("1");
						layer.close(index);
					});
				break;
			case "Acquire":
				msgTip = "取值步骤,获取的值可供展示或者后续步骤使用。需要一个接收参数(必要参数),你输入的参数值将会作为该次步骤获取的值的key。具体使用方法请参考操作手册。";
				$(".getParamValue").html(visibleText);
				$("#requireParameter").attr("placeholder","请填写一个用来接收结果的参数,格式 <参数名> ,并且保证此测试用例中,该参数名不重复");
				$("#requireParameterType").val("");
				break;
		 }
		 $("#stepMethodTip").text(msgTip); 
		 $("#stepMethod").val(t);
	});	
	
	
	var modeFlag=GetQueryString("modeFlag");
	var publicFlag=GetQueryString("publicStep");
	var urlP="webCase";
	
	if(publicFlag=="true"){
		urlP="publicStep";
	}
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".row").eq(0).css("display","block");
		var stepId=GetQueryString("stepId");
		$.post(urlP+"-getStep",{stepId:stepId},function(data){
			if(data.returnCode==0){
				originalData=data.webStep;
				resetForm();
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}else{
		var caseId=GetQueryString("caseId");
		$(".step-method").eq(0).click();
		$("#caseId").val(caseId);
	}
	$("#form-webStep-edit").validate({
		rules:{
			stepDesc:{
				required:true,
				minlength:2,
				maxlength:400
			},
			stepMethod:{
				required:true
			},
			callMethod:{
				required:true
			},
			capture:{
				required:true
			},
			requireParameter:{
				required:true
			}
		},
		messages:{
			callMethod:"必须选择一个对象调用方法",
			requireParameter:"必须需要一个比对参数或者接受参数"
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post(urlP+"-editStep",formData,function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请重试',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});



//选择CallMethod
function chooseCallMethod(){
	choosePopup(callMethods,"callMethod","选择对象调用方法","800px","480px");
}

function choosePopup(array,typeName,titleName,w,h){
	var html='';
	var htmlName='';
	var htmlVal='';
	$.each(array, function(i, n){		
		htmlName+='&nbsp;&nbsp;&nbsp;<input type="button" value="'+n.method+'" class="btn btn-danger radius objectTypeCss"/>';
		/* htmlVal+='<span class="typeDesc" style="float:left; padding:5px 20px;display:none;color:red;">'++'</span>'; */
		htmlVal+='<table class="table table-bg typeDesc" style="font-size:10px"><tr>'+'<td><strong>方法名：</strong></td><td>'+
		n.method+'</td></tr><tr><td><strong>说明：</strong></td><td>'+n.info+'</td></tr>'+
		'<tr><td><strong>适用对象：</strong></td><td>'+n.objectType+
		'</td></tr><tr><td><strong>方法参数：</strong></td><td>'+n.parameter+
		'</td></tr><tr><td><strong><span class="label label-warning radius">注意：</span></strong></td><td>'+n.mark+'</td></tr></table>';
		if(n.method=="toWindow"||n.method=="getAttribute"){
			htmlName+='<br><br>';
		}
	});
	html='<div><br>'+htmlName+'<br><br><hr><div style="float:left; padding:5px 20px;margin:10ox;">'+htmlVal+'</div></div>';
	var index=layer.open({
		  title: titleName,
		  type: 1,
		  skin: 'layui-layer-rim', //加上边框		
		  area: [w, h], //宽高
		  content: html
		});
	$(".objectTypeCss").on("click",function(){
		 $(this).addClass("btn-danger").siblings().removeClass("btn-danger");
		 $(".typeDesc").eq($(".objectTypeCss").index(this)).show().siblings().hide();
	});
	$(".objectTypeCss").on("dblclick",function(){
		var t=$(this).val();
		$("#"+typeName+"Text").text(t);
		$("#"+typeName).val(t);
		//根据callMethod类型来显示或者不显示指定的输入框
		var thisMethod = callMethods[$(".objectTypeCss").index(this)];
		if(thisMethod.objectType=="不需要指定"){
			$(".getObjectId").hide();
		}else{
			$(".getObjectId").show();
		}
		if(thisMethod.parameter=="不需要"){
			$(".getRequireValue").hide();
		}else{
			$(".getRequireValue").show();
		}
		
		
		layer.close(index);
	});	
	var selectedMethod = $("#callMethod").val();
	if(selectedMethod!=null&&selectedMethod!=""){
		$(".objectTypeCss[value='"+selectedMethod+"']").click();
	}else{
		$(".objectTypeCss").eq(0).click();
	}
}

function chooseWebObject(){
	var index=layer.open({
		type:2,
		title:"选择测试对象",
		content: "webObjectList.html"
	});
	layer.full(index);
}

//清除已选择的测试对象

function clearWebObject(){
	$("#clearObjectBtn").attr("type","hidden");
	$("#objectId").val('');
	$("#objectNameText").text('');
}

//选择数据库信息
function selectDB(){
	$("#requireParameterType").val($("#selectDb").val());
	$(".getParamValue").html(visibleText);
	$("#requireParameter").attr("placeholder","请输入查询用SQL语句,获取结果请指定一个数值");
	layer.close(index_selectDb); 
}

//重置表格
function resetForm(){
	$("#stepId").val(originalData.stepId);
	$("#stepIdText").text(originalData.stepId);
	$("#stepDesc").val(originalData.stepDesc);
	$("#stepMethod").val(originalData.stepMethod);				
	$("input[name='"+originalData.stepMethod+"']").addClass("btn-danger").siblings().removeClass("btn-danger");
	if(originalData.webObject!=null){
		$("#objectId").val(originalData.webObject.objectId);
		$("#objectNameText").text(originalData.webObject.objectName);
		$("#clearObjectBtn").attr("type","button");
	}											
	$("#callMethod").val(originalData.callMethod);
	$("#callMethodText").text(originalData.callMethod);		
	$(".getParamValue").html(visibleText);
	$("#requireValue").val(originalData.requireValue);
	$("#requireParameter").val(originalData.requireParameter);
	$("#requireParameterType").val(originalData.requireParameterType);
	$("#capture").val(originalData.capture);
}