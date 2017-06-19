	
	var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	
    	$("#switchDT").on("click",function(){
    		
    		if($(this).hasClass("btn-danger")){   
    			$(this).children("span").text("场景脚本");
    			$(this).removeClass("btn-danger").addClass("btn-success");
    			table.ajax.url( 'webScript-listScripts?fileKind=1' ).load(null,false);
    		}else{  
    			$(this).children("span").text("公共脚本");
    			$(this).removeClass("btn-success").addClass("btn-danger");
    			table.ajax.url( 'webScript-listScripts?fileKind=0' ).load(null,false);
    		}
    		
    	});
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 4, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "webScript-listScripts?fileKind=1",
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
                {"data":"scriptId"},
                {"data":null},
                {"data":"scriptAuthor"},
                {"data":"createTime"},
                {"data":"lastRunTime"},
                {"data":null},
                {"data":null}
            ],
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            },
            "columnDefs": [
				{
				    "targets":0,
				    "render":function(data, type, full, meta){
				    	var htmlContent = '<input type="checkbox" name="'+data.scriptPath+'" value="'+ data.scriptId+'" class="selectScript">';
                        return htmlContent;
				    }
				
				},
				{
				    "targets":2,
				    "className":"ellipsis",
				    "render":function(data, type, full, meta){
				    	data = data.scriptName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var html="";
                    	if(data.scriptDesc!=""&&data.scriptDesc!=null){
                        	var html = '<a class="btn btn-secondary  radius size-S" href="javascript:layer.prompt({formType: 2,title:\''+data.scriptName+'脚本备注\',value:\''+data.scriptDesc+'\',area: [\'350px\', \'260px\']}, function(value, index, elem){layer.close(index);});">查看</a>';
                    	}
                    	return html;
                    }

                },
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="editScript(\''+data.scriptId+'\')" href="javascript:;" title="修改"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delScript(this,\''+data.scriptId+'\',\''+data.scriptPath+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[0,6,7]}// 不参与排序的列
            ]
        });
    });
 
    //运行脚本
    function runScript(){
    	
    	var checkboxList = $(".selectScript:checked");
    	if(checkboxList.length<1){
    		layer.alert('至少选择一个需要执行的脚本!',{icon:5});
    		return false;
    	}
    	
    	if($("#switchDT").hasClass("btn-danger")){
    		layer.alert('不能单独执行公共脚本,请切换至场景脚本再重新操作!',{icon:5});
    		return false;
    	}
    	
    	var scriptNames="";
    	var scriptIds="";
    	$.each(checkboxList,function(i,n){
    		scriptNames+=$(n).attr("name");
    		scriptIds+=$(n).val();
    		if(i<(checkboxList.length-1)){
    			scriptNames+=",";
    			scriptIds+=",";
    		}
    	});
    	layer.confirm('确定执行选中的'+checkboxList.length+'个脚本？(执行过程中请不要关闭本页面)',function(index){
    		layer.close(index);
    		$wrapper.spinModal();
    		$.post('webScript-rubScripts',{scriptNames:scriptNames,scriptIds:scriptIds},function(data){
        		if(data.returnCode==0){
        			$wrapper.spinModal(false);
                    layer.msg('执行完成,请至报告模块查看测试报告!',{icon:1,time:1500});
        		}else{
        			$wrapper.spinModal(false);
        			layer.alert(data.msg, {icon: 5});
        		}
        	});
    	});
    }
    
    //批量删除
    function batchDel(){
    	var checkboxList = $(".selectScript:checked");
    	batchDelObjs(checkboxList,"webScript-delScript");
    }
    
    //删除脚本
    function delScript(obj,id,scriptPath){
    	delObj("确定要删除该脚本信息？","webScript-delScript",{scriptId:id},obj);
    }
    
    //脚本编辑
    function editScript(id){
    	layer_show("编辑脚本信息","webScriptEdit.html?modeFlag=1&scriptId="+id,'800','640');
    }
    
    //添加上传脚本
    function addScript(){
    	layer_show("添加脚本信息","webScriptEdit.html?modeFlag=0",'800','560');
    }
    