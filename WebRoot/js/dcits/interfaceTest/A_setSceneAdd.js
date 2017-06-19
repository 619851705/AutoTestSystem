 var table;
    var setId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){
    	setId=parent.parent.$("#selectSetId").val(); 
        table = $('.table-sort').dataTable({
            "aaSorting": [[ 0, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "set-getNotScenes?setId="+setId,
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
            "lengthMenu": [[5, 10], ['5', '10']],  //显示数量设置
            "columns":[
                {"data":"messageSceneId"},
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
				    	return '<span title="' + data.interfaceName + '">' + data.interfaceName + '</span>';
				    }
				
				},
				{
				    "targets":2,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	return '<span title="' + data.messageName + '">' + data.messageName + '</span>';
				    }
				
				},
				{
				    "targets":3,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	return '<span title="' + data.sceneName + '">' + data.sceneName + '</span>';
				    }
				
				},
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var context = '<div id="addFlagBtn"><a href="javascript:;" onclick="addScene(this,\''+data.messageSceneId+'\')" class="btn btn-danger size-S radius">添加</a></div>';
                        return context;
                    }

                },            
                {"orderable":false,"aTargets":[4]}// 不参与排序的列
            ]
        });
    });
    
    
    function addScene(obj,messageSceneId){
    	$.get("set-addScene",{messageSceneId:messageSceneId,setId:setId},function(data){       
    		if(data.returnCode==0){
    			var html='<span class="label label-success radius" ><i class="Hui-iconfont" style="font-size: 37px;">&#xe6a7;</i></span>'; 
    			$(obj).parents("#addFlagBtn").html(html);
    			layer.msg('添加成功',{icon:1,time:1500});   			
    		}else if(data.returnCode==1){
    			layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
    		}else{
    			layer.alert(data.msg, {icon: 5});
    		}
    	});
    }