 var table;
    var $wrapper = $('#div-table-container');
    
    $(".l").children("a").on("click",function(){
    	$(this).addClass("btn-primary").siblings("a").removeClass("btn-primary").addClass("btn-default");
    });
    
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 3, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "role-listOp?opType=1",
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
                {"data":"opId"},
                {"data":"opName"},
                {"data":null},                
                {"data":"parentOpName"},
                {"data":null},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":2,
				    "render":function(data, type, full, meta ){
				    	return '<a href="'+data.callName+'" target="_blank">'+data.callName+'</a>';
				    }
				},
                {
                    "targets":4,
                    "render":function(data, type, full, meta ){
                    	var bstatus;
                    	var btnstyle;
                    	switch(data.status)
                    	{
                    	case "0":
	                   		bstatus = "正常";
	                   		btnstyle = "success";
                    	  	break;
                    	case "1":
                    		bstatus = "禁用";
	                   		btnstyle = "danger";
                    	  	break;               
                    	}
                        return htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>'; 
                    }
                },
                {
                    "targets":5,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.mark||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {"orderable":false,"aTargets":[5]}// 不参与排序的列
            ]
        });
    });
    
   //展示不同的操作接口列表
   //1 - 接口自动化模块
   //2 - Web自动化模块
   //3 - APP自动化模块
   //4 - 系统管理模块
   //5 - 权限管理模块
   function showOsp(opType){
	   table.ajax.url('role-listOp?opType='+opType).load();
   }
   
  	
  //刷新表格
	function refreshTable(){
		table.ajax.reload(null,false);
	}