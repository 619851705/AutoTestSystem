 var table;
    var setId;
    var getSetMode;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){
    	setId=parent.$("#selectSetId").val();
    	getSetMode=parent.$("#getSetMode").val();
    	var url="set-getScenes?setId="+setId;
    	if(getSetMode!=null&&getSetMode!=""){
    		url=url+"&getMode=1";
    	}
    	
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
        		            url: url,
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
                {"data":null},
                {"data":"mark","className":"my_td_class"},
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
                     	return '<span title="' + data.interfaceName + '">' + data.interfaceName + '</span>';
                     }
              	   
                 },
                 {
                	 "targets":3,
                	 "className":"ellipsis",
                     "render":function(data, type, full, meta){
                     	return '<span title="' + data.messageName + '">' + data.messageName + '</span>';
                     }
              	   
                 },
                 {
                	 "targets":4,
                	 "className":"ellipsis",
                     "render":function(data, type, full, meta){
                     	return '<span title="' + data.sceneName + '">' + data.sceneName + '</span>';
                     }
              	   
                 },
				{
				    "targets":5,
				    //td单元格回调
				    "createdCell":function(td, cellData, rowData, row, col){				    		
							if(cellData.replace(/[^\u0000-\u00ff]/g,"aa").length>35){
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
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var sname=data.interfaceName+'-'+data.messageName+'-'+data.sceneName;
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="messageScene_test(\''+sname+'\',\''+data.messageSceneId+'\')" href="javascript:;" title="测试"><i class="Hui-iconfont">&#xe603;</i></a><a style="text-decoration:none" class="ml-5" onClick="messageScene_edit(\'场景编辑\',\'A_messageSceneEdit.html\',\''+data.messageSceneId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="setScene_del(this,\''+data.messageSceneId+'\')" href="javascript:;" title="从该测试集中删除"><i class="Hui-iconfont">&#xe631;</i></a>';
                        return context;
                    }

                },            
                {"orderable":false,"aTargets":[0,5,6]}// 不参与排序的列
            ]
        });
    });
	

	/*批量删除场景*/
	function messageScene_batchDel(){
		//console.log($(":checkbox[checked]"));
		if(setId==0){
			layer.msg('你还不能这样做',{icon:5,time:1500});
			return;
		}
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
				url:"set-delScene",
				data:{messageSceneId:messageSceneId,setId:setId},
				async:false,
				success:function(data){
					if(data.returnCode==0){
						delCount = i+1;
					}else{
						delFlag = 1;
						layer.alert("在删除测试集场景"+messageSceneName+"时发生了意外,请稍后再试", {icon: 2});
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
    	if(setId==0){
			layer.msg('你还不能这样做',{icon:5,time:1500});
			return;
		}
		layer.open({
    		type: 2,
    		area: [w+'px', h +'px'],
    		fix: false, //不固定
    		maxmin: false,
    		shade:0.4,
    		title: title,
    		content: url,
    		end:function(){
    			$('#btn-refresh').click();
    		}
    	});
    }
    /*场景-编辑*/
    function messageScene_edit(title,url,id,w,h){
    	$("#selectMessageSceneId").val(id);
    	layer_show(title,url,'800','600');
    }
    /*场景-删除*/
    function setScene_del(obj,id){
    	if(setId==0){
			layer.msg('你还不能这样做',{icon:5,time:1500});
			return;
		}
        layer.confirm('确认要删除吗？\n该操作只会解除该场景与测试集的关联关系,不会真正删除场景',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("set-delScene",{messageSceneId:id,setId:setId},function(data){
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