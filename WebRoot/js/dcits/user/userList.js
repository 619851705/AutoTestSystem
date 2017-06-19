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
        		            url: "user-list",
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
                {"data":"userId"},
                {"data":"username"},
                {"data":"realName"}, 
                {"data":"role.roleName"}, 
                {"data":null},
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
                    		bstatus = "锁定";
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
                    	data = data.lastLoginTime||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":6,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.createTime||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var statusIcon='&#xe605;';
                    	if(data.status=="0"){
                    		statusIcon='&#xe60e;';
                    	}
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="resetPassword(\''+data.userId+'\',\''+data.username+'\')" href="javascript:;" title="重置密码"><i class="Hui-iconfont">&#xe68f;</i></a> <a style="text-decoration:none" class="ml-5" onClick="userEdit(\'用户信息编辑\',\'userEdit.html\',\''+data.userId+'\',\''+data.username+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="userLock(\''+data.userId+'\',\''+data.username+'\',\''+data.status+'\')" href="javascript:;" title="锁定"><i class="Hui-iconfont">'+statusIcon+'</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[7]}// 不参与排序的列
            ]
        });
    });
    
    //重置密码
    function resetPassword(userId,userName){
    	layer.confirm('确定要重置该用户的密码吗？',function(index){
    		$.get("user-resetPwd",{userId:userId},function(data){
    			if(data.returnCode==0){
                    layer.msg('密码已重置为111111',{icon:1,time:1000});
        		}else{
        			layer.alert(data.msg, {icon: 5});
        		}
    		});
    		
    	});
    }
    
    //用户增加
    function addUser(){
    	layer_show("增加用户", "userEdit.html?modeFlag=0", "600", "300");
    }
    
  	//用户编辑
  	function userEdit(title,url,userId,userName){
		if(userName=="admin"){
			layer.msg('不能修改预置管理员用户信息!',{time:1500});
		}else{
			layer_show(title, "userEdit.html?modeFlag=1&userId="+userId, "600", "480");
		}
		
    }
  	
    //用户删除
    function userLock(userId,userName,status){
    	if(userName=="admin"){
    		layer.msg('不能锁定预置管理员用户!',{time:1500});
    	}else{
    		var mode = "0";
        	var tipMsg = "确定需要解锁该用户吗?";
        	var tipMsg1 = "该用户已解锁!";
        	if(status=="0"){
        		mode = "1";
        		tipMsg = "确认要锁定该用户吗(请谨慎操作,被锁定的用户将不能登录)";
        		tipMsg1 = "已锁定该用户,该用户将不能登录!";
        	}   	
        	layer.confirm(tipMsg,function(index){
        		layer.close(index);
            	$wrapper.spinModal();
            	$.get("user-lock",{userId:userId,userName:userName,mode:mode},function(data){
            		if(data.returnCode==0){
            			table.ajax.reload(null,false);
                        layer.msg(tipMsg1,{icon:1,time:1000});
            		}else{
            			$wrapper.spinModal(false);
            			layer.alert(data.msg, {icon: 5});
            		}
            	});
                
            });
    	} 	
    }
    
  	
  //刷新表格
	function refreshTable(){
		table.ajax.reload(null,false);
	}