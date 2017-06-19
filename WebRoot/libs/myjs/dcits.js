/*Javascript中暂停功能的实现
Javascript本身没有暂停功能（sleep不能使用）同时 vbscript也不能使用doEvents，故编写此函数实现此功能。
javascript作为弱对象语言，一个函数也可以作为一个对象使用。
比如：
 
[code]
function Test(){
 alert("hellow");
 this.NextStep=function(){
 alert("NextStep");
 }
}
我们可以这样调用 var myTest=new Test();myTest.NextStep();
我们做暂停的时候可以吧一个函数分为两部分，暂停操作前的不变，把要在暂停后执行的代码放在this.NextStep中。
为了控制暂停和继续，我们需要编写两个函数来分别实现暂停和继续功能。
暂停函数如下：
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
该函数把要暂停的函数放到数组window.eventList里，同时通过setTimeout来调用继续函数。
继续函数如下：
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