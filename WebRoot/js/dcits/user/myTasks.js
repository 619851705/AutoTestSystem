 var table;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){     	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 2, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "webTest-taskList",
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
            "lengthMenu": [[5, 10, 30], ['5', '10', '30']],  //显示数量设置
            "columns":[
                {"data":null},
                {"data":null},
                {"data":"submitTime"},
                {"data":"finishTime"},                               
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":0,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.taskName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":1,
                    "render":function(data, type, full, meta ){
                    	var type = '';
                    	if(data.testMode=="0"){
                    		type = '测试用例';
                    	}else{
                    		type = '测试集';
                    	}
                        return type; 
                    }
                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	switch(data.status)
                    	{
                    	case "0":
	                   		bstatus = "测试完成";
	                   		btnstyle = "success";
                    	  	break;
                    	case "1":
                    		bstatus = "待测试";
	                   		btnstyle = "default";
                    	  	break;
                    	case "2":
                    		bstatus = "正在测试";
	                   		btnstyle = "primary";
                      	  	break;
                    	case "3":
                    		bstatus = "配置错误";
	                   		btnstyle = "danger";
                      	  	break;
                    	case "4":
                    		bstatus = "系统错误";
	                   		btnstyle = "danger";
                      	  	break;
                    	case "5":
                    		bstatus = "已过期";
	                   		btnstyle = "danger";
                      	  	break;
                    	}
                        return htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                    }

                }
            ]
        });
    });