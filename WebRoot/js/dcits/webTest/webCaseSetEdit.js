 var table; 
    var setId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){ 
    	setId=GetQueryString("setId");
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 3, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "caseSet-getSetCase?setId="+setId,
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
        	"autoWidth": true,   //自动宽度
            "responsive": true,   //自动响应
            "lengthMenu": [[7, 10, 50], ['7', '10', '50']],  //显示数量设置           
            "columns":[
                {"data":"caseId"},
                {"data":null},
                {"data":"username"},
                {"data":null},
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
				    	data = data.caseName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="runWebCase('+data.caseId+')" href="javascript:;" title="执行测试用例"><i class="Hui-iconfont" >&#xe603;</i></a><a style="text-decoration:none" class="ml-5" onClick="delSetComp(\''+data.id+'\')" href="javascript:;" title="删除关联关系"><i class="Hui-iconfont">&#xe6e2;</i></a>';             	
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var htmlContent='';
                    	switch(data.status)
                    	{
                    	case "0":
	                   		htmlContent = '<span class="label label-success radius">可用</span>';
                    	  	break;
                    	case "1":
	                   		htmlContent = '<a href="javascript:;" onclick="handleCase(\''+data.id+'\');"><span class="label label-primary radius">待审核</span></a>';
                    	  	break;
                    	}
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[5]}// 不参与排序的列
            ]
        });
    });
 
//处理审核请求
function handleCase(id){
	layer.confirm('请处理此条审核请求', {
		  btn: ['审核通过', '打回']
		}, function(index, layero){
			updateCompStatus(id,"0");
		}, function(index){
			updateCompStatus(id,"2");
		});
}

function runWebCase(caseId){
	$.get("webTest-runTest?caseId="+caseId,function(data){
		if(data.returnCode==0){			
			layer.alert('已发送测试请求,请关注当前测试任务进度',{icon:1});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}


function delSetComp(id){
	layer.confirm('确认从该测试集中解除该条测试用例的关联关系吗?',{icon:3,title:'确认删除'},function(index){
		layer.close(index);
		updateCompStatus(id,"3");
	});
}

function updateCompStatus(id,status){
	$.post("caseSet-updateCompStatus",{id:id,status:status},function(data){
		  if(data.returnCode==0){
			  table.ajax.reload(null,false);
			  layer.msg('更新成功',{icon:1,time:2000});
		  }else{
			  layer.alert(data.msg,{icon:5});
		  }
	  });
}

//编辑测试集信息
function editSetInfo(){	
	layer_show("编辑测试集信息","webCaseSetInfoEdit.html?setId="+setId,'800','340');
	
}