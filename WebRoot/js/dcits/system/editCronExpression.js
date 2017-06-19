var taskId;
var expression;
var jsonData = [{"name":"hour","options":"*,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23","text":"每小时,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23"},
                {"name":"minute","options":"*,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59","text":"每分钟,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59"},
                {"name":"second","options":"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59","text":"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59"},
                {"name":"date","options":"*,?,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31","text":"每天,不设置,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31"},
                {"name":"week","options":"*,?,1,2,3,4,5,6,7","text":"每天,不设置,星期一,星期二,星期三,星期四,星期五,星期六,星期日"},
                {"name":"month","options":"*,1,2,3,4,5,6,7,8,9,10,11,12","text":"每月,1,2,3,4,5,6,7,8,9,10,11,12"}];
$(function(){
	taskId = GetQueryString("taskId");
	expression = GetQueryString("taskCronExpression");
	
	var html = '';
	var optionsArray = [];
	var textArray = [];
	$.each(jsonData,function(i,n){
		html = '';
		html+='<span class="select-box radius"><select class="select" size="1" id="'+n["name"]+'">';
		optionsArray = n["options"].split(",");
		textArray = n["text"].split(",");
		for(var i=0;i<optionsArray.length;i++){
			html+='<option value="'+optionsArray[i]+'">'+textArray[i]+'</option>';
		}
		html+='</select></span>';	
		$("#"+n["name"]+"Div").html(html);
	});
	
	$("#week").children("option[value='?']").attr("selected","selected");
	
	if(expression!=null&&expression!=""){
		var expressionArray = expression.split(" ");
		$("#second").val(expressionArray[0]);
		$("#minute").val(expressionArray[1]);
		$("#hour").val(expressionArray[2]);
		$("#date").val(expressionArray[3]);
		$("#month").val(expressionArray[4]);
		$("#week").val(expressionArray[5]);
		$("#year").val(expressionArray[6]);	
		$("#taskCronExpression").val(expression);
		
	}
	
	showExplain();
	
	$("input,select").change(function(){
		showExplain();
	});	
	
	$("#onCl").click(sumbitUpdate);
});

var year = '';
var second = '';
var minute = '';
var hour = '';
var date = '';
var month = '';
var week = '';
var explain;
function showExplain(){
	explain='';
	year = $("#year").val();
	second = $("#second").val();
	minute = $("#minute").val();
	hour = $("#hour").val();
	date = $("#date").val();
	month = $("#month").val();
	week = $("#week").val();
	
	$("#taskCronExpression").val($.trim(second+" "+minute+" "+hour+" "+date+" "+month+" "+week+" "+year));
	
	if(year!=""){
		explain+=year+"年的";
	} 
	

	if(month!="*"){
		explain+=month+"月份的";
	}

	
	if(week!="?"){
		
		if(week!="*"){
			var selectOp = document.getElementById("week");
			explain+="每个"+selectOp.options[selectOp.selectedIndex].text+"的";
		}else{
			explain+="每天";
		}
	}
	
	if(date!="?"){
		if(date!="*"){
			explain+=date+"号的";
		}else{
			explain+="每天";
		}
	}
	
	if(hour!="*"){
		explain+=hour+"点";
	}else{
		explain+="每小时的";
	}
	
	if(minute!="*"){
		explain+=minute+"分";
	}else{
		explain+="每分钟";
	}
	
	if(second!="0"){
		explain+=second+"秒";
	}
	explain+="开始执行定时任务!";
	
	$("#expressExplain").text(explain);
}

function sumbitUpdate(){
	var currExpression = $("#taskCronExpression").val();
	var df = $.Deferred();
	df.done(function(){
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	});
	if(currExpression!=""&&currExpression!=null&&currExpression!=expression){
		$.post("task-updateExpression",{taskId:taskId,taskCronExpression:currExpression},function(data){
			if(data.returnCode==0){
				parent.layer.msg("更新成功",{icon:1,time:2000});
				df.resolve();
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});		
	}else{
		parent.layer.msg("你未作更改或者未设置成功!",{icon:2,time:2000});	
		df.resolve();
	}	
	
	
}