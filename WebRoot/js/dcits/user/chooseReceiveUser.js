var tbobyP=$("#tbodyP");
	$(document).ready(function(){
		$("#queryReceiveUserBtn").click(queryReceiveUser);
	});
	
	
	/*选择用户*/
	function choose(id,name){		
		parent.$("#receiveUserName").text(name);
		parent.$("#receiveUserId").val(id);		
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
	
	function queryReceiveUser(){
		var condition = $("#queryReceiveUser").val();
		$("#noDataTip").text("");
		tbobyP.html('');
		if(condition==null || condition==""){
			layer.msg('请输入查询条件',{icon:2,time:1500});
			return false;
		}
		var html='';
		var btnS='';
		$.get("user-filter",{realName:condition},function(data){
			if(data.returnCode==0){

				$.each(data.data,function(i,n){
					btnS='<a href="javascript:;" onclick="choose(\''+n.userId+'\',\''+n.realName+'\')" class="btn btn-danger size-S radius">选择</a>';
					html+='<tr class="text-c"><td>'+n.userId+'</td><td class="ellipsis" >'+ '<span title="' + n.realName  + '">' + n.realName  + '</span>'
					+'</td><td>'+btnS+'</td></tr>';
				});
				tbobyP.html(html);
			}else if(data.returnCode==2){
				$("#noDataTip").text(data.msg);
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}