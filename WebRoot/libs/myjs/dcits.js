/*Javascript����ͣ���ܵ�ʵ��
Javascript����û����ͣ���ܣ�sleep����ʹ�ã�ͬʱ vbscriptҲ����ʹ��doEvents���ʱ�д�˺���ʵ�ִ˹��ܡ�
javascript��Ϊ���������ԣ�һ������Ҳ������Ϊһ������ʹ�á�
���磺
 
[code]
function Test(){
 alert("hellow");
 this.NextStep=function(){
 alert("NextStep");
 }
}
���ǿ����������� var myTest=new Test();myTest.NextStep();
��������ͣ��ʱ����԰�һ��������Ϊ�����֣���ͣ����ǰ�Ĳ��䣬��Ҫ����ͣ��ִ�еĴ������this.NextStep�С�
Ϊ�˿�����ͣ�ͼ�����������Ҫ��д�����������ֱ�ʵ����ͣ�ͼ������ܡ�
��ͣ�������£�
*/
function Pause(obj,iMinSecond){
 if (window.eventList==null) window.eventList=new Array();
 var ind=-1;
 for (var i=0;i<window.eventList.length;i++){
 if (window.eventList[i]==null) {
  window.eventList[i]=obj;
  ind=i;
  break;
 }
 }
  
 if (ind==-1){
 ind=window.eventList.length;
 window.eventList[ind]=obj;
 }
 setTimeout("GoOn(" + ind + ")",1000);
}
/*
�ú�����Ҫ��ͣ�ĺ����ŵ�����window.eventList�ͬʱͨ��setTimeout�����ü���������
�����������£�
*/
 
function GoOn(ind){
 var obj=window.eventList[ind];
 window.eventList[ind]=null;
 if (obj.NextStep) obj.NextStep();
 else obj();
}


function showLoading(){
	$(".page-container").css({ display: "block", height: $(document).height() });
	var yscroll = document.documentElement.scrollTop;
	var screenx=$(window).width();
	var screeny=$(window).height();
    $(".DialogDiv").css("display", "block");
	$(".DialogDiv").css("top",yscroll+"px");
	 var DialogDiv_width=$(".DialogDiv").width();
	 var DialogDiv_height=$(".DialogDiv").height();
	  $(".DialogDiv").css("left",(screenx/2-DialogDiv_width/2)+"px")
	 $(".DialogDiv").css("top",(screeny/2-DialogDiv_height/2)+"px")
	 $("body").css("overflow","hidden");

}