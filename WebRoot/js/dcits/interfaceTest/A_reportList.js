 var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	$("#showBtn").click(showModeChange);
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 7, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "report-list",
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
                {"data":"reportId"},
                {"data":null},
                {"data":"sceneNum"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"createUserName"},
                {"data":"startTime"},
                {"data":"finishTime"},
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
                    "render":function(data, type, full, meta ){                     
                        var modeText;
                    	if(data.testMode=="0"){
                    		modeText = "全量测试";
                    	}else{
                    		modeText = '<span title="' + data.setName + '">' + data.setName + '</span>';
                    	}
                        /* var htmlContent = '<span class="label label-success radius">'+modeText+'</span>';
                        return htmlContent; */
                        return modeText;
                        
                    }

                },				                          
                {
                    "targets":3,
                    "render":function(data, type, full, meta ){
                        var htmlContent = '<a href="javascript:;" onclick="showResult(\'测试成功场景详情\',\''+data.reportId+'\',0);" class="btn btn-success radius">'+data.successNum+'</a>';
                        return htmlContent;
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var htmlContent = '<a href="javascript:;" onclick="showResult(\'测试失败场景详情\',\''+data.reportId+'\',1);" class="btn btn-warning radius">'+data.failNum+'</a>';
                        return htmlContent;
                    }

                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var htmlContent = '<a href="javascript:;" onclick="showResult(\'测试异常场景详情\',\''+data.reportId+'\',2);" class="btn btn-danger radius">'+data.stopNum+'</a>';
                        return htmlContent;
                    }

                },
                {
                    "targets":9,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="createReortHtml(\'测试报告\',\'A_reportHtml.html\',\''+data.reportId+'\',\''+data.sceneNum+'\')" href="javascript:;" title="报告生成"><i class="Hui-iconfont">&#xe6f2;</i></a> <a style="text-decoration:none" class="ml-5" onClick="downloadReport(\''+data.reportId+'\')" href="javascript:;" title="下载"><i class="Hui-iconfont">&#xe640;</i></a> <a style="text-decoration:none" class="ml-5" onClick="report_del(this,\''+data.reportId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[6,9]}// 不参与排序的列
            ]
        });
    });
    
    //查看测试详情
    function showResult(title,reportId,mode){
    	$("#selectReportId").val(reportId);
    	$("#selectMode").val(mode);
		layer_show(title,'A_resultList.html','800','600');
    }
    
    //生成html报告
    function createReortHtml(title,url,reportId,sceneNum){
    	if(sceneNum<1){
    		layer.alert("当前测试报告中没有任何测试详情结果",{icon:5});
    		return false;
    	}   	
    	window.open("A_reportHtml.html?reportId="+reportId);  
    }
    
    //下载报告
    function downloadReport(reportId){
		layer.msg('尚未完成',{icon:2,time:1500});

    }
    
    //只看我的
    function showModeChange(){
    	layer.alert("如果你需要此功能,请联系管理员");
    }
    
    //删除
    function report_del(obj,reportId){
    	layer.confirm('确定删除吗？',function(index){
    		layer.close(index);
        	$wrapper.spinModal();
    		$.get("report-del",{reportId:reportId},function(data){
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
   