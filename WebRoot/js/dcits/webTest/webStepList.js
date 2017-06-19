var table; 
    var caseId;
    var categoryId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){     	
    	caseId=GetQueryString("caseId");
    	categoryId=GetQueryString("categoryId");
    	var url="publicStep-listStep?categoryId="+categoryId;
    	if(caseId!=null&&categoryId==null){
    		url="webCase-listStep?caseId="+caseId;
    		$("#opBar").show();
    	}
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 1, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: url,
        		            cache : false,	//禁用缓存
        		            dataType: "json",
        		            //async:false,
        		            success: function(result) {
        		            	if(result.returnCode==0){
        		            		$wrapper.spinModal(false);
        		            		$("#acquireStr").val(result.acquireStr);
            			            callback(result);
        		            	}else{
        		            		layer.alert(result.msg,{icon:5});
        		            	}
        		            },
        		            error: function(XMLHttpRequest, textStatus, errorThrown) {
        		                layer.alert('AJAX调用失败',{icon:5});
        		            }
        		        });
        		},
        	"autoWidth": false,   //自动宽度
            "responsive": true,   //自动响应
            "lengthMenu": [[10, 15, 100], ['10', '15', '100']],  //显示数量设置
            "columns":[
                {"data":"stepId"},
                {"data":"orderNum"},
                {"data":null},
                {"data":"stepMethod"},
                {"data":null},
                {"data":"callMethod"},
                {"data":null},
                {"data":null},
                {"data":null},              
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [ 
				{
				    "targets":7,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){				    	
				    	data = data.requireParameter.replace(/</ig, "&lt;").replace(/>/ig, "&gt;")||"";
                    	return '<span title="' + data + '">' + data + '</span>';
				    	
				    }
				
				},
				{
                    "targets":6,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.requireValue||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":9,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="editWebStep(\''+data.stepId+'\')" href="javascript:;" title="修改"><i class="Hui-iconfont">&#xe6df;</i></a> ';
                        if(caseId!=null){
                        	context+='<a style="text-decoration:none" class="ml-5" onClick="delWebStep(this,\''+data.stepId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        }
                    	return context;
                    }

                },
                {
                    "targets":4,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	var currObject=data.webObject;
                    	if(currObject==null){
                    		return "";
                    	}else{
                    		/* var context = '<a href="javascript:;" onClick="showStepObject(\''+currObject.objectName+'\',\''+currObject.objectType+'\',\''+currObject.how+'\',\''+currObject.propertyValue+'\',\''+currObject.objectSeq+'\',\''+currObject.pageUrl+'\');" title="'+currObject.objectName+'">'+currObject.objectName+'</a>';
                            return context; */
                            
                            data = currObject.objectName||"";
                        	return context = '<a href="javascript:;" onClick="showStepObject(\''+currObject.objectName+'\',\''+currObject.objectType+'\',\''+currObject.how+'\',\''+currObject.propertyValue+'\',\''+currObject.objectSeq+'\',\''+currObject.pageUrl+'\');" title="'+currObject.objectName+'"><span title="' + data + '">' + data + '</span></a>';
                            
                    	}
                    	
                    }

                },
                {
                    "targets":2,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	/* var context = '<a href="javascript:;" onclick="showStepDesc(\''+data.stepDesc+'\');" class="btn btn-danger size-S radius">查看</a>';
                        return context; */
                        data = data.stepDesc||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":8,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.capture=="0"){
                    		bstatus = "是";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "否";
                    		btnstyle = "disabled";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[2,9]}// 不参与排序的列
            ]
        });
    });



//步骤详情
function showStepDesc(desc){
	layer.alert(desc,{icon:1});
}

//展示object详情
function showStepObject(a,b,c,d,e,f){
	var str='<span style="color:red;">请至测试对象管理页面管理测试对象属性</span>';
	var showHtml='<table class="table table-bg"><tr>'+
	'<td><strong>对象名称：</strong></td><td>'+a+'</td></tr><tr><td><strong>对象类型：</strong></td><td>'+b+'</td></tr>'+
	'<tr><td><strong>对象获取方式：</strong></td><td>'+c+'</td></tr><tr><td><strong>获取参数：</strong></td><td>'+d+
	'</td></tr><tr><td><strong>查找顺序：</strong></td><td>'+e+'</td></tr><tr><td><strong>来源页面：</strong></td><td>'+f+'</td></tr><tr><td colspan="2">'+str+'</td></tr></table>';
	layer.open({
		  title: '测试对象详情',
		  type: 1,
		  skin: 'layui-layer-rim', //加上边框		
		  area: ['600px', '360px'], //宽高
		  content: showHtml
		});
}

//删除测试步骤
function delWebStep(obj,stepId){
	layer.confirm('删除测试步骤会导致测试用例的不完整,请确认是否删除?',function(index){
		layer.close(index);
    	$wrapper.spinModal();
    	$.get("webCase-delStep",{stepId:stepId,caseId:caseId},function(data){
    		if(data.returnCode==0){
    			$wrapper.spinModal(false);
	    		table.row($(obj).parents('tr')).remove().draw();
	            layer.msg('已删除',{icon:1,time:1500});
    		}else{
    			$wrapper.spinModal(false);
    			layer.alert(data.msg, {icon: 5});
    		}
    	});
        
    });
}


//修改测试步骤
function editWebStep(stepId){
	var content="webStepEdit.html?modeFlag=1&stepId="+stepId;
	if(categoryId!=null){
		content+="&publicStep=true";
	}
	var index = layer.open({
        type: 2,
        title: "修改测试步骤",
        content:content 
    });
    layer.full(index);
}

//增加新的测试步骤
function addStep(){
	layer.confirm('请选择添加方式(建议先到公共步骤库查找)',{title:'添加方式',btn:['从公共步骤库中选择','创建新的测试步骤']}
	,function(index){
		var index1 = layer.open({
	        type: 2,
	        title: "选择公共测试步骤",
	        content: "choosePublicStep.html?caseId="+caseId
	    });
	    layer.full(index1);
	    layer.close(index);
	}
	,function(index){
		var index1 = layer.open({
	        type: 2,
	        title: "增加新的测试步骤",
	        content: "webStepEdit.html?modeFlag=0&caseId="+caseId
	    });
	    layer.full(index1);
	});
		
	
}

var html1;
var index;
//测试步骤排序
function sortSteps(){
	html1='';
	//获取最新的steps
	$.get("webCase-listStep?caseId="+caseId,function(d){
		if(d.returnCode==0){
			var size=d.data.length;
			if(size<2){
				layer.msg('测试步骤太少了,增加几个再进行排序操作',{time:1500});
				return false;
			}
			var height=140+40*size;
			var html='<div><br>&nbsp;&nbsp;<input class="btn btn-danger radius" type="button" onclick="saveSort();" value="确认保存"/>&nbsp;&nbsp;<input class="btn btn-danger radius" type="button" onclick="resetSort();" value="还原"/><br><br><ul id="steps">';
			$.each(d.data,function(i,n){
				html1+='<li><input class="btn btn-info radius" type="button" name="'+n.stepId+'" value="'+n.stepDesc+'"></li>';
			});
			html+=html1+'</ul></div>';
			index=layer.open({
				  title: '请手动拖拽对测试步骤进行排序',
				  type: 1,
				  skin: 'layui-layer-rim', //加上边框		
				  area: ['350px', height+'px'], //宽高
				  content: html
				});		
			var el = document.getElementById('steps');
			new Sortable(el);
		}else{
			layer.alert(data.msg, {icon: 5});
		}
	});
}
 
function resetSort(){
	$("#steps").html(html1);
}

function saveSort(){
	var html2=$("#steps").html();
	if(html1==html2){
		layer.close(index);
		layer.msg('你未做更改');
		return;
	}
	var stepArrayNew=$("ul > li");
	var str='';
	$.each(stepArrayNew,function(i,n){
		str+=$(n).children().eq(0).attr("name");
		if(i!=(stepArrayNew.length-1)){
			str+=',';
		}
	});
	$.post("webCase-sortSteps",{sortStr:str},function(data){
		if(data.returnCode==0){
			layer.close(index);
			$("#btn-refresh").click();
			layer.msg('排序成功',{icon:1,time:1500});
		}else{
			layer.alert(data.msg,{icon:5});
		}
		
	});
	
	
}
//推荐我的测试步骤
function recommendSteps(){
	layer_show("推荐我的测试步骤","recommendSteps.html?caseId="+caseId,'500', '600');
}

//推荐审核查询
function myStepAuditRecord(){
	layer_show("审核记录","myStepAuditRecord.html",'850','450');
}