 var table;
    var messageId;
    var messageName;
    var interfaceName;
	var $wrapper = $('#div-table-container');
    $(document).ready(function(){
    	messageId = GetQueryString("messageId");
    	messageName = GetQueryString("messageName");
    	interfaceName = GetQueryString("interfaceName");
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
        		            url: "messageScene-list?messageId="+messageId,
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
                {"data":"messageSceneId"},
                {"data":null},
                {"data":null},                
                {"data":"sceneName"},
                {"data":"validateMethodStr"}, 
                {"data":"mark","className":"my_td_class"},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":6,
				    //td单元格回调
				    "createdCell":function(td, cellData, rowData, row, col){				    		
							if(cellData.replace(/[^\u0000-\u00ff]/g,"aa").length>41){
								var index;
						        $(td).mouseover(function(){
						        	index=layer.tips(cellData, td, {
			  		  				tips: [1, '#3595CC']
						        		});				        
						        });
						        $(td).mouseout(function(){
						        	layer.close(index);
						        });
							}else{
								
							}				    						    					    	
				    }
				
				}, 
                {
                    "targets":0,
                    "render":function(data, type, full, meta){
                        var htmlContent = '<input type="checkbox" name="'+data.messageSceneName+'" value="'+ data.messageSceneId+'" class="selectMessageScene">';
                        return htmlContent;
                    }

                },              
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var sname=interfaceName+'-'+messageName+'-'+data.sceneName;
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="setValidateMethod(this,\''+data.messageSceneId+'\')" href="javascript:;" title="验证规则设定"><i class="Hui-iconfont">&#xe654;</i></a><a style="text-decoration:none" class="ml-5" onClick="messageScene_test(\''+sname+'\',\''+data.messageSceneId+'\')" href="javascript:;" title="测试"><i class="Hui-iconfont">&#xe603;</i></a><a style="text-decoration:none" class="ml-5" onClick="messageScene_edit(\'场景编辑\',\'A_messageSceneEdit.html\',\''+data.messageSceneId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="messageScene_del(this,\''+data.messageSceneId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":2,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = interfaceName||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":3,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = messageName||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },                
                {"orderable":false,"aTargets":[0,2,3,4,5,6,7]}// 不参与排序的列
            ]
        });
    });
	
	
	/*批量删除场景*/
	function messageScene_batchDel(){
		//console.log($(":checkbox[checked]"));
		var checkboxList = $(".selectMessageScene:checked");
		if(checkboxList.length<1){
			return false;
		}
		
		$wrapper.spinModal();
		var delCount = 0;
		var delFlag = 0;
 		$.each(checkboxList,function(i,n){
 			messageSceneId=$(n).val();
			messageSceneName=$(n).attr("name");
			$.ajax({
				type:"GET",
				url:"messageScene-del",
				data:{messageSceneId:messageSceneId},
				async: false,
				success:function(data){
					if(data.returnCode==0){
						delCount = i+1;
					}else{
						delFlag = 1;
						layer.alert("在删除报文场景"+messageSceneName+"时发生了意外,请稍后再试", {icon: 2});
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

    /*场景-添加*/
    function messageScene_add(title,url,w,h){
    	layer_show(title,url+"?messageId="+messageId,'800','500');
    }
    /*场景-编辑*/
    function messageScene_edit(title,url,id,w,h){
    	$("#selectMessageSceneId").val(id);
    	layer_show(title,url,'800','600');
    }
    /*场景-删除*/
    function messageScene_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("messageScene-del",{messageSceneId:id},function(data){
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
    
    /*场景-测试*/
    function messageScene_test(name,id){
    	$("#selectMessageSceneId").val(id);
    	layer_show(name+' 场景测试','A_sceneTest.html','800','500');
    }
    
    /*场景的验证规则设定*/
    function setValidateMethod(obj,id){
    	var validateMethod = $(obj).parent("td").siblings("td:eq(5)")[0];
    	layer.confirm('请选择对返回进行验证的方式',{title:'提示',btn:['默认全局验证','节点验证','全文验证'],btn3:function(index){
    		$.get('messageScene-changeValidateRule',{messageSceneId:id,validateRuleFlag:'2'},function(data){
    			if(data.returnCode==0){
    				table.cell(validateMethod).data('全文验证').draw(); 				
    				layer.confirm('操作成功,是否现在编辑验证信息?',function(index){
    					layer.close(index);
    					layer_show('全文验证管理','validateFullJson.html?messageSceneId='+id, '800', '520');
    				});
    			}else{
    				layer.alert(data.msg,{icon:5});
    			}
    		});
    		
    	}},function(index){
    		$.get('messageScene-changeValidateRule',{messageSceneId:id,validateRuleFlag:'0'},function(data){
    			if(data.returnCode==0){
    				table.cell(validateMethod).data('全局验证').draw();
    				layer.alert('操作成功!',{icon:1},function(index){
    					layer.alert('默认全局验证配置请至 测试执行->测试设置 模块设置[返回值确认]参数!',{icon:0});
    				});
    			}else{
    				layer.alert(data.msg,{icon:5});
    			}
    		});   		
    	},function(index){
    		$.get('messageScene-changeValidateRule',{messageSceneId:id,validateRuleFlag:'1'},function(data){
    			if(data.returnCode==0){
    				table.cell(validateMethod).data('节点验证').draw();
    				layer.confirm('操作成功,是否现在编辑验证规则信息?',function(index){
    					layer.close(index);
    					var index = layer.open({
    		                type: 2,
    		                title: "节点验证管理",
    		                content: 'validateParameters.html?messageSceneId='+id
    		            });
    		    		layer.full(index);
    				});
    			}else{
    				layer.alert(data.msg,{icon:5});
    			}
    		});
    		
    	});
    }