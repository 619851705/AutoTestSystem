 var table; 
    var caseId=GetQueryString("caseId");
    if(caseId==null){
    	$("#addSetBar").css("display","block");
    }
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){   
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 6, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "caseSet-list",
        		            cache : false,	//禁用缓存
        		            dataType: "json",
        		            //async:false,
        		            success: function(result) {
        		            	if(result.returnCode==0){
        		            		$wrapper.spinModal(false);
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
                {"data":"setId"},
                {"data":null},
                {"data":null},
                {"data":"testCount"},
                {"data":null},
                {"data":"createUser"},
                {"data":"createTime"},
                {"data":"lastModifyUser"},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":1,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.setName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":8,
                    "render":function(data, type, full, meta){
                    	var context='';
                    	if(caseId!=null){
                    		context='<a style="text-decoration:none" class="ml-5" onClick="addToSet('+data.setId+')" href="javascript:;" title="添加到测试集"><i class="Hui-iconfont" >&#xe61f;</i></a>';
                    	}else{
                    		context='<a style="text-decoration:none" class="ml-5" onClick="runWebCaseSet('+data.setId+')" href="javascript:;" title="执行测试用例集"><i class="Hui-iconfont" >&#xe603;</i></a><a style="text-decoration:none" class="ml-5" onClick="editWebCaseSet(\''+data.setId+'\')" href="javascript:;" title="修改测试用例集"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delWebCaseSet(this,\''+data.setId+'\')" href="javascript:;" title="删除测试用例集"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                    	}
                        return context;
                    }

                },
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var context = '<input type="button" onclick="showSetDesc(\''+data.setDesc+'\');" class="btn btn-danger radius size-S" value="查看"/>';
                        return context;
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.status=="0"){
                    		bstatus = "可用";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "禁用";
                    		btnstyle = "disabled";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[2,8]}// 不参与排序的列
            ]
        });
    });
 
//添加到测试用例集合
function addToSet(setId){
	layer.confirm('确认提交本测试用例到该测试集中？', {icon: 3, title:'提示'}, function(index){
		layer.close(index);
		  $.get("caseSet-addToSet",{setId:setId,caseId:caseId},function(data){
			  if(data.returnCode==0){
				  layer.msg('申请添加成功,请等待管理员审核,你也可以在测试用例管理页面查看最新状态.',{icon:1,time:2000});
			  }else{
				  layer.alert(data.msg,{icon:5});
			  }
		  });		  
		});  
}
   
   
//测试
function runWebCaseSet(setId){
	$.get("webTest-runTest",{setId:setId},function(data){
		if(data.returnCode==0){
			layer.alert('已发送测试请求,请关注当前测试任务进度',{icon:1});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}

//删除
function delWebCaseSet(obj,setId){
	layer.confirm('该操作将会导致同样删除与之关联的测试报告等内容,是否确认删除？', {icon: 3, title:'提示'}, function(index){
		layer.close(index);
    	$wrapper.spinModal();
		  $.get("caseSet-delSet",{setId:setId},function(data){
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

//展示详细说明
function showSetDesc(desc){
	layer.alert(desc,{icon:0,title:'测试用例集说明'});
	
}
	

//添加测试用例集
function addWebCaseSet(){
	layer.prompt({
		  formType: 0,
		  value: '新建测试用例集',
		  title: '输入新建测试用例集名称'
		}, function(value, index, elem){
		  $.get("caseSet-editSet",{setName:value},function(data){
			  if(data.returnCode==0){
				  table.ajax.reload(null,false);
				  layer.msg("增加成功",{icon:1,time:2000});
			  }else{
				  layer.alert(data.msg,{icon:5});
			  }
		  });
		});
		   
}

//编辑测试用例集
function editWebCaseSet(setId){
	layer_show("测试用例集详情","webCaseSetEdit.html?setId="+setId,'950','500');
}