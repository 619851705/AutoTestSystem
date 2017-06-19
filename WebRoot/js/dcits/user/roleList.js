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
        		            url: "role-list",
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
                {"data":"roleId"},
                {"data":"roleGroup"},
                {"data":"roleName"},                
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
                    "targets":3,
                    "render":function(data, type, full, meta ){
                    	var htmlContent = '<a href="javascript:;" onclick="showRolePower(\''+data.roleId+'\',\''+data.roleName+'\');" class="btn btn-default size-S radius">'+data.oiNum+'</a>';
                        return htmlContent; 
                    }
                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="roleEdit(\'角色编辑\',\'roleEdit.html\',\''+data.roleId+'\',\''+data.roleName+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="roleDel(this,\''+data.roleId+'\',\''+data.roleName+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":4,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.mark||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {"orderable":false,"aTargets":[4,5]}// 不参与排序的列
            ]
        });
    });
    
    
    //角色增加
    function addRole(){
    	layer_show("增加角色信息", "roleEdit.html?modeFlag=0", "600", "400");
    }
    
  	//角色编辑
  	function roleEdit(title,url,roleId,roleName){
  		if(roleName=="admin"){
  			layer.msg('不能修改预置管理员角色信息!',{time:1500});
  		}else{
  			layer_show("编辑角色信息", "roleEdit.html?modeFlag=1&roleId="+roleId, "600", "430");
  		}
  		
    }
  	
    //角色删除
    function roleDel(obj,roleId,roleName){
    	if(roleName!="admin"){
    		layer.confirm('确认要删除吗？',function(index){
        		layer.close(index);
            	$wrapper.spinModal();
            	$.get("role-del",{roleId:roleId},function(data){
            		if(data.returnCode==0){
            			$wrapper.spinModal(false);
            			table.row($(obj).parents('tr')).remove().draw();
                        layer.msg('已删除',{icon:1,time:1500});
            		}else{
            			$wrapper.spinModal(false);
            			layer.msg(data.msg, {time:1500});
            		}
            	});
                
            });
    	}else{
    		layer.msg('不能删除预置管理员角色!',{time:1500});
    	}
    	
    	
    }
    
  	//权限详情
  	function showRolePower(roleId,roleName){ 		
    	layer_show("角色权限编辑", "rolePowerEdit.html?roleId="+roleId+"&roleName="+roleName, "400", "660");
    }
  	
  //刷新表格
	function refreshTable(){
		table.ajax.reload(null,false);
	}