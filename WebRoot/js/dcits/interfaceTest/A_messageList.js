var table;
    var messageId;
    var thisUrl = "message-list";
    $(document).ready(function(){
    	var tpl = $("#tpl").html();
        //预编译模板
        var template = Handlebars.compile(tpl);
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 1, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
    				//手动控制遮罩
	    			$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "message-list",
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
            "lengthMenu": [[10, 15, 100], ['10', '15', '100']],  //显示数量设置
            "columns":[
                {"data":null},
                {"data":"messageId"},
                {"data":null},
                {"data":null},                
                {"data":"createTime","width":"120px"},
                {"data":null},
                {"data":"createUserName"},
                {"data":"lastModifyUser"},
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
				    "targets":2,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.interfaceName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
				{
                    "targets":3,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.messageName||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
				{
				    "targets":9,
				    "render":function(data, type, full, meta ){
				    	/* var htmlContent = '<a href="javaScript:showMessageScene('+data.messageId+');">'+data.sceneNum+'</a>';
                        return htmlContent; */
				    	var context =
                        {
                            func: [
                                {"name": data.sceneNum, "fn": "showMessageScene(\'"+data.interfaceName+"\',\'"+data.messageName+"\',\'"+data.messageId+"\')", "type": "default"}
                            ]
                        };
                        var html = template(context);
                        return html;
				    }
				
				},
                {
                    "targets":5,
                    "render":function(data, type, full, meta ){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.status=="0"){
                    		bstatus = "可用";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "禁用";
                    		btnstyle = "disabled";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {
                    "targets":0,
                    "render":function(data, type, full, meta){
                        var htmlContent = '<input type="checkbox" name="'+data.messageName+'" value="'+ data.messageId+'" class="selectMessage">';
                        return htmlContent;
                    }

                },
                {
                    "targets":8,
                    "render":function(data, type, full, meta){
                    	var context =
                        {
                            func: [
                                {"name": "获取", "fn": "getParamsJson(\'"+data.messageName+"入参报文\',\'A_messageParams.html\',\'"+data.messageId+"\')", "type": "primary"}
                            ]
                        };
                        var html = template(context);
                        return html;
                    }

                },
                {
                    "targets":10,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="message_edit(\'报文编辑\',\'A_messageEdit.html\',\''+data.messageId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="message_del(this,\''+data.messageId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[0,8,10]}// 不参与排序的列
            ]
        });
    });
	

	/*批量删除报文*/
	function message_batchDel(){
		//console.log($(":checkbox[checked]"));
		var checkboxList = $(".selectMessage:checked");
		if(checkboxList.length<1){
			return false;
		}
		$wrapper.spinModal();
		var delCount = 0;
		var delFlag = 0;
 		$.each(checkboxList,function(i,n){
			messageId=$(n).val();
			messageName=$(n).attr("name");
			$.ajax({
				type:"GET",
				url:"message-del",
				data:{messageId:messageId},
				async: false,
				success:function(data){
					if(data.returnCode==0){
						delCount = i+1;					
					}else{
						delFlag = 1;
						layer.alert("在删除报文"+messageName+"时发生了意外,请稍后再试", {icon: 2});
						return;
					}
				}
			});
		}); 		
 		table.ajax.reload(null,false);
  		if(delFlag==0){
  			parent.layer.msg('删除了'+delCount+'条记录',{icon:1,time:1500});
  		}	
	}

	/*打开场景列表*/
	function showMessageScene(interfaceName,messageName,id){
		var index = layer.open({
            type: 2,
            title: interfaceName+"-"+messageName,
            content: "A_messageSceneList.html?messageId="+id+"&interfaceName="+interfaceName+"&messageName="+messageName
        });
        layer.full(index);
	}

	/*管理报文的参数*/
	function getParamsJson(title,url,id,w,h){
		$("#selectMessageId").val(id);
		layer_show(title,url,'800','500');
	}

    /*报文-添加*/
    function message_add(title,url,w,h){
    	var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*报文-编辑*/
    function message_edit(title,url,id,w,h){
    	$("#selectMessageId").val(id);
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*报文-删除*/
    function message_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("message-del",{messageId:id},function(data){
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