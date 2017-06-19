var table; 
    var reportSetId;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	reportSetId=GetQueryString("reportSetId");
    	var url="webReport-listReportCase";
    	if(reportSetId!=null){
    		url+="?reportSetId="+reportSetId;
    	}
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 5, "desc" ]],//默认第几个排序
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
                {"data":"reportCaseId"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"testTime"},
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
				    	data = data.caseName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="createReportHtml(\'Web自动化测试报告\',\'webReportHtml.html\',\''+data.reportCaseId+'\')" href="javascript:;" title="报告生成"><i class="Hui-iconfont">&#xe6f2;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delWebReport(this,\''+data.reportCaseId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var context = '<img src="../../libs/myjs/images/'+data.browser+'.png"  alt="'+data.browser+'" height="20px" width="20px"/>';
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var context = '<a href="javascript:;" onclick="showStepReport(\'测试报告详细\',\''+data.reportCaseId+'\');" class="btn btn-success size-S radius">'+data.stepNum+'</a>';
                        return context;
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.status=="success"){
                    		bstatus = "通过";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "失败";
                    		btnstyle = "danger";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[6]}// 不参与排序的列
            ]
        });
        $('.table-sort').on( 'click', 'tr', function () {
            if ( $(this).hasClass('selected') ) {
                $(this).removeClass('selected');
            }
            else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
        } );
    });
    
    //删除测试报告
    function delWebReport(obj,reportCaseId){    	
    	layer.confirm('确认要删除吗？',function(index){
    		layer.close(index);
        	$wrapper.spinModal();
	    	$.get("webReport-delReportCase",{reportCaseId:reportCaseId},function(data){
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

    //生成测试报告
    function createReportHtml(title,url,reportCaseId){
    	window.open("webReportHtml.html?reportCaseId="+reportCaseId);  
    }
    
    //打开详细测试步骤的测试报告
    function showStepReport(title,reportCaseId){
    	layer_show(title,"webStepReport.html?reportCaseId="+reportCaseId,'800','600');
    }
    
    //高级筛选
    function advanceFilterReport(){
    	layer.msg('尚未完成');
    }