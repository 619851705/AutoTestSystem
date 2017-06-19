var table;
    var setId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){   	
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
        		            url: "set-list",
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
                {"data":null},
                {"data":"setId"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"createTime"},               
                {"data":"userName"},
                {"data":"mark","className":"my_td_class"},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":7,
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
                    "targets":3,
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
                        var htmlContent = '<input type="checkbox" name="'+data.setName+'" value="'+ data.setId+'" class="selectSet">';
                        return htmlContent;
                    }

                },
                {
                    "targets":8,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="set_edit(\'测试集编辑\',\'A_setEdit.html\',\''+data.setId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="set_del(this,\''+data.setId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":2,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.setName||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var context = '<a href="javascript:;" onclick="showSetScenes(\''+data.setName+'\',\''+data.setId+'\')" class="btn btn-default size-S radius">'+data.sceneNum+'</a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[0,7,8]}// 不参与排序的列
            ]
        });
    });
	

	/*批量删除测试集*/
	function set_batchDel(){
		//console.log($(":checkbox[checked]"));
		var checkboxList = $(".selectSet:checked");
		if(checkboxList.length<1){
			return false;
		}
		$wrapper.spinModal();
		var delCount = 0;
		var delFlag = 0;
		
 		$.each(checkboxList,function(i,n){
			setId=$(n).val();
			setName=$(n).attr("name");
			$.ajax({
				type:"GET",
				url:"set-del",
				data:{setId:setId},
				async:false,
				success:function(data){
					if(data.returnCode==0){
						delCount = i+1;
					}else{
						delFlag = 1;
						layer.alert("在删除测试集"+setName+"时发生了意外,请稍后再试", {icon: 2});
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
	function showSetScenes(name,id){
		$("#selectSetId").val(id);
		var index = layer.open({
            type: 2,
            title: name,
            content: "A_setSceneList.html",
        });
        layer.full(index);
	}


    /*测试集-添加*/
    function set_add(){
    	layer.prompt({
    		  formType: 0,
    		  value: '',
    		  title: '请输入你要创建的测试集名称:'
    		}, function(value, index, elem){
  				$.post("set-save",{setName:value},function(data){
  					if(data.returnCode==0){ 						
  					/* var html='<tr class="text-c"><td><input type="checkbox" name="'+data.set.setName+'" value="'+data.set.setId+'" class="selectSet">'
								+'</td><td>'+data.set.setId+'</td><td>'+data.set.setName+'</td><td><span class="label label-success radius">可用</span>'
								+'</td><td><a href="javascript:;" onclick="showSetScenes(\''+data.set.setName+'\'\''+data.set.setId+'\')" class="btn btn-default size-S radius">0</a>'
								+'</td><td>'+data.set.createTime+'</td><td>'+data.set.userName+'</td><td class=" my_td_class"></td><td>'
								+'<a style="text-decoration:none" class="ml-5" onclick="set_edit(\'测试集编辑\',\'A_setEdit.html\',\''+data.set.setId+'\')" '
								+'href="javascript:;" title="编辑"><i class="Hui-iconfont"></i></a> <a style="text-decoration:none" class="ml-5" '
								+'onclick="set_del(this,\''+data.set.setId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont"></i></a></td></tr>';
  						html+=$("#tbodyS").html();
  						$("#tbodyS").html(html);  */
  						table.ajax.reload(null,false);
  						layer.close(index);
  						layer.msg('增加成功,你可以在列表选择测试集来进行修改添加场景的操作',{icon:1,time:2000});
  					}else if(data.returnCode==1){
  						layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
  					}else if(data.returnCode==2){
  						layer.msg('已存在相同名称的测试集,请更换名称',{icon:5,time:1500});
  					}else{
  						parent.layer.alert(data.msg, {icon: 5});
  					}
  				});  	

    		  //layer.close(index);
    		});
    }
    /*测试集-编辑*/
    function set_edit(title,url,id,w,h){
     	$("#selectSetId").val(id);
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index); 
    	
    }
    /*测试集-删除*/
    function set_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("set-del",{setId:id},function(data){
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