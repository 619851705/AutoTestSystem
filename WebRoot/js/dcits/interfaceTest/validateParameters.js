var table;
    var messageSceneId;
    var currMark;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){   
    	messageSceneId = GetQueryString("messageSceneId");
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 0, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "validate-listParameterValidate?messageSceneId="+messageSceneId,
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
                {"data":"validateId"},
                {"data":null},              
                {"data":null},
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
				    "targets":1,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.parameterName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},           
                {
                    "targets":2,
                    "render":function(data, type, full, meta ){
                    	if(data.getValueMethod=="0"){
                    		return "普通取值";
                    	}else if(data.getValueMethod=="1"){
                    		return "入参节点值";
                    	}else{
                    		return "数据库取值";
                    	}
                    }
                },
                {
                    "targets":3,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.validateValue||"";
				    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){   
                    	var checked = '';
                    	if(data.status=="0"){
                    		checked= 'checked';
                    	}                  	
                    	return '<div class="switch size-MINI" data-on-label="正常" data-off-label="禁用"><input type="checkbox" '+checked+' value="'+data.validateId+'"/></div>';    
                      
                    }

                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	return '<a href="javascript:;" onclick="showMark(\''+data.mark+'\');" class="btn btn-success size-MINI radius">查看</a>';
                    }

                },
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="validate_edit(\''+data.validateId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="validate_del(this,\''+data.validateId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[5,6]}// 不参与排序的列
            ],
            initComplete:function(){
            	$('.switch')['bootstrapSwitch']();
            	$(':checkbox').change(function(){
            		var flag = $(this).is(':checked');
            		var validateId = $(this).attr('value');
            		updateStatus(validateId,flag,this);
            	});
            }
        });
        
    });

    //更新status
    function updateStatus(validateId,flag,object){
    	var status = '1';
    	if(flag==true){
    		status = '0';
    	}
    	$.get('validate-updateStatus',{validateId:validateId,status:status},function(data){
    		if(data.returnCode!=0){
    			$(object).click();
    			layer.alert(data.msg,{icon:5});
    		}
    	});
    }
    
    
    function showMark(mark){
    	layer.prompt({
    		  formType: 2,
    		  value: mark,
    		  title: '备注'
    		}, function(value, index, elem){
    		  layer.close(index);
    		});
    }

    /*validate-添加*/
    function validate_add(){
    	layer_show('添加参数验证规则', 'validateParameterEdit.html?modeFlag=0&messageSceneId='+messageSceneId, '800', '600');
    }
    
    
    /*validate-编辑*/
    function validate_edit(id){
    	layer_show('编辑参数验证规则', 'validateParameterEdit.html?modeFlag=1&validateId='+id+'&messageSceneId='+messageSceneId, '800', '600');
    }
    /*mock-删除*/
    function validate_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
        	layer.close(index);
        	$wrapper.spinModal();
        	$.get("validate-del",{validateId:id},function(data){
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
		table.ajax.reload(function(json){
			$('.switch')['bootstrapSwitch']();
			$(':checkbox').change(function(){
        		var flag = $(this).is(':checked');
        		var validateId = $(this).attr('value');
        		updateStatus(validateId,flag,this);
        	});
		},false);

	}