var table;
    var $wrapper = $('#div-table-container');
    var taskType = GetQueryString("taskType");
    var ajaxUrl = "";
    switch (taskType) {
	case "0":
		ajaxUrl = "set-list";
		break;
	case "1":
		ajaxUrl = "caseSet-list";
		break;
	case "2":
		ajaxUrl = "appSet-list";
		break;
	}
    
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 1, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "POST",
        		            url: ajaxUrl,
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
            "lengthMenu": [[8, 15, 100], ['8', '15', '100']],  //显示数量设置
            "columns":[
                {"data":"setId"},
                {"data":"setName"},
                {"data":null},
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [  				
                {
                    "targets":2,
                    "render":function(data, type, full, meta){                  
                    	return '<a href="javascript:;" class="btn btn-danger size-S radius" onclick="choose(\''+data.setId+'\',\''+data.setName+'\');">选择</a>';
                    }

                },
                {"orderable":false,"aTargets":[2]}// 不参与排序的列
            ]
        });
    });
    
    
    function choose(id,name){
    	parent.$("#relatedId").val(id);
		parent.$("#setNameText").text(name);	
		parent.layer.close(parent.layer.getFrameIndex(window.name));
    }