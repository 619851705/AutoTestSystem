var table;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 7, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "POST",
        		            url: "task-list",
        		            cache : false,	//禁用缓存
        		            dataType: "json",
        		            success: function(result) {
        		            		if(result.returnCode==0){
        		            			if(result.schedulerFlag=="0"){
        		            				$("#schedulerBtn").addClass("disabled").removeClass("btn-primary");
        		            				$("#schedulerBtn").attr("disabled","disabled");
        		            			}       		            			
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
                {"data":"taskId"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"runCount"}, 
                {"data":"lastFinishTime"}, 
                {"data":"createTime"},
                {"data":null},
                {"data":null},
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
				    	data = data.taskName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
				{
				    "targets":2,
				    "render":function(data, type, full, meta){
				    	var taskTypeName = '';
				    	var typeIcon = '';
				    	switch (data.taskType) {
						case "0":
							taskTypeName = "接口自动化";
							break;
						case "1":
							taskTypeName = "Web自动化";
							break;
						case "2":
							taskTypeName = "APP自动化";
							break;
						}
				    	return taskTypeName;
				    }
				
				},
				{
				    "targets":3,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.setName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":4,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	if(data.status=="0"){
                    		data = data.taskCronExpression||"";
				    						return '<span title="' + data + '">' + data + '</span>';
                    	}else{
                        	return '<a class="btn btn-success radius size-S"  href="javascript:settingRule(\''+data.taskId+'\',\''+data.taskCronExpression+'\');" title="定时规则">设定</a>';

                    	}
                    }

                },
                {
                    "targets":8,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.status=="0"){
                    		bstatus = "运行中";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "已关闭";
                    		btnstyle = "disabled";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {
                    "targets":9,
                    "render":function(data, type, full, meta){
                    	var context = '';
                    	var statusIcon = '&#xe631;';
                    	var statusTitle = '关闭';
                    	if(data.status!="0"){
                    		statusIcon = '&#xe6e6;';
                    		statusTitle = '开启';
                    		var context = '<a style="text-decoration:none" class="ml-5" onClick="taskEdit(\''+data.taskId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont" style="font-size:1.5em;">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="taskDel(this,\''+data.taskId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont" style="font-size:1.5em;">&#xe609;</i></a>';
                    	}
                    	context='<a style="text-decoration:none;" class="ml-5" onClick="changeTask(\''+data.taskId+'\',\''+data.status+'\')" href="javascript:;" title="'+statusTitle+'"><i class="Hui-iconfont" style="font-size:1.5em;">'+statusIcon+'</i></a>&nbsp;'+context;
                    	return context;
                    }

                },
                {"orderable":false,"aTargets":[9]}// 不参与排序的列
            ]
        });
    });
    
    //编辑定时规则
    function settingRule(taskId,rule){
    	layer.confirm("请选择编辑模板:",{title:"定时规则编辑",btn:["填写CRON表达式","可视界面选择"]},
    		function(index){
    		layer.prompt({
    			  formType: 0,
    			  value: rule,
    			  title: '请输入CRON表达式',
    			}, function(value, index, elem){   			  
    				updateRule(index,taskId,value);   			  
    			});
    		},
    		function(index){
    			layer_show("编辑定时规则/CRON表达式", "editCronExpression.html?taskId="+taskId+"&taskCronExpression="+rule, "800", "600");
    			layer.close(index);
    	});
    }
    
    function updateRule(index,taskId,rule){
    	$.post("task-updateExpression",{taskId:taskId,taskCronExpression:rule},function(data){
    		if(data.returnCode==0){   			
    			layer.close(index);
    			layer.msg('更新成功!',{icon:1,time:1500});
    		}else{
    			layer.alert(data.msg,{icon:5});
    		}
    		
    	});
    }
    
   //增加任务
   function addTask(){
	   layer_show("添加定时任务","autoTaskEdit.html?modeFlag=0", "800", "600");
   }
   
   //删除任务
   function taskDel(obj,taskId){
	   layer.confirm("确定要删除该定时任务吗？",function(index){
	       	$.get("task-del",{taskId:taskId},function(data){
	       		if(data.returnCode==0){
	       			table.row($(obj).parents('tr')).remove().draw();
	                layer.msg(data.msg,{icon:1,time:1500});
	       		}else{
	       			layer.alert(data.msg, {icon: 5});
	       		}
	       	});
	           
	       });
   }
    
  	//编辑任务信息
  	function taskEdit(taskId){
  		layer_show("编辑任务","autoTaskEdit.html?modeFlag=1&taskId="+taskId, "800", "600");  		
  	}
  	
    
   //启动或者停止任务
   function changeTask(taskId,status){
	   var url = "task-addTask";
	   var sendTip = "确定启动该定时任务吗？(启动之前请仔细检查定时规则)";
	   if(status=="0"){
		   url = "task-stopTask";
		   sendTip = "确定要停止该定时任务吗?";
	   }	   
	   layer.confirm(sendTip,function(index){
       	$.get(url,{taskId:taskId},function(data){
       		if(data.returnCode==0){
       			table.ajax.reload(null,false);
                layer.msg(data.msg,{icon:1,time:1500});
       		}else{
       			layer.alert(data.msg, {icon: 5});
       		}
       	});
           
       });
   }
  	
   //帮助
   function cronExpressionTip(){
	   var tip = '<div style="margin:5px;padding:5px;border:1px solid green;">'+'<strong>字段&nbsp;&nbsp;&nbsp;允许值&nbsp;&nbsp;&nbsp;允许的特殊字符</strong><br>秒&nbsp;&nbsp;&nbsp;'+
	   '&nbsp;0-59&nbsp;&nbsp;&nbsp;&nbsp;,&nbsp;-&nbsp;*&nbsp;/<br>分&nbsp;&nbsp;&nbsp;&nbsp;0-59&nbsp;&nbsp;'+
	   '&nbsp;&nbsp;,&nbsp;-&nbsp;*&nbsp;/<br>小时&nbsp;&nbsp;&nbsp;&nbsp;0-23&nbsp;&nbsp;&nbsp;&nbsp;,&nbsp;'+
	   '-&nbsp;*&nbsp;/<br>日期&nbsp;&nbsp;&nbsp;&nbsp;1-31&nbsp;&nbsp;&nbsp;&nbsp;,&nbsp;-&nbsp;*&nbsp;?&nbsp;/&nbsp;'+
	   'L&nbsp;W&nbsp;C<br>月份&nbsp;&nbsp;&nbsp;&nbsp;1-12&nbsp;或者&nbsp;JAN-DEC&nbsp;&nbsp;&nbsp;,&nbsp;-&nbsp;'+
	   '*&nbsp;/<br>星期&nbsp;&nbsp;&nbsp;&nbsp;1-7&nbsp;或者&nbsp;SUN-SAT&nbsp;&nbsp;&nbsp;&nbsp;,&nbsp;-&nbsp;*&nbsp;?'+
		'&nbsp;/&nbsp;L&nbsp;C&nbsp;#<br>年（可选）&nbsp;留空,&nbsp;1970-2099&nbsp;&nbsp;&nbsp;,&nbsp;-&nbsp;*&nbsp;/&nbsp;'+
		'<br>-&nbsp;区间&nbsp;&nbsp;<br>*&nbsp;通配符&nbsp;&nbsp;<br>?&nbsp;你不想设置那个字段<br><br>下面只例出几个式子<br>&nbsp;'+
		'<br><strong>CRON表达式&nbsp;&nbsp;&nbsp;&nbsp;含义&nbsp;</strong><br><span style="color:red;">"0&nbsp;0&nbsp;12&nbsp;*&nbsp;*&nbsp;?"&nbsp;&nbsp;&nbsp;&nbsp;</span>'+
		'每天中午十二点触发&nbsp;<br><span style="color:red;">"0&nbsp;15&nbsp;10&nbsp;?&nbsp;*&nbsp;*"&nbsp;&nbsp;&nbsp;&nbsp;</span>每天早上10：15触发&nbsp;<br><span style="color:red;">"'+
		'0&nbsp;15&nbsp;10&nbsp;*&nbsp;*&nbsp;?"&nbsp;&nbsp;&nbsp;&nbsp;</span>每天早上10：15触发&nbsp;<br><span style="color:red;">"0&nbsp;15&nbsp;10&nbsp;*&nbsp;'+
		'*&nbsp;?&nbsp;*"&nbsp;&nbsp;&nbsp;&nbsp;</span>每天早上10：15触发&nbsp;<br><span style="color:red;">"0&nbsp;15&nbsp;10&nbsp;*&nbsp;*&nbsp;?&nbsp;2005"</span>'+
		'&nbsp;&nbsp;&nbsp;&nbsp;2005年的每天早上10：15触发&nbsp;<br><span style="color:red;">"0&nbsp;*&nbsp;14&nbsp;*&nbsp;*&nbsp;?"</span>&nbsp;&nbsp;&nbsp;&nbsp;'+
		'每天从下午2点开始到2点59分每分钟一次触发&nbsp;<br><span style="color:red;">"0&nbsp;0/5&nbsp;14&nbsp;*&nbsp;*&nbsp;?"</span>&nbsp;&nbsp;&nbsp;&nbsp;每天从下'+
		'午2点开始到2：55分结束每5分钟一次触发&nbsp;<br><span style="color:red;">"0&nbsp;0/5&nbsp;14,18&nbsp;*&nbsp;*&nbsp;?"</span>&nbsp;&nbsp;&nbsp;&nbsp;每天的下午'+
		'2点至2：55和6点至6点55分两个时间段内每5分钟一次触发&nbsp;<br><span style="color:red;">"0&nbsp;0-5&nbsp;14&nbsp;*&nbsp;*&nbsp;?"</span>&nbsp;&nbsp;&nbsp;&nbsp;'+
		'每天14:00至14:05每分钟一次触发&nbsp;<br><span style="color:red;">"0&nbsp;10,44&nbsp;14&nbsp;?&nbsp;3&nbsp;WED"</span>&nbsp;&nbsp;&nbsp;&nbsp;三月的每周三的'+
		'14：10和14：44触发&nbsp;<br><span style="color:red;">"0&nbsp;15&nbsp;10&nbsp;?&nbsp;*&nbsp;MON-FRI"</span>&nbsp;&nbsp;&nbsp;&nbsp;每个周一、周二、周三、周四、周五'+
		'的10：15触发'+'</div>';
	   
	   
	   layer.open({
		   type:1,
		   title:"CRON表达式帮助",
		   area:['800px', '640px'],
		   content:tip
	   });
   }
   
   //启动任务调度中心
   function schedulerSwitch(){
	   $.post("task-startAllTasks",function(data){
		   if(data.returnCode==0){
			   $("#schedulerBtn").addClass("disabled").removeClass("btn-primary");
			   $("#schedulerBtn").attr("disabled","disabled");
			   layer.msg("启动成功!",{icon:1,time:1500});
		   }else{
			   layer.alert(data.msg,{icon:5});
		   }
	   });
   }
   
   
  //刷新表格
	function refreshTable(){
		table.ajax.reload(null,false);
	}