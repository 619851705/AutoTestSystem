 var table; 
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
        		            url: "webReport-listReportSet",
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
                {"data":"reportSetId"},
                {"data":null},
                {"data":"testDetails.caseNum"},
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
				    	data = data.setName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="createReportHtml(\'Web自动化测试报告\',\'webReportHtml.html\',\''+data.reportSetId+'\')" href="javascript:;" title="报告生成"><i class="Hui-iconfont">&#xe6f2;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delWebSetReport(this,\''+data.reportSetId+'\')" href="javascript:;" title="删除测试集报告"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	var context;
                    	if(data.testDetails.executeNum!=0){
                        	context = '<a href="javascript:;" onclick="showCaseReport(\''+data.setName+'`'+data.testTime+'\',\''+data.reportSetId+'\');" class="btn btn-success size-S radius">'+data.testDetails.executeNum+'</a>';
                    	}else{
                        	context = '<a disabled="true" href="javascript:;"  class="btn disabled radius size-S">'+data.testDetails.executeNum+'</a>';
                    	}
                        return context;
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var per=0;
                    	if(data.testDetails.executeNum!=0){
                    		per=Math.round((data.testDetails.successNum/data.testDetails.executeNum)*100);
                    	}                  	
                    	return 	showTestdetails("success",per);
                        
                    }

                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var per=0;
                    	if(data.testDetails.executeNum!=0){
                    		per=Math.round((data.testDetails.failNum/data.testDetails.executeNum)*100);
                    	} 
                    	return showTestdetails("fail",per);
                    }

                },
                {"orderable":false,"aTargets":[7]}// 不参与排序的列
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
    
    function showTestdetails(status,per){
    	var btnstyle='';
    	if(status=="success"){
    		btnstyle = "success";
    	}else{
    		btnstyle = "danger";
    	}
        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+per+'%</span>';
        return htmlContent;
    }
    
    
    //删除测试报告
    function delWebSetReport(obj,reportSetId){    	
    	layer.confirm('确认要删除吗？',function(index){
    		layer.close(index);
        	$wrapper.spinModal();
	    	$.get("webReport-delReportSet",{reportSetId:reportSetId},function(data){
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
    function createReportHtml(title,url,reportSetId){
    	window.open("webReportHtml.html?reportSetId="+reportSetId);  
    }
    
    //打开测试用例报告列表
    function showCaseReport(title,reportSetId){
    	var index=layer.open({
    		title:title,
    		type:2,
    		content:"webReportList.html?reportSetId="+reportSetId
    	});
    	layer.full(index);
    }
    
    //高级筛选
    function advanceFilterReport(){
    	layer.msg('尚未完成');
    }