var table;
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){
    	var reportCaseId=GetQueryString("reportCaseId");
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 5, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "webReport-stepReport?reportCaseId="+reportCaseId,
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
                {"data":"reportId"},
                {"data":null},
                {"data":null},                
                {"data":null},
                {"data":"testUsername"},
                {"data":"opTime"},
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
				    	data = data.stepName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":2,
                    "render":function(data, type, full, meta){
                    	var btnstyle="";
                    	switch(data.runStatus){
                    	case "success":
                    		btnstyle="success";
                    		break;
                    	case "fail":
                    		btnstyle="danger";
                    		break;
                    	case "error":
                    		btnstyle="danger";
                    		break;               
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+data.runStatus+'</span>';
                        return htmlContent;
                    }

                },              
                {
                    "targets":3,
                    "render":function(data, type, full, meta){
                    	if(data.capturePath==""||data.capturePath==null){
                    		return "";
                    	}
                    	var context = '<a href="../../screenshots/'+data.capturePath+'"  class="btn btn-danger size-S radius" target="_blank">查看</a>';
                        return context;
                    }

                },
                {
                    "targets":6,
                    "render":function(data, type, full, meta){
                    	if(data.testMark==""||data.testMark==null){
                    		return "";
                    	}
                    	var context = '<a href="javascript:;" onclick="showTestMark(\''+data.testMark.replace(/\n/ig, "<br>")+'\');" class="btn btn-danger size-S radius">查看</a>';
                        return context;
                    }

                },
                {"orderable":false,"aTargets":[1,3,6]}// 不参与排序的列
            ]
        });
    });
    
    function showTestMark(mark){
    	layer.open({
    		  title:'测试备注',
    		  type: 1,
    		  skin: 'layui-layer-rim', 
    		  area: ['600px', '360px'], 
    		  content: mark
    		});
    }