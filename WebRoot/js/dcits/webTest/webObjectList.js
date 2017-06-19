var table; 
    var categoryId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	$.fn.dataTable.ext.errMode = 'none';
    	categoryId=GetQueryString("categoryId");
    	var url;
    	if(categoryId==null){
    		url="webObject-list";
    		$("#showFunction").css("display","none");
    	}else{
    		url="webObject-list?categoryId="+categoryId;
    	}
    	
        table = $('.table-sort').on( 'error.dt', function ( e, settings, techNote, message ) {
            layer.alert("加载数据的时候发生了错误,错误详情:\n"+message);
        	}).DataTable({
            "aaSorting": [[ 0, "desc" ]],//默认第几个排序
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
                {"data":"objectId"},
                {"data":null},
                {"data":"objectType"},
                {"data":"how"},
                {"data":null},
                {"data":"objectSeq"},
                {"data":null},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [                 
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context;
                    	if(categoryId!=null){
                    		context = '<a style="text-decoration:none" class="ml-5" onClick="editWebObject(\'修改测试对象\',\'webObjectEdit.html\',\''+data.objectId+'\')" href="javascript:;" title="修改"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delWebObject(this,\''+data.objectId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                    	}else{
                        	context = '<input type="button" onclick="chooseObject(\''+data.objectId+'\',\''+data.objectName+'\');" class="btn btn-danger radius size-S" value="选择"/>';
                    	}
                        return context;
                    }

                },
                {
                    "targets":1,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.objectName||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":4,
                    "className":"ellipsis",
                    "render":function(data, type, full, meta){
                    	data = data.propertyValue||"";
                    	return '<span title="' + data + '">' + data + '</span>';
                    }

                },
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	if(data.pageUrl==""||data.pageUrl==null){    		
                    		return "";
                    	}
                    	var context = '<a href="'+data.pageUrl+'" target="_blank" title="'+data.pageUrl+'">打开</a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[1,4,6,7]}// 不参与排序的列
            ]
        });
    });
    
	//增加测试对象
	function addWebObject(){
		var index = layer.open({
            type: 2,
            title: "增加测试对象",
            content: "webObjectEdit.html?modeFlag=0&categoryId="+categoryId
        });
        layer.full(index);
	}
    
	//修改测试对象
	function editWebObject(title,url,objectId){
		var index = layer.open({
            type: 2,
            title: "修改测试对象",
            content: "webObjectEdit.html?modeFlag=1&objectId="+objectId+"&categoryId="+categoryId
        });
        layer.full(index);
	}
	
	//删除测试对象
	function delWebObject(obj,objectId){
		layer.confirm('删除测试对象会导致与之关联的测试步骤和测试用例都不可用,确认要删除吗？',function(index){
	    	$.get("webObject-del",{objectId:objectId},function(data){
	    		layer.close(index);
	        	$wrapper.spinModal();
	    		if(data.returnCode==0){
	    			$wrapper.spinModal(false);
		    		  table.row($(obj).parents('tr')).remove().draw();
		              layer.msg('已删除',{icon:1,time:1500});
	    		}else if(data.returnCode==1){
	    			layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
	    		}else{
	    			$wrapper.spinModal(false);
        			layer.alert(data.msg, {icon: 5});
	    		}
	    	});
	        
	    });
	}
   
	//选择测试对象-测试步骤
	function chooseObject(objectId,objectName){
		parent.$("#clearObjectBtn").attr("type","button");
		parent.$("#objectId").val(objectId);
		parent.$("#objectNameText").text(objectName);
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}