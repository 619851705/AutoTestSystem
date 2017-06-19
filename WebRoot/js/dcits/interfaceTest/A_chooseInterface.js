 var tbobyP=$("#tbodyP");
	$(document).ready(function(){
		$("#queryInterfaceBtn").click(queryInterface);
	});
	
	/*接口-添加*/
    function interface_add(title,url,w,h){
    	var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
	
	/*选择接口*/
	function choose(id,name){
		parent.$("#interfaceIdText").text(name);
		parent.$("#interfaceId").val(id);
		parent.$("#interfaceChooseBtn").val("更改");
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
	
	function queryInterface(){
		var condition = $("#queryInterface").val();
		$("#noDataTip").text("");
		tbobyP.html('');
		if(condition==null || condition==""){
			layer.msg('请输入查询条件',{icon:2,time:1500});
			return false;
		}
		var html='';
		var btnS='';
		$.get("interface-filter",{condition:condition},function(data){
			if(data.returnCode==0){

				$.each(data.data,function(i,n){
					btnS='<a href="javascript:;" onclick="choose(\''+n.interfaceId+'\',\''+n.interfaceName+'\')" class="btn btn-danger size-S radius">选择</a>';
					html+='<tr class="text-c"><td>'+n.interfaceId+'</td><td class="ellipsis" >'+ '<span title="' + n.interfaceName  + '">' + n.interfaceName  + '</span>'
					+'</td><td class="ellipsis" >'+ '<span title="' + n.interfaceCnName  + '">' + n.interfaceCnName  + '</span>'+'</td><td>'+btnS+'</td></tr>';
				});
				tbobyP.html(html);
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else if(data.returnCode==2){
				$("#noDataTip").text("没有查询到指定的接口,你可以手动添加新的接口");
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}