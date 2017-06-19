$(function(){
	//加载对应的js文件
	var oHead = document.getElementsByTagName('HEAD').item(0);
	var oScript= document.createElement("script");
	oScript.type = "text/javascript";
	var r = (window.location.pathname.split("."))[0].split("/");
	r = "../../js/dcits/"+r[r.length-2]+"/"+r[r.length-1]+".js";
	oScript.src=r;
	oHead.appendChild( oScript);
});

//遮罩层覆盖区域
var $wrapper = $('#div-table-container');

//DT的配置常量
var CONSTANT = {
		DATA_TABLES : {	
			DEFAULT_OPTION:{
			"aaSorting": [[ 1, "desc" ]],//默认第几个排序
            "bStateSave": true,//状态保存
            "processing": false,   //显示处理状态
    		"serverSide": false,  //服务器处理
    		"ajax":function(data, callback, settings) {//ajax配置为function,手动调用异步查询
	    			//手动控制遮罩
	    			$wrapper.spinModal();
        			$.ajax({
        		            type: "GET",
        		            url: thisUrl, //thisUrl为每个页面自己单独定义的请求地址
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
            "responsive": false,   //自动响应
            "lengthMenu": [[10, 15, 100], ['10', '15', '100']],  //显示数量设置
            //行回调
            "createdRow": function ( row, data, index ){
                $(row).addClass('text-c');
            }},
            //常用的COLUMN
			COLUMNFUN:{
				//过长内容省略号替代
				ELLIPSIS:function (data, type, row, meta) {
                	data = data||"";
                	return '<span title="' + data + '">' + data + '</span>';
                }
			}
		}
};
	
//设置jQuery Ajax全局的参数  
$.ajaxSetup({  
    error: function(jqXHR, textStatus, errorThrown){  
    	layer.closeAll('dialog');
        switch (jqXHR.status){  
            case(500):  
                layer.alert("服务器系统内部错误",{icon:5});  
                break;  
            case(401):  
            	layer.alert("未登录或者身份认证过期",{icon:5});  
                break;  
            case(403):  
            	layer.alert("你的权限不够",{icon:5});  
                break;  
            case(408):  
            	layer.alert("AJAX请求超时",{icon:5});
                break;  
            default:  
            	layer.alert("AJAX调用失败",{icon:5});
        } 
        if($wrapper!=null){
        	 $wrapper.spinModal(false);
        }	       
    }
});

/**
 * 返回DT中checkbox的html
 * @param name  name属性,对象的name或者其他
 * @param val   value属性,对象的id
 * @param className  class属性,一般为select+对象
 */
function checkboxHmtl(name,val,className){
	return '<input type="checkbox" name="'+name+'" value="'+val+'" class="'+className+'">';
}


/**
 * 单个删除功能，通用
 * tip 确认提示
 * url 删除请求地址
 * params 发送参数
 * obj 表格删除对应的内容
 */
function delObj(tip,url,params,obj){
	layer.confirm(tip,function(index){
		$wrapper.spinModal();
		$.post(url,params,function(data){
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

/**
 * 批量删除方法-表格为DT时
 * @param checkboxList  checkBox被选中的列表
 * @param url   删除url
 * @param tableObj   TD对象，默认名为table
 * @returns {Boolean}
 */
function batchDelObjs(checkboxList,url,tableObj){
	if(checkboxList.length<1){
		return false;
	}
	layer.confirm('确认删除选中的'+checkboxList.length+'条记录?',function(index){
		layer.close(index);
		$wrapper.spinModal();
		var delCount = 0;
		var delFlag = 0;
			$.each(checkboxList,function(i,n){
			objId=$(n).val();//获取id
			objName=$(n).attr("name");	//name属性为对象的名称		
			$.ajax({
				type:"POST",
				url:url,
				data:{objId:objId},
				async:false,
				success:function(data){
					if(data.returnCode!=0){	
						delFlag = 1;
						layer.alert("在删除"+objName+"时出错了:"+data.msg, {icon: 5});
						return;
					}else{
						delCount = i+1;
					}
				}
				});
			});
			refreshTable(tableObj);
			if(delFlag==0){
				parent.layer.msg('删除了'+delCount+'条记录',{icon:1,time:1500});
			}
	});
		
}



/**
 * 刷新表格
 * 所有表格页面都是的DT对象名称都要命名为table
 * 否则要传入指定的DT对象
 */
function refreshTable(tableObj){
	if(tableObj==null){
		table.ajax.reload(null,false);
	}else{
		tableObj.ajax.reload(null,false);
	}
	
}