var zTreeObj;
//初始的被checked的opId的数组
var initCheckOpId=new Array();
//操作的被取消或者删除的op
var currDelCheckOpId=new Array();
//操作的增加的op
var currAddCheckOpId=new Array();

var roleId = GetQueryString("roleId");
var roleName = GetQueryString("roleName");

var setting = {
	view: {showIcon: false},
	check: {
		enable: true,
		chkboxType:  { "Y" : "s", "N" : "ps" },
		autoCheckTrigger: true
		},
	data: {
		simpleData: {
			enable:true,
			idKey: "opId",
			pIdKey: "parentOpId",
			rootPId: 1
		},
		key: {
			name:"opName",
			title:"mark",
			url:"callName",
			checked:"isOwn"
		}
	},
	callback:{
		onCheck:zTreeOnCheck
	}
};	


$(document).ready(function(){
/* 	if(roleName=="admin"){
		$("#saveBtn").hide();
	} */
	
	$.get("role-getNodes?roleId="+roleId,function(data){
		if(data.returnCode==0){
			var nodes=data.interfaces;
			$.each(nodes,function(i,n){
				if(n.isParent=="true"){
					n["open"]="true";
				}
				if(n.isOwn==true){
					initCheckOpId.push(n.opId);
				}
			});
			var t = $("#treeDemo");
			t = $.fn.zTree.init(t, setting, nodes);
		}else{
			layer.alert(data.msg,{icon:5});
		}		
	});
});

//Ztree中checkBox被选中或者取消时的回调
function zTreeOnCheck(event, treeId, treeNode) {
	//判断是否是根节点
	if(treeNode.isParent=="false"||treeNode.isParent==false){
		//判断是被勾选还是取消勾选
		if(treeNode.isOwn){
			//被勾选,判断是否为初始的数据		
			if(initCheckOpId.indexOf(treeNode.opId)==-1){
				currAddCheckOpId.push(treeNode.opId);
			}else{
				currDelCheckOpId.splice(currDelCheckOpId.indexOf(treeNode.opId),1);
			}	
	/* 		alert("增加数："+currAddCheckOpId.length+",删除数："+currDelCheckOpId.length); */
		}else{
			//取消勾选
			if(initCheckOpId.indexOf(treeNode.opId)==-1){
				currAddCheckOpId.splice(currAddCheckOpId.indexOf(treeNode.opId),1);
			}else{
				currDelCheckOpId.push(treeNode.opId);
			}
	/* 		alert("增加数："+currAddCheckOpId.length+",删除数："+currDelCheckOpId.length); */
		}
	}	
}


//保存信息发送到服务端
function saveChange(){	
	//判断是否需要发送更新请求到后台
	if(currDelCheckOpId.length<1&&currAddCheckOpId.length<1){
		parent.layer.close(parent.layer.getFrameIndex(window.name));
		return;
	}
	var sendData = {"roleId":roleId};
	if(currDelCheckOpId.length>0){
		sendData["delOpIds"]=currDelCheckOpId.join(",");
	}
	if(currAddCheckOpId.length>0){
		sendData["addOpIds"]=currAddCheckOpId.join(",");
	}
	console.log(sendData.delOpIds);
	console.log(sendData.addOpIds);
	$.get("role-updateRolePower",sendData,function(data){
		if(data.returnCode==0){
			parent.$('#btn-refresh').click();
			parent.layer.close(parent.layer.getFrameIndex(window.name));
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
}
