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
        		            url: "caseSet-auditRecord",
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
            "lengthMenu": [[7, 10, 50], ['7', '10', '50']],  //显示数量设置           
            "columns":[
                {"data":"id"},
                {"data":null},
                {"data":null},
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
				    	data = data.setName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
				{
				    "targets":2,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.caseName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '';
                    	if(data.status=="0"){
                    		context='';
                    	}else if(data.status=="1"){
                    		context='<input type="button" onclick="remindAudit(\''+data.id+'\');" class="btn btn-danger radius size-S" value="提醒审核"/>';
                    	}else{
                    		context='<input type="button" onclick="resubmit(\''+data.id+'\');" class="btn btn-danger radius size-S" value="重新提交"/>';
                    	}              	
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	switch(data.status)
                    	{
                    	case "0":
	                   		bstatus = "成功";
	                   		btnstyle = "success";
                    	  	break;
                    	case "1":
                    		bstatus = "待审核";
	                   		btnstyle = "primary";
                    	  	break;
                    	case "2":
                    		bstatus = "被打回";
	                   		btnstyle = "default";
                      	  	break;
                    	case "3":
                    		bstatus = "已删除";
	                   		btnstyle = "danger";
                      	  	break;
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[5]}// 不参与排序的列
            ]
        });
    });
    
//提醒审核
function remindAudit(id){
	layer.msg("提醒成功,请等待管理员审核");
}

//重新提交审核
function resubmit(id){
	$.post("caseSet-updateCompStatus",{id:id,status:"1"},function(data){
		if(data.returnCode==0){						
			table.ajax.reload(null,false);
			layer.msg("提交成功,请等待审核");
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}