 var table;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 0, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "db-list",
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
                {"data":"dbId"},
                {"data":"dbType"},
                {"data":"dbName"},                
                {"data":"dbUsername"},
                {"data":null},
                {"data":"dbUrl"},
                {"data":null},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [   
                {
                    "targets":4,
                    "render":function(data, type, full, meta ){
                     	var htmlContent = '<a href="javascript:;" onclick="layer.alert(\''+data.dbPasswd+'\',{icon:4,title:\'数据库密码查看\'});">*******</a>';
                        return htmlContent; 
                    }
                },
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var context = '<a href="javascript:;" onclick="layer.alert(\''+data.dbMark+'\',{icon:0,title:\'数据库备注查看\'});" class="btn btn-success size-S radius">查看</a>';
                        return context;
                    }

                },
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="dbTest(\''+data.dbId+'\')" href="javascript:;" title="测试连接"><i class="Hui-iconfont">&#xe6f1;</i></a><a style="text-decoration:none" class="ml-5" onClick="dbEdit(\'数据库信息编辑\',\'queryDBEdit.html\',\''+data.dbId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="dbDel(this,\''+data.dbId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                    	return context;
                    }

                },
                {"orderable":false,"aTargets":[2,3,4,5,7]}// 不参与排序的列
            ]
        });
    });
    
    
    //数据库信息增加
    function addDatabaseInfo(){
    	layer_show("数据库信息增加", "queryDBEdit.html?modeFlag=0", "800", "550");
    }
    
  	//数据库信息编辑
  	function dbEdit(title,url,dbId){
  		layer_show(title, url+"?modeFlag=1&dbId="+dbId, "800", "550");
    }
  	
  	//连接测试
  	function dbTest(dbId){
  		$wrapper.spinModal();
  		$.get("db-testDB",{dbId:dbId},function(data){
  			$wrapper.spinModal(false);
  			if(data.returnCode==0){ 				
  				layer.alert("测试连接成功!", {icon: 1});
  			}else if(data.returnCode==2){
    			layer.alert("尝试连接失败,请检查数据相关信息是否正确?", {icon: 5});
  			}else{
    			layer.alert(data.msg, {icon: 5});
  			}			
  		});
  	}
  	
    //数据库信息删除
    function dbDel(obj,dbId){
    	layer.confirm('确认要删除吗？删除之后某些被关联的测试用例和测试集可能会无法正常执行,请谨慎操作!',function(index){
    		layer.close(index);
        	$wrapper.spinModal();
        	$.get("db-del",{dbId:dbId},function(data){
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
  	
  //刷新表格
	function refreshTable(){
		table.ajax.reload(null,false);
	}