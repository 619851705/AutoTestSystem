var demoIframe;
var zTreeObj;
var beforeNodeName;
var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false,
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "categoryId",
			pIdKey: "parentCategoryId",
			rootPId: 0
		}
	},
	edit:{
		enable: true,
		isCopy: false,
		isMove: true,
		showRemoveBtn: true,
		showRenameBtn: true,
		removeTitle:"删除节点",
		renameTitle: "编辑节点名称",
		
	},
	callback: {
		beforeClick: function(treeId, treeNode) {
			var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
			if (treeNode.isParent) {
				zTreeObj.expandNode(treeNode);
				return false;
			} else {
					var demoIframe = $("#testIframe");
					demoIframe.attr("src","webObjectList.html?categoryId="+treeNode.categoryId);
				return true;
			}
		},
		beforeDrag:beforeDrag,
		beforeDrop:beforeDrop,
		beforeEditName:beforeEditName,
		beforeRemove:beforeRemove,
		onRename:onRename,
		onDrop:onDrop,
		onRemove:onRemove
	}
};	

var newCount = 1;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='增加节点' onfocus='this.blur();'></span>";
	if(treeNode.categoryType!="page"){		
		sObj.after(addStr);}
	var btn = $("#addBtn_"+treeNode.tId);
	if (btn) btn.bind("click", function(){
		if(treeNode.categoryType=="page"){
			layer.msg("此节点类型为page-页面,无法再增加下属节点",{icon:5,time:2000});
			return;
		}
		var types=
		{
				"website":"网站",
				"module":"模块",
				"feature":"功能",
				"page":"页面"
				
		};

		
		var html='';
		var htmlName='';
		var htmlVal='';
		$.each(types, function(key, val){
			htmlName+='<input type="button" value="'+key+'" class="btn btn-danger radius objectTypeCss"/>&nbsp;&nbsp;';
			htmlVal+='<span class="typeDesc" style="float:left; padding:5px 20px;display:none;color:red;">'+val+'</span>';
		});
		html='<div><br>&nbsp;&nbsp;&nbsp;'+htmlName+'<br><br><div>'+htmlVal+'</div></div>';
		var index=layer.open({
			  title: "双击选择你要创建的节点类型",
			  type: 1,
			  skin: 'layui-layer-rim', //加上边框		
			  area: ["350px","180px"], //宽高
			  content: html
			});
		$(".objectTypeCss").on("click",function(){
			 $(this).addClass("btn-danger").siblings().removeClass("btn-danger");
			 $(".typeDesc").eq($(".objectTypeCss").index(this)).show().siblings().hide();
		});
		$(".objectTypeCss").on("dblclick",function(){
			var chooseCategoryType=$(this).val();
			layer.close(index);
			//发送请求创建该节点
			$.post("webObject-addCategory",{categoryName:"新的节点"+ (newCount++),targetCategoryId:treeNode.categoryId,categoryType:chooseCategoryType},function(data){
				if(data.returnCode==0){
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					var newNode={categoryId:data.categoryId, parentCategoryId:treeNode.id, name:"新的节点"+ (newCount++),categoryType:chooseCategoryType,objectNum:0};
					if(chooseCategoryType!="page"){
						newNode["isParent"]=true;				
					}
					newNode["icon"]="../../libs/zTree/v3/css/zTreeStyle/img/diy/"+newNode.categoryType+".png";
					zTree.addNodes(treeNode, newNode);
					layer.msg("增加节点成功",{icon:1,time:2000});
				}else{
					layer.alert("你刚才的操作由于以下错误导致未能成功,请稍后重试\n"+data.msg,{icon:5});
				}
			});
			
			
		});
		$(".objectTypeCss").eq(0).click();	
		return false;
	});
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
};	
	
	
//拖放操作完成之后的回调
function onDrop(event, treeId, treeNodes, targetNode, moveType,isCopy){
	
}
//修改操作之后的回调函数
function onRename(event, treeId, treeNode, isCancel){
	if(!isCancel){
		$.get("webObject-updateCategoryName",{categoryId:treeNode.categoryId,categoryName:treeNode.name},function(data){
			if(data.returnCode==0){
				layer.msg("修改成功",{icon:1,time:2000});
			}else{
				var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
				treeNode.name=beforeNodeName;
				zTreeObj.updateNode(treeNode);
				layer.alert(data.msg,{icon:5});
			}
		});
		//console.log(treeNode);
	}
}


//删除节点之前的回调
function beforeRemove(treeId, treeNode){
	var flag=false;
	if(treeNode.categoryType=="all"){
		return false;
	}
	if(treeNode.objectNum>0){		
		layer.msg("节点下还有正在使用的测试对象,请先删除");
		return false;
	}
	
	var x=confirm('确认删除该节点及其下属所有的根结点吗?');
	if(x){
		$.ajax({
			url:"webObject-delCategory",
			type:"POST",
			data:{categoryId:treeNode.categoryId},
			async: false,
			success:function(data){
				if(data.returnCode==0){				
					layer.msg("删除成功!",{icon:1,time:2000});
					flag=true;
				}else{				
					layer.alert(data.msg,{icon:5});
					flag=false;
				}
			}
		});
	}
	return flag;
	/* 		layer.confirm('确认删除该节点及其下属所有的根结点吗?',function(index){		
		
    },
    function(index){
    	layer.close(index);
    	flag=false;
    }); */
	
}

//删除操作之后的回调
function onRemove(event, treeId, treeNode){
	var parentNode = treeNode.getParentNode();
	parentNode.isParent=true;
}


//拖放之前的回调
function beforeDrag(treeId, treeNodes) {
	for (var i=0,l=treeNodes.length; i<l; i++) {
		if (treeNodes[i].drag === false) {
			return false;
		}
	}
	return true;
}

//拖放完成之前的回调
function beforeDrop(treeId, treeNodes, targetNode, moveType) {	
	if(!(targetNode==null||(moveType != "inner" && targetNode.parentCategoryId==0))){
		if(moveType=="inner"&&targetNode.categoryType=="page"){
			layer.msg("不能移动当前节点到page节点下");
			return false;
		}
		var flag=false;
		$.ajax({
			url:"webObject-moveCategory",
			type:"POST",
			data:{
				categoryId:treeNodes[0].categoryId,
				targetCategoryId:targetNode.categoryId,
				moveType:moveType
				},
			async: false,
			success:function(data){
				if(data.returnCode!=0){
					layer.alert(data.msg,{icon:5});
					flag=false;
				}else{
					flag=true;
				}
			}
			
		});
		return flag;
		}
	return false;
}

//编辑之前的回调
function beforeEditName(treeId, treeNode){
	if(treeNode.categoryType=="all"){
		return false;
	}
	beforeNodeName=treeNode.name;
	return true;
}

function filter(node) {
    return (node.categoryType=="page");
}
$(document).ready(function(){
	$.get("webObject-getNodes",function(data){
		if(data.returnCode==0){
			var nodes=data.categorys;
			$.each(nodes,function(i,n){
				if(n.categoryType!="page"){
					n["isParent"]=true;			
				}
				n["icon"]="../../libs/zTree/v3/css/zTreeStyle/img/diy/"+n.categoryType+".png";
			});
			var t = $("#treeDemo");
			t = $.fn.zTree.init(t, setting, nodes);
			demoIframe = $("#testIframe");
			demoIframe.bind("load", loadReady);
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.selectNode(zTree.getNodeByParam("categoryType", "page")); 
			var node=zTree.getNodesByFilter(filter, true);
			demoIframe.attr("src","webObjectList.html?categoryId="+node.categoryId);
			demoIframe.click();
		}			
	});
});




function loadReady() {
	var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
	htmlH = demoIframe.contents().find("html").get(0).scrollHeight,
	maxH = Math.max(bodyH, htmlH), minH = Math.min(bodyH, htmlH),
	h = demoIframe.height() >= maxH ? minH:maxH ;
	if (h < 530) h = 530;
	demoIframe.height(h);
}