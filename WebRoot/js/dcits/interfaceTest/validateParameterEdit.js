$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	//更改checkbox的回调
	$(':checkbox').change(function(){
		var flag = $(this).is(':checked');
		if(flag==true){
			$("#parameterNameText").html('<span class="c-red">*</span>节点路径：');
			$("#parameterName").attr('placeholder','例如ROOT.DATA.PHONE_NO等');
			$("#tipMsg").text('出参报文中该节点的路径,类似ROOT.DATA.PHONE_NO等,代表ROOT节点下的DATA节点下的PHONE_NO的值,区分大小写');
		}else{
			$("#parameterNameText").html('<span class="c-red">*</span>节点名称：');
			$("#parameterName").attr('placeholder','例如PHONE_NO');
			$("#tipMsg").text('请输出参报文中单个节点参数名,类似PHONE_NO等,报文中有多个同名节点区分大小写');
		}
		
	});
	
	var modeFlag=GetQueryString("modeFlag");
	var messageSceneId = GetQueryString("messageSceneId");
	$("#messageSceneId").val(messageSceneId);
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".row").eq(0).css("display","block");
		var validateId=GetQueryString("validateId");
		$.post("validate-get",{validateId:validateId},function(data){
			if(data.returnCode==0){
				var o = data.validate;
				$("#status").val(o.status);
				$("#validateIdText").text(o.validateId);
				$("#validateId").val(o.validateId);	
				if(o.complexFlag=="0"){		
					$("#complexFlag").attr("checked",true);
					$(".switch-animate").addClass("switch-on");
					$(':checkbox').change();
					
				}
				$("#getValueMethod").val(o.getValueMethod);
				switch (o.getValueMethod) {
				case "0":
					$("#validateValue").attr("placeholder","字符串");
					$("#getValueMethodText").text("字符串");
					break;
				case "1":
					$("#validateValue").attr("placeholder","入参节点或者入参节点路径");
					$("#getValueMethodText").text("入参节点值");
					break;
				default:
					//获取指定数据库信息
					$.post("db-get",{dbId:o.getValueMethod},function(data){
						if(data.returnCode==0){
							$("#getValueMethodText").text("数据库-"+data.dataDB.dbMark+"-"+data.dataDB.dbName);
							$("#validateValue").attr("placeholder","SQL语句");
						}else{
							layer.alert("获取查询数据库信息错误:\n"+data.msg,{icon:5});
						}
					});			
					break;
				}
				$("#parameterName").val(o.parameterName);
				$("#validateValue").val(o.validateValue);
				$("#mark").val(o.mark);
				
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-validate-edit").validate({
		rules:{
			parameterName:{
				required:true,
				minlength:2,
				maxlength:255
			},
			getValueMethod:{
				required:true
			},
			validateValue:{
				required:true
			}
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("validate-edit",formData,function(data){
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

function chooseGetValueMethod(){
	//选择对比参数获取方式
	layer.confirm('请选择数据获取方式',{title:'提示',btn:['字符串','入参节点','数据库'],
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
		}}
		,function(index){
			$("#getValueMethod").val("0");
			$("#validateValue").attr("placeholder","字符串");
			$("#tipMsg").text('请输入用于比对该参数值的字符串,如18655036394');
			$("#getValueMethodText").text("字符串");
			layer.close(index);
		}
		,function(index){
			$("#getValueMethod").val("1");
			$("#validateValue").attr("placeholder","入参节点或者入参节点路径");
			$("#tipMsg").text('请输入你要获取的入参节点名称或者入参节点路径,程序将会自动化来获取,优先将会以入参节点路径模式来取值,区分大小写');
			$("#getValueMethodText").text("入参节点值");
			layer.close(index);
		});
}

//选择数据库信息
function selectDB(){
	$("#getValueMethod").val($("#selectDb").val());
	$("#validateValue").attr("placeholder","SQL语句");
	$("#getValueMethodText").text("数据库-"+$("select option:selected").text());
	$("#tipMsg").text('请输入查询用的SQL语句,在SQL语句中,你同样可以使用<PHONE_NO>的格式或者<ROOT.DATA.PHONE_NO>来替代表示入参节点数据');
	layer.close(index_selectDb); 
}