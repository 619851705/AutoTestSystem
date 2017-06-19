var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){ 
    	
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
        		            url: "publicStep-listCategory",
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
            "lengthMenu": [[10, 15, 50], ['10', '15', '50']],  //显示数量设置           
            "columns":[
                {"data":"categoryId"},
                {"data":null},
                {"data":"stepNum"},
                {"data":null},
                {"data":null},
                {"data":"useCount"},
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
				    	data = data.categoryName||"";
				    	return '<span title="' + data + '">' + data + '</span>';
				    }
				
				},
                {
                    "targets":6,
                    "render":function(data, type, full, meta){                          
                    	var	context='<input type="button" onclick="chooseSteps(\''+data.categoryId+'\');" class="btn btn-danger radius size-S" value="选择"/>';           	
                        return context;
                    }

                },
                {
                    "targets":3,
                    "render":function(data, type, full, meta){                   	
                        var htmlContent = '<input type="button" onclick="showDesc(\''+data.categoryDesc+'\');" class="btn btn-primary radius size-S" value="查看"/>';
                        return htmlContent;                       
                    }

                },
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var listTag=new Array();
              			if(data.categoryTag!=null&&data.categoryTag!=""){
              				listTag=data.categoryTag.split(",");
              			}
              			var htmlContent='';
              			for(var i=0;i<listTag.length;i++){
              				htmlContent+='<span class="label label-success radius">'+listTag[i]+'</span>&nbsp;';
              			}
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[3,4,6]}// 不参与排序的列
            ]
        });
    });
    
//查看备注
function showDesc(str){
	layer.alert(str,{icon:6});
}

//选择
function chooseSteps(categoryId){
	var caseId=GetQueryString("caseId");
	var index = layer.load(0, {shade: [0.3, '#000']}); 
	$.get("publicStep-copySteps",{caseId:caseId,categoryId:categoryId},function(data){
		if(data.returnCode==0){
			parent.$("#btn-refresh").click();
			layer.close(index);
			parent.layer.close(parent.layer.getFrameIndex(window.name));
		}else{
			layer.close(index);
			layer.alert(data.msg,{icon:5});
		}
	});	
}