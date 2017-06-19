var tbobyP=$("#tbodyP");
    var interfaceId;
	$(document).ready(function(){		
		interfaceId= parent.$('#selectInterfaceId').val();		
		var html='';
		$.get("interface-showParameters?interfaceId="+interfaceId,function(data){
			if(data.returnCode==0){
				$.each(data.data,function(i,n){
					var btnS='<a href="javascript:;" onclick="interface_delParameter(this,\''+n.parameterId+'\')" class="btn btn-danger size-S radius">删除</a>';
					/* html+='<tr class="text-c"><td class="paramId" id="'+n.parameterId+'">'+n.parameterId+'</td><td class="opTd" name="parameterIdentify">'+n.parameterIdentify
						+'</td><td class="ellipsis opTd" name="parameterName">'+ '<span title="' + n.parameterName + '">' + n.parameterName + '</span>'+'</td><td class="ellipsis opTd" name="defaultValue">'
						+ '<span title="' + n.defaultValue + '">' + n.defaultValue + '</span>'+'</td><td class="opTd" name="type">'+n.type+'</td><td>'+btnS+'</td></tr>'; */
					html+='<tr class="text-c"><td class="paramId" id="'+n.parameterId+'">'+n.parameterId+'</td><td class="opTd" name="parameterIdentify">'+n.parameterIdentify
					+'</td><td class="ellipsis opTd" name="parameterName">'+ n.parameterName +'</td><td class="ellipsis opTd" name="defaultValue">'
					+  n.defaultValue +'</td><td class="opTd" name="type">'+n.type+'</td><td class="btnTd">'+btnS+'</td></tr>';
				});
				tbobyP.html(html);	
				$(".opTd").click(interface_editParameter);
				$("#editTig").text("单击已修改参数属性");
			}else if(data.returnCode==2){
				$("#noDataTip").text("没有参数,你可以手动增加或者通过JSON报文导入");
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});		
	});
	
	/**增加一列保存新的参数*/
	function interface_addParameter(){
		if(tbobyP.has('input').length>0 || tbobyP.has('select').length>0){
			layer.alert('请先保存或者取消已修改的内容!', {icon: 2});
			return false;
		};
		var html='';
		var btnS='<a href="javascript:;" onclick="interface_saveParameter(this)" class="btn btn-success size-S radius">保存</a>&nbsp;<a href="javascript:;" onclick="interface_cancelAdd(this)" class="btn btn-danger size-S radius">取消</a>';
		var selectS='<select><option value="Array">Array</option><option value="Map">Map</option><option value="String">String</option><option value="Number">Number</option><option value="List">List</option></select>';
		html+='<tr class="text-c"><td class="paramId" id=""></td><td class="opTd" name="parameterIdentify">'+'<input type="text"/>'
			+'</td><td class="opTd" name="parameterName">'+'<input type="text"/>'+'</td><td class="opTd" name="defaultValue">'
			+'<input type="text"/>'+'</td><td class="opTd" name="type">'+selectS+'</td><td class="btnTd">'+btnS+'</td></tr>';
		tbobyP.prepend(html);
	}
	
	/**保存新增参数*/
	function interface_saveParameter(obj){
		var interfaceId= parent.$('#selectInterfaceId').val();	
		var tdList = $(obj).parent('td').siblings();
		var parameterIdentify = $(tdList[1]).children().val();
		var parameterName = $(tdList[2]).children().val();
		var defaultValue = $(tdList[3]).children().val();
		var type = $(tdList[4]).children().val();
		$.post("interface-saveParameter",{
			parameterIdentify: parameterIdentify,
			parameterName: parameterName,
			defaultValue: defaultValue,
			type: type,
			interfaceId: interfaceId},function(data){
			if(data.returnCode==0){
				var html = '';
				var btnS='<a href="javascript:;" onclick="interface_delParameter(this,\''+data.parameterId+'\')" class="btn btn-danger size-S radius">删除</a>';
				html+='<tr class="text-c"><td class="paramId" id="'+data.parameterId+'">'+data.parameterId+'</td><td class="opTd" name="parameterIdentify">'+parameterIdentify
					+'</td><td class="opTd" name="parameterName">'+parameterName+'</td><td class="opTd" name="defaultValue">'
					+defaultValue+'</td><td class="opTd" name="type">'+type+'</td><td class="btnTd">'+btnS+'</td></tr>';
				$(obj).parents("tr").remove();
				tbobyP.prepend(html);
				$(".opTd").click(interface_editParameter);
				layer.msg('增加成功',{icon:1,time:1500});	
				$("#editTig").text("单击已修改参数属性");
				$("#noDataTip").text("");
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}
	
	
	/**取消增加参数*/
	function interface_cancelAdd(obj){
		$(obj).parents("tr").remove();
	}
	
	var index;
	/**导入json串*/
	function interface_leadToJson(){
		var showHtml='<div class="page-container">'+
		'<div class="cl pd-5 bg-1 bk-gray mt-0"> <span class="1"><a href="javascript:;" onclick="batchImportParams();" class="btn btn-danger radius">导入</a></span></div><br>'+	
		'<textarea style="height: 240px;" class="textarea radius" id="jsonParams" placeholder="输入接口报文"></textarea>'+
		'</div>';
		index=layer.open({
			  title: '导入json',
			  shade: [0.3, '#000'],
			  type: 1,
			  skin: 'layui-layer-rim', //加上边框		
			  area: ['780px', '400px'], //宽高
			  content: showHtml
			});
	}
	
	function batchImportParams(){
		var paramsJson=$("#jsonParams").val();
		$.post("interface-batchImportParams",{interfaceId:interfaceId,paramsJson:paramsJson},function(data){
			if(data.returnCode==0){
				$('#btn-refresh').click();
				layer.close(index);
				layer.msg('导入成功',{icon:1,time:1500});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else if(data.returnCode==2){
				layer.msg('你输入的不是json格式',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}
	
	/**删除此条参数*/
	function interface_delParameter(obj,id){
		layer.confirm('确认要删除吗？',function(index){
			layer.close(index);
        	$.post("interface-delParameter",{parameterId:id},function(data){
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
	/**编辑参数*/
	function interface_editParameter(){
		if(tbobyP.has('input').length>0 || tbobyP.has('select').length>0){
			layer.alert('请先保存或者取消已修改的内容!', {icon: 2});
			return false;
		};
		$("#editTig").text("Enter键提交更改,Esc取消更改");
		var tdObj = $(this);
		if (tdObj.children("input").length>0 || tdObj.children("select").length>0) {
            return false;
        }
		var text = tdObj.text();
		var currBtn = tdObj.siblings(".btnTd");
		var btnHtml = $(currBtn).html();
		var btnS='<a id="saveBtn" href="javascript:;" onclick="" class="btn btn-success size-S radius">保存</a>&nbsp;<a id="cancelBtn" href="javascript:;" onclick="" class="btn btn-danger size-S radius">取消</a>';
		$(currBtn).html(btnS);
		
		

		/* var textHtml = tdObj.html(); */
		tdObj.html("");
		var inputObj;
		if(tdObj.attr("name")=="type"){
			var selectType = $("<select></select>").append("<option value='Array'>Array</option>").append("<option value='Map'>Map</option>").append("<option value='String'>String</option>").append("<option value='Number'>Number</option>").append("<option value='List'>List</option>");
			inputObj = selectType.css("font-size", tdObj.css("font-size")).css("background-color", tdObj.css("background-color")).width(tdObj.width()).val(text).appendTo(tdObj);
		}else{
			inputObj = $("<input type='text'>").css("font-size", tdObj.css("font-size")).css("background-color", tdObj.css("background-color")).width(tdObj.width()).val(text).appendTo(tdObj);       
	        inputObj.trigger("focus").trigger("select");
		}
		
              inputObj.click(function () {
                  return false;
              });
              
              var saveFn = function(){
            	  var inputtext = inputObj.val();
                  //回车ajax提交,获取parameterId
                  var parameterId = tdObj.siblings(".paramId").attr("id");
                  var attrName = tdObj.attr("name");
                  $.post("interface-editParameter",{
                	  parameterId:parameterId,
                	  attrName:attrName,
                	  attrValue:inputtext},
                	  function(data){
                		  if(data.returnCode==0){
                			  layer.msg('更新成功',{icon:1,time:1000});
                			  tdObj.html(inputtext); 
                			  $(currBtn).html(btnHtml);
                              $("#editTig").text("单击以修改参数属性");
                		  }else if(data.returnCode==1){
                			  layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
                			  tdObj.html(text);
                			  $(currBtn).html(btnHtml);
                              $("#editTig").text("单击以修改参数属性");
                		  }else{
                			  layer.alert(data.msg, {icon: 5});
                		  }
                	  	
                  }); 
              };
              
              $("#cancelBtn").click(function(){
      			tdObj.html(text);
                  $(currBtn).html(btnHtml);
                  $("#editTig").text("单击以修改参数属性");
      		});
              
              $("#saveBtn").click(saveFn);
              
              $(document).keyup(function (event) {
                  var keycode = event.which;
                  //回车情况
                  if (keycode==13) {
                	  saveFn();                                     
                  }
                  //ESC情况
                  if (keycode==27) {
                      tdObj.html(text);
                      $(currBtn).html(btnHtml);
                      $("#editTig").text("单击以修改参数属性");
                  }
                  
              });
              
              
              
	}