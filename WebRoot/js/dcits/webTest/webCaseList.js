var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 4, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "webCase-list",
        		            cache : false,	//禁用缓存
        		            dataType: "json",
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
                {"data":"caseId"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"createTime"},
                {"data":null},
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
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="runWebCase('+data.caseId+')" href="javascript:;" title="执行测试用例"><i class="Hui-iconfont" >&#xe603;</i></a><a style="text-decoration:none" class="ml-5" onClick="addToSet('+data.caseId+')" href="javascript:;" title="添加到测试用例集合"><i class="Hui-iconfont" >&#xe6dc;</i></a><a style="text-decoration:none" class="ml-5" onClick="editWebCase(\''+data.caseId+'\')" href="javascript:;" title="修改"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delWebCase(this,\''+data.caseId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var context = '<input type="button" onclick="showSteps(\''+data.caseName+'\',\''+data.caseId+'\');" class="btn btn-success-outline radius size-S" value="'+data.stepNum+'"/>';
                        return context;
                    }

                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '<input type="button" onclick="showCaseDesc(\''+data.caseDesc+'\');" class="btn btn-danger radius size-S" value="查看"/>';
                        return context;
                    }

                },
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var context = '<img src="../../libs/myjs/images/'+data.browser+'.png"  alt="'+data.browser+'" height="20px" width="20px"/>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[5,6]}// 不参与排序的列
            ]
        });
    });
 
//添加到测试用例集合
function addToSet(caseId){
	var index = layer.open({
        type: 2,
        title: "添加到测试集",
        content: "webCaseSetList.html?caseId="+caseId
    });
    layer.full(index);
}
    
    
//测试
function runWebCase(caseId){
	var url = "webTest-runTest?caseId="+caseId;
	$.get(url,function(data){
		if(data.returnCode==0){			
			layer.alert('已发送测试请求,请到web自动化测试客户端进行本地测试执行操作!',{icon:1});
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}
//删除
function delWebCase(obj,caseId){
	layer.confirm('删除测试用例的同时也会删除与之关联的所有测试步骤和测试报告,确认要删除吗？',function(index){
		$wrapper.spinModal();
    	$.get("webCase-delCase",{caseId:caseId},function(data){
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
function showCaseDesc(desc){
	layer.alert(desc,{icon:0,title:'测试用例说明'});
	
}
	
//打开测试步骤页面
function showSteps(caseName,caseId){
	var index = layer.open({
        type: 2,
        title: caseName+"-测试步骤",
        content: "webStepList.html?caseId="+caseId
    });
    layer.full(index);
}

//新增测试用例
function addWebCase(){
	layer_show("新增测试用例","webCaseEdit.html?modeFlag=0",'800','500');
}


//编辑测试用例
function editWebCase(caseId){
	layer_show("编辑测试用例","webCaseEdit.html?modeFlag=1&caseId="+caseId,'800','500');
}

//查看我的审核记录
function showMyAuditRecord(){
	layer_show("审核记录","myCaseAuditRecord.html",'850','450');
}