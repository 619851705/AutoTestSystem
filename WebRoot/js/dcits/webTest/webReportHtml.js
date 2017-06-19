var SCENARIOS = "h3[id^='scenario_']";

$(document).ready(function() {
  
  var reportCaseId=GetQueryString("reportCaseId");
  var reportSetId=GetQueryString("reportSetId");
  if(reportSetId!=null){
	  $.get("webReport-getReportSet",{reportSetId:reportSetId},function(d){
		  if(d.returnCode==0){
			  $("#duration").html("测试时间:<strong>"+d.reportSet.testTime+"</strong>");
			  $("#totals").html("测试用例数:<strong>"+d.reportSet.testDetails.caseNum+"</strong>&nbsp;&nbsp;&nbsp;成功执行数:<strong>"+d.reportSet.testDetails.executeNum+"</strong>");
			  $("#reportTitle").text(d.reportSet.setName);
			  var html='';
			  for(var i=0;i<d.reportCases.length;i++){
				 $.ajax({
					 type:"GET",
					 url:"webReport-stepReport?reportCaseId="+d.reportCases[i].reportCaseId,
					 async: false,
					 success:function(data){
						 if(data.returnCode==0){
							  html+=createReport(data,(i+1));
						  }else{
							  alert("服务器处理失败,请刷新重试"); 
						  } 
					 }
				 });				  
			  }
			  $("#container").html(html);
			  $(SCENARIOS).css('cursor', 'pointer');
			  $(SCENARIOS).click(function() {
			    $(this).siblings().toggle(250);
			  });
		  }else{
			  alert("服务器处理失败,请刷新重试"); 
		  }
	  });
  }else{
	  getReportCaseStep(reportCaseId);
  }
  

  $("#collapser").css('cursor', 'pointer');
  $("#collapser").click(function() {
    $(SCENARIOS).siblings().hide();
  });

  $("#expander").css('cursor', 'pointer');
  $("#expander").click(function() {
    $(SCENARIOS).siblings().show();
  });
});

function moveProgressBar(percentDone) {
    $("cucumber-header").css('width', percentDone +"%");
  }
  function makeRed(element_id) {
    $('#'+element_id).css('background', '#C40D0D');
    $('#'+element_id).css('color', '#FFFFFF');
  }
  function makeYellow(element_id) {
    $('#'+element_id).css('background', '#FAF834');
    $('#'+element_id).css('color', '#000000');
  }


function getReportCaseStep(reportCaseId){
	 //获取完整的测试报告
	  $.get("webReport-stepReport?reportCaseId="+reportCaseId,function(d){
		  if(d.returnCode==0){
			  $("#duration").html("测试时间:<strong>"+d.reportInfo.testTime+"</strong>");
			  $("#totals").html("测试步骤数量:<strong>"+d.reportInfo.stepNum+"</strong>&nbsp;&nbsp;&nbsp;浏览器:<strong>"+d.reportInfo.browser+"</strong>");
			  var html=createReport(d,1);
			  $("#container").html(html);
			  $(SCENARIOS).css('cursor', 'pointer');
			  $(SCENARIOS).click(function() {
			    $(this).siblings().toggle(250);
			  });
		  }else{
			  alert("服务器处理失败,请刷新重试");
		  }
	  });
	
}


function moveProgressBar(percentDone) {
  $("cucumber-header").css('width', percentDone +"%");
}
function makeRed(element_id) {
  $('#'+element_id).css('background', '#C40D0D');
  $('#'+element_id).css('color', '#FFFFFF');
}
function makeYellow(element_id) {
  $('#'+element_id).css('background', '#FAF834');
  $('#'+element_id).css('color', '#000000');
}

function GetQueryString(name)
{
var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
var r = window.location.search.substr(1).match(reg);
if(r!=null)return unescape(r[2]); return null;
}
  

//创建reportHtml--传入一个reportSet
function createReport(reportSet,num){
	var html='<div class="feature">';
	html+='<h2><span class="val">'+reportSet.reportInfo.caseName+'</span></h2>';
	html+='<p class="narrative"></p>';
	html+='<div class=\'scenario\'>';
	if(reportSet.reportInfo.status=="fail"){
		html+='<h3 style="background:#C40D0D;color:#FFFFFF;" id="scenario_'+num+'"><span class="keyword">场景:</span> <span class="val">'+reportSet.reportInfo.caseName+'</span><span style="color:black;float:right;">'+reportSet.reportInfo.browser+'</span></h3>';
	}else{
		html+='<h3 id="scenario_'+num+'"><span class="keyword">场景:</span> <span class="val">'+reportSet.reportInfo.caseName+'</span><span style="color:black;float:right;">'+reportSet.reportInfo.browser+'</span></h3>';

	}
	html+='<ol>';
	$.each(reportSet.data,function(i,n){
		html+=createStepReport(n);
		html+='<script type="text/javascript">moveProgressBar(\''+(0.8*(i+1))+'\');<\/script>';
	});
	html+='</ol></div></div>';
	return html;
}

//创建测试步骤结果展示--传入一个stepReport
function createStepReport(report){
	var status="";
	var html='';
	if(report.runStatus=="success"){
		status="passed";
	}else{
		status="failed";
	}
	html+='<li id="'+report.reportId+'" class="step '+status+'">';
	html+='<div class="step_name"><span class="step val">'+report.stepName+'</span></div>';
	html+='<div class="step_file"><span>'+report.opTime+'</span>&nbsp;&nbsp;';
	if(report.capturePath!=""&&report.capturePath!=null){
		html+='<a href="javascript:;" onclick="showCapture(this,\'capture'+report.reportId+'\');"><span>截图<span></a></div></li>';
		html+='<li><div id="capture'+report.reportId+'" style="display:none;"><img src="../../screenshots/'+report.capturePath+'"  alt="'+report.stepName+'" width="100%"/></div></li>';
	}else{
		html+='</div></li>';
	}
	
	if(report.testMark!=""&&report.testMark!=null){
		html+='<li class="step message">'+report.testMark+'</li>';
	}
	return html;
}

function showCapture(obj,captureId){
	var flag=$("#"+captureId).is(":hidden");
	if(flag){		
		$("#"+captureId).show();
	}else{
		$("#"+captureId).hide();
	}
}