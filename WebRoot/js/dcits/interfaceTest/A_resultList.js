 var table;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){
    	var reportId=parent.$("#selectReportId").val();
    	var mode=parent.$("#selectMode").val();   
    	
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
        		            url: "report-showResult?reportId="+reportId+"&mode="+mode,
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
            "iDisplayLength": 5,
            "columns":[
                {"data":"resultId"},
                {"data":null},
                {"data":null},                
                {"data":"statusCode"},
                {"data":"useTime"},
                {"data":null},
                {"data":"opTime"}               
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
				    	data = (data.messageInfo.split(","))[0];
				    	return  '<span title="' + data + '">' + data + '</span>';
				    },
				    "createdCell":function(td, cellData, rowData, row, col){
				    			var infoList=rowData.messageInfo.split(",");
				    			var data="[Message]-"+infoList[1]+"<br>[Scene]-"+infoList[2];
								var index;
						        $(td).mouseover(function(){
						        	index=layer.tips(data, td, {
			  		  				tips: [1, '#3595CC']
						        		});				        
						        });
						        $(td).mouseout(function(){
						        	layer.close(index);
						        });
											    						    					    	
				    }
				
				}, 
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var runstatus="";
                    	var btnstyle="";
                    	switch(data.runStatus){
                    	case "0":
                    		runstatus="success";
                    		btnstyle="success";
                    		break;
                    	case "1":
                    		runstatus="fail";
                    		btnstyle="danger";
                    		break;
                    	case "2":
                    		runstatus="stop";
                    		btnstyle="disabled";
                    		break;               
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+runstatus+'</span>';
                        return htmlContent;
                    }

                },              
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '<a href="javascript:;" onclick="showResultDetail(\''+data.resultId+'\');" class="btn btn-success radius">查看</a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[5]}// 不参与排序的列
            ]
        });
    });
    
    function showResultDetail(resultId){
    	$.get("report-showResultDetail?resultId="+resultId,function(data){
    		if(data.returnCode==0){
    			var showHtml='<table class="table table-bg"><tr>'+
    			'<td><strong>请求地址:</strong></td><td>'+data.result.requestUrl+'</td></tr><tr><td><strong>入参报文:</strong></td><td></td></tr>'+
    			'<tr><td colspan="2"><textarea style="height: 150px;" class="textarea radius">'+data.result.requestMessage+'</textarea></td></tr><tr><td><strong>出参报文:</strong></td><td>'+
    			'</td></tr><tr><td colspan="2"><textarea style="height: 150px;" class="textarea radius">'+data.result.responseMessage+'</textarea></td></tr>'+    			
    			'<tr><td><strong>测试备注:</strong></td><td></td></tr><tr><td colspan="2"><textarea style="height: 150px;" class="textarea radius">'+data.result.mark+'</textarea></td></tr></table>';
    			parent.layer.open({
    				  title: '测试结果详情',
    				  shade: 0,
    				  type: 1,
    				  skin: 'layui-layer-rim', //加上边框		
    				  area: ['700px', '500px'], //宽高
    				  content: showHtml
    				});
    		}else if(data.returnCode==1){
    			layer.msg('服务器内部错误',{icon:2,time:1500});
    		}else{
    			layer.alert(data.msg,{icon:5});
    		}
    	});  	
    }