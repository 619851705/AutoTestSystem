$(document).ready(function(){
	var caseId=GetQueryString("caseId");
	$.get("webCase-listStep",{caseId:caseId},function(data){
		if(data.returnCode==0){
			var html='';
			$.each(data.data,function(i,n){
				html+='<div class="row cl"><label class="form-label col-xs-offset-1 col-xs-2 col-sm-3">'+
				'<input type="checkbox" value="'+n.stepId+'"></label>'+
				'<div class="formControls col-xs-8 col-sm-9"><strong>'+n.stepDesc+'</strong></div></div>';
			});
			$("#showStepsArea").html(html);
		}else{
			layer.alert(data.msg,{icon:5});
		}
	});
});

var stepCheckedStr='';

function resetChoose(){
	$("#resetBtn").hide();
	$("#setCategoryInfo").hide(250);
	$("#showStepsArea").show(250);	
}

function chooseStepsOk(){
	var checkboxList = $(":checkbox:checked");
	if(checkboxList.length<1){
		layer.msg('至少要选择一个测试步骤');
		return;
	}
	var stepChecked=new Array();
	for(var i=0;i<checkboxList.length;i++){
		stepChecked.push($(checkboxList[i]).val());
	}
	stepCheckedStr=stepChecked.join(",");
	$("#resetBtn").show();
	$("#showStepsArea").hide(250);
	$("#setCategoryInfo").show(250);
}

function editCategoryDesc(){
	layer.prompt({
		  formType: 2,
		  value: $("#categoryDesc").val(),
		  title: '请输入详情备注'
		}, function(value, index, elem){
			$("#categoryDesc").val(value);
		  layer.close(index);
		});
}

function addCategoryTag(){
	layer.prompt({
		  formType: 0,
		  title: '添加标签'
		}, function(value, index, elem){
			if($("#tagSpan").children("span").length>3){
				layer.msg('最多只能增加4个标签!');
				layer.close(index);
				return false;
			}			
			var html='<span class="label label-success radius">'+value+'</span>&nbsp;';
			$("#tagSpan").append(html);
			if($("#categoryTag").val()==""){
				$("#categoryTag").val(value);
			}else{
				$("#categoryTag").val($("#categoryTag").val()+','+value);
			}			
		  	layer.close(index);
		});
}

function submitRecommend(){
	//var index = layer.load(0, {shade: [0.3, '#000']}); 
	var categoryName=$("#categoryName").val();
	if(categoryName==null||categoryName==""){
		layer.msg('请填写名称');
		return;
	}
	var categoryDesc=$("#categoryDesc").val();
	var categoryTag=$("#categoryTag").val();
	$.post("publicStep-addCategory",{
		categoryName:categoryName,
		categoryDesc:categoryDesc,
		categoryTag:categoryTag,
		stepsStr:stepCheckedStr
		},function(data){
		if(data.returnCode==0){
			//layer.close(index);
			parent.layer.close(parent.layer.getFrameIndex(window.name));
			parent.layer.msg('推荐成功,请等待管理员审核',{icon:1,time:2000});
		}else{
			//layer.close(index);
			layer.alert(data.msg,{icon:5});
		}
	});
}