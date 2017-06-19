var sendTable;
var receiveTable;
var $wrapper = $('#div-table-container');
$(function(){
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");	
	
	receiveTable = $('.receiveMails').DataTable({
        "aaSorting": [[ 0, "desc" ]],//默认第几个排序
        "bStateSave": true,//状态保存
        "processing": false,   //显示处理状态
		"serverSide": false,  //服务器处理
		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
    			//手动控制遮罩
				$wrapper.spinModal();
    			$.ajax({
    		            type: "POST",
    		            url: "mail-listMails?mailType=1",
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
            {"data":"mailId"},
            {"data":"sendUserName"},
            {"data":null},                
            {"data":null},
            {"data":"sendTime"},
            {"data":null}
        ],
        //行回调
        "createdRow": function ( row, data, index ){
            $(row).addClass('text-c');
        },
        "columnDefs": [
			{
			    "targets":2,
			    "className":"ellipsis",
			    "render":function(data, type, full, meta ){
			    	var info = '';
			    	if(data.ifValidate=="1"){
			    		info = data.mailInfo||"";
			    	}else{
			    		info = "这是一份加密邮件,你需要验证密码才能查看内容,点击验证!";
			    	}
			    	return '<a href="javascript:;" onclick="viewMail(\''+data.mailId+'\',\''+data.ifValidate+'\');" title="'+info+'">'+info+'</a>';
			    }
			},
            {
                "targets":3,
                "render":function(data, type, full, meta ){
                	var bstatus;
                	var btnstyle;
                	switch(data.readStatus)
                	{
                	case "0":
                   		bstatus = "已读";
                   		btnstyle = "success";
                	  	break;
                	case "1":
                		bstatus = "未读";
                   		btnstyle = "default";
                	  	break;               
                	}
                    return htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>'; 
                }
            },
            {
                "targets":5,
                "render":function(data, type, full, meta){
                    return  "";
                }

            },
            {"orderable":false,"aTargets":[2,5]}// 不参与排序的列
        ]
    });
	
	sendTable = $('.sendMails').DataTable({
        "aaSorting": [[ 0, "desc" ]],//默认第几个排序
        "bStateSave": true,//状态保存
        "processing": false,   //显示处理状态
		"serverSide": false,  //服务器处理
		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
    			//手动控制遮罩
				$wrapper.spinModal();
    			$.ajax({
    		            type: "POST",
    		            url: "mail-listMails?mailType=2",
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
            {"data":"mailId"},
            {"data":"receiveUserName"},
            {"data":null},                
            {"data":null},
            {"data":"sendTime"},
            {"data":null}
        ],
        //行回调
        "createdRow": function ( row, data, index ){
            $(row).addClass('text-c');
        },
        "columnDefs": [
			{
			    "targets":2,
			    "className":"ellipsis",
			    "render":function(data, type, full, meta ){
			    	var info = data.mailInfo||"";
			    	return '<a href="javascript:;" onclick="editSendMail(\''+data.mailId+'\',\''+data.sendStatus+'\');" title="'+info+'">'+info+'</a>';
			    }
			},
            {
                "targets":3,
                "render":function(data, type, full, meta ){
                	var bstatus;
                	var btnstyle;
                	switch(data.sendStatus)
                	{
                	case "0":
                   		bstatus = "已发送";
                   		btnstyle = "success";
                	  	break;
                	case "1":
                		bstatus = "未发送";
                   		btnstyle = "default";
                	  	break;               
                	}
                    return htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>'; 
                }
            },
            {
                "targets":5,
                "render":function(data, type, full, meta){
                	var html = '';
                	if(data.sendStatus=="1"){
                		html+='<a style="text-decoration:none" class="ml-5" onClick="editSendMail(\''+data.mailId+'\',\'1\')"'+
                        ' href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a><a style="text-decoration:none" class="ml-5" onClick="delSendMail(\''+data.mailId+'\')"'+
                        ' href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                	}                   
                    return html;
                }

            },
            {"orderable":false,"aTargets":[2,5]}// 不参与排序的列
        ]
    });
	
});

function setReaded(mailId){
    	$.post("mail-changeStatus",{mailId:mailId,statusName:"readStatus",status:"0"},function(data){
    		if(data.returnCode==0){
    			receiveTable.ajax.reload(null,false);
    		}else{
    			layer.alert(data.msg, {icon: 5});
    		}
    		
    	});
}

function viewMail(mailId,validateFlag){
	if(validateFlag=="0"){
		layer.prompt({
			  formType: 1,
			  value: '',
			  title: '验证密码'
			}, function(value, index, elem){
			  $.post("user-verifyPasswd",{password:value},function(data){
				 if(data.returnCode==0){
					layer.close(index);					
					$.post("mail-changeStatus",{mailId:mailId,statusName:"ifValidate",status:"1"},function(data){
			    		if(data.returnCode==0){
			    			setReaded(mailId);			    			
			    		}else{
			    			layer.alert(data.msg, {icon: 5});
			    		}			    		
			    	});	
					layer_show("查看邮件", "mailDetail.html?modeFlag=0&mailId="+mailId, "800", "600");
				 }else{
					 layer.alert(data.msg,{icon:5});
				 }
			  });
			});
	}else{
		layer_show("查看邮件", "mailDetail.html?modeFlag=0&mailId="+mailId, "800", "600");
		setReaded(mailId);
	}	
}

function editSendMail(mailId,sendFlag){
		var title = "编辑邮件";
		if(sendFlag=="0"){
			title = "查看邮件";
		}
		layer_show(title, "mailDetail.html?modeFlag="+sendFlag+"&mailId="+mailId, "800", "600");

}

function addMail(){
	layer_show("发送邮件", "mailDetail.html?modeFlag=1", "800", "600");
}

function batchDel(){
	
}


function batchOp(mode){
	
}


function delSendMail(mailId){
	 layer.confirm('确认要删除吗？',function(index){
     	layer.close(index);
     	$wrapper.spinModal();
     	$.get("mail-del",{mailId:mailId},function(data){
     		if(data.returnCode==0){
     			sendTable.ajax.reload(null,false);
                 layer.msg('删除成功',{icon:1,time:1500});
     		}else{
     			$wrapper.spinModal(false);
     			layer.alert(data.msg, {icon: 5});
     		}
     	});
         
     });
}

function refreshTable(mode){
	if(mode=="1"){
		sendTable.ajax.reload(null,false);
	}else{
		receiveTable.ajax.reload(null,false);
	}
}