var table; 
    var $wrapper = $('#div-table-container');
    $(document).ready(function(){  
    	
        table = $('.table-sort').DataTable({
            "aaSorting": [[ 6, "asc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
        			//手动控制遮罩
    				$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: "publicStep-categoryAudit",
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
            "lengthMenu": [[7, 10, 50], ['7', '10', '50']],  //显示数量设置           
            "columns":[
                {"data":"categoryId"},
                {"data":null},
                {"data":"createUser"},
                {"data":null},
                {"data":null},
                {"data":null},
                {"data":"submitTime"},                
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
                    "targets":3,
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
                {
                    "targets":4,
                    "render":function(data, type, full, meta){
                    	var htmlContent='<button class="btn btn-danger radius size-S" onclick="showDesc(\''+data.categoryDesc+'\');">查看</button>';
                    	return htmlContent;
                    }

                },
                {
                    "targets":5,
                    "render":function(data, type, full, meta){
                    	var context = '<input type="button" onclick="showSteps(\''+data.categoryName+'\',\''+data.categoryId+'\');" class="btn btn-success-outline radius size-S" value="'+data.stepNum+'"/>';
                        return context;
                    }

                },
                {
                    "targets":7,
                    "render":function(data, type, full, meta){
                    	htmlContent = '<a href="javascript:;" onclick="handleRecord(\''+data.categoryId+'\');"><span class="label label-primary radius">待审核</span></a>';
                        return htmlContent;
                    }

                },
                {"orderable":false,"aTargets":[1,3,4]}// 不参与排序的列
            ]
        });
    });
 //展示详情
 function showDesc(desc){
	 if(desc!=null&&desc!=""){
		 layer.prompt({
			  formType: 2,
			  value: desc,
			  title: '详情备注'
			}, function(value, index, elem){
			  layer.close(index);
			}); 
	 }	
 }
 
 //处理审批
 function handleRecord(categoryId){
	 layer.confirm('请处理此条审核请求', {
		  btn: ['审核通过', '打回']
		}, function(index, layero){
			updateStatus(categoryId,"0");
		}, function(index){
			updateStatus(categoryId,"2");
		});	 
 }
 
 function updateStatus(categoryId,status){
	 $.post("publicStep-updateCategoryStatus",{categoryId:categoryId,status:status},function(data){
			if(data.returnCode==0){						
				table.ajax.reload(null,false);
				parent.$('.table-sort').DataTable().ajax.reload(null,false);
				layer.msg("处理成功",{icon:1,time:2000});
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
 }
 
 //打开详情步骤页
 function showSteps(categoryName,categoryId){
	 var index = layer.open({
	        type: 2,
	        title: categoryName+"-测试步骤",
	        content: "webStepList.html?categoryId="+categoryId
	    });
	    layer.full(index);
 }