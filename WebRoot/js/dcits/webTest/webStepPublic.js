var table; 
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
        		            url: "publicStep-listCategory",
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
                {"data":"categoryId"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"useCount"},
                {"data":"createUser"},
                {"data":"submitTime"},
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
				    	data = data.categoryName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="editStepCategory(\''+data.categoryId+'\',\''+data.categoryDesc+'\')" href="javascript:;" title="修改步骤库分类详情"><i class="Hui-iconfont" >&#xe6df;</i></a><a style="text-decoration:none" class="ml-5" onClick="delStepCategory(this,\''+data.categoryId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var context = '<input type="button" onclick="showSteps(\''+data.categoryName+'\',\''+data.categoryId+'\');" class="btn btn-success-outline radius size-S" value="'+data.stepNum+'"/>';
                        return context;
                    }

                },
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var listTag=new Array();
              			if(data.categoryTag!=null&&data.categoryTag!=""){
              				listTag=data.categoryTag.split(",");
              			}
              			var htmlContent='';
              			for(var i=0;i<listTag.length;i++){
              				htmlContent+='<span class="label label-success radius">'+listTag[i]+'</span>&nbsp;';
              			}
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[3,7]}// 不参与排序的列
            ]
        });
    });

    
//审核列表  
function auditRecord(){
  	layer_show("审核推荐步骤库", "stepCategoryAudit.html", '850', '600');
}
  
  
//修改信息
function editStepCategory(categoryId,categoryDesc){
	layer.prompt({
		  formType: 2,
		  value: categoryDesc,
		  title: '你只能修改步骤库备注说明'
		}, function(value, index, elem){
			$.post('publicStep-editCategory',{categoryId:categoryId,categoryDesc:value},function(data){
				if(data.returnCode==0){
					table.ajax.reload(null,false);					
					layer.close(index);
					layer.msg('更新成功',{icon:1,time:2000});
				}else{
					layer.alert(data.msg,{icon:5});
				}
			});
		  
		});
}   
    
//显示包含步骤
function showSteps(categoryName,categoryId){
	var index = layer.open({
        type: 2,
        title: categoryName+"-测试步骤",
        content: "webStepList.html?categoryId="+categoryId
    });
    layer.full(index);
}   
    
//删除
function delStepCategory(obj,categoryId){
	layer.confirm('删除该步骤库不会影响到已经使用过的测试用例,确定要删除吗?',function(index){
		layer.close(index);
    	$wrapper.spinModal();
    	$.get("publicStep-delStepCategory",{categoryId:categoryId},function(data){
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