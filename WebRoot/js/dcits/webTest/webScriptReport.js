	
	var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){      	
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
        		            url: "webScript-listReports",
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
                {"data":"reportId"},
                {"data":"reportName"},
                {"data":"caseNum"},
                {"data":"testUserName"},
                {"data":"testTime"},
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
				    	var htmlContent = '<input type="checkbox" name="'+data.reportName+'" value="'+ data.reportId+'" class="selectReport">';
                        return htmlContent;
				    }
				
				},
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	var html="";
                    	if(data.testMark!=""&&data.testMark!=null){
                        	var html = '<a class="btn btn-secondary  radius size-S" href="javascript:layer.prompt({formType: 2,title:\''+data.reportName+'测试备注\',value:\''+data.testMark+'\',area: [\'350px\', \'260px\']}, function(value, index, elem){layer.close(index);});">查看</a>';
                    	}
                    	return html;
                    }

                },
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" target="_blank" href="../../files/rubyReport/'+data.reportPath+'" title="查看"><i class="Hui-iconfont">&#xe6f2;</i></a> <a style="text-decoration:none" class="ml-5" onClick="delReport(this,\''+data.reportId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[0,6,7]}// 不参与排序的列
            ]
        });
    });
 
    
    //批量删除
    function batchDel(){
    	var checkboxList = $(".selectReport:checked");
    	batchDelObjs(checkboxList,"webScript-delReport");
    }
    
    //删除脚本
    function delReport(obj,id){
    	delObj("确定要删除该测试报告？","webScript-delReport",{objId:id},obj);
    }
    
    