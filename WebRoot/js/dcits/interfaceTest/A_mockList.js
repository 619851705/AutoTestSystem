var table;
    var mockId;
    var baseUrl;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){       	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 6, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "mock-list",
        		            cache : false,	//禁用缓存
        		            dataType: "json",
        		            //async:false,
        		            success: function(result) {
        		            	if(result.returnCode==0){
        		            		$wrapper.spinModal(false);
        		            		baseUrl = result.home;
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
                {"data":"mockId"},
                {"data":null},
                {"data":null},                
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"callCount"},
                {"data":"createUserName"},
                {"data":"createTime"},
                {"data":null,}
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
				    	data = data.interfaceName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},           
				{
				    "targets":2,
				    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = baseUrl+data.mockUrl||"";
                    	return '<a href="'+data+'" target="_blank">'+'<span title="' + data + '">' + data + '</span></a>';
                    }			    				   				
				},
                {
                    "targets":3,
                    "render":function(data, type, full, meta ){
                    	var htmlContent = '<a href="javascript:;" onclick="showJson(\''+data.interfaceName+'出入参详情\',\''+data.mockId+'\');" class="btn btn-danger size-S radius">查看</a>';
                        return htmlContent; 
                    }
                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
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
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var text;
                    	if(data.ifValidate=="0"){
                    		text="是";
                    	}else{
                    		text="否";
                    	}
                    	return text;
                    }

                },
                {
                    "targets":9,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="mock_edit(\'mock信息编辑\',\'A_mockEdit.html\',\''+data.mockId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="mock_del(this,\''+data.mockId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[2,3,9]}// 不参与排序的列
            ]
        });
    });

	/*打开出入参详情*/
	function showJson(title,mockId){
		$.get("mock-get",{mockId:mockId},function(data){
			if(data.returnCode==0){
				var html='<table class="table table-bg"><tr>'+
				'<td style="font-size:1.25em;">入参报文:</td><td></td></tr>'+
				'<tr><td colspan="2"><textarea style="height: 150px;" class="textarea radius">'+data.mock.requestJson+'</textarea></td></tr><tr><td style="font-size:1.25em;">出参报文:</td><td>'+
				'</td></tr><tr><td colspan="2"><textarea style="height: 150px;" class="textarea radius">'+data.mock.responseJson+'</textarea></td></tr></table>';
				layer.open({
					title:title,
					type:1,
					skin: 'layui-layer-rim', //加上边框		
					area: ['700px', '500px'], //宽高
					content: html
				});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
		
	}


    /*mock-添加*/
    function mock_add(title,url,w,h){
    	var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*mock-编辑*/
    function mock_edit(title,url,id,w,h){
    	$("#selectMockId").val(id);
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*mock-删除*/
    function mock_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("mock-del",{mockId:id},function(data){
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