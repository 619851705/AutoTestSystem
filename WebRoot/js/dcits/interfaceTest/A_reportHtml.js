$(document).ready(function(){
		var reportId=GetQueryString("reportId");
		if(reportId!=null){
			$.get("report-htmlView?reportId="+reportId,function(data){
				if(data.returnCode==0){
					var report=data.report;
					var results=data.results;
					$("#sceneNum").text(report.sceneNum);
					$("#successNum").text(report.successNum);
					$("#failNum").text(report.failNum);
					$("#stopNum").text(report.stopNum);
					$("#startTime").text(report.startTime);
					var num = new Number((report.successNum/report.sceneNum)*100);
					var a=changeTwoDecimal(num);
					$("#mxb").text(a+"%");
					
					//插入详情
					var html='';
					var status;
					$.each(results,function(i,n){						
						if(n.runStatus=="0"){
						status='<span style="color:green;">SUCCESS</span>'
						}else if(n.runStatus=="1"){
						status='<span style="color:red;">FAIL</span>'
						}else{
						status='<span style="color:gray;">STOP</span>'
						}
						var infoList=n.messageInfo.split(",");
						html+='<tr><td align=center>'+infoList[0]+'</td>'+
						'<td align=center>'+infoList[1]+'</td>'+
						'<td align=center>'+infoList[2]+'</td>'+
						'<td align=center>'+status+'</td><td align=center>'+n.useTime+
						'ms</td><td align=center><a href="javascript:;" onClick="showResult(this,\''+n.resultId+'\');"><span>查看</span></a></td></tr><tr id="resultDetail'+n.resultId+'" class="divcss5" style="display:none;"><td align=left colspan="6"><strong>请求地址:&nbsp;&nbsp;</strong>'+
						n.requestUrl+'<br><strong>入参报文:&nbsp;&nbsp;</strong>'+
						n.requestMessage+'<br><strong>出参报文:&nbsp;&nbsp;</strong>'+
						n.responseMessage+'<br><strong>备注:&nbsp;&nbsp;</strong>'+
						n.mark+'</td></tr>';
					});
					
					$("#resultView").html(html);
				}else{
					alert("调用Ajax失败...请重新刷新页面");
				}
			});
		}
	});
	
	//四舍五入函数
	function changeTwoDecimal(x)
	{
	var f_x = parseFloat(x);
	if (isNaN(f_x))
	{
	alert('function:changeTwoDecimal->parameter error');
	return false;
	}
	f_x = Math.round(f_x *100)/100;

	return f_x;
	}
	
	
	function showResult(obj,id){
		var a=$(obj).children("span")[0];
		var showStr=$(a).text();
		var thisTr=$("#resultDetail"+id);
		if(showStr=="查看"){
			$(a).text("隐藏");
			thisTr.css('display','table-row');
		}else{
			$(a).text("查看");
			thisTr.css('display','none');
		}
	}