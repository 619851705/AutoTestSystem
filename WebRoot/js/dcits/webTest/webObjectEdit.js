$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	var modeFlag=GetQueryString("modeFlag");
	var categoryId=GetQueryString("categoryId");
	$("#categoryId").val(categoryId);
	//modeFlag为0则 为增加对象请求
	//modeFlag为1则为修改对象请求
	if(modeFlag==1){
		$(".row").eq(0).css("display","block");
		var objectId=GetQueryString("objectId");
		$.post("webObject-get",{objectId:objectId},function(data){
			if(data.returnCode==0){
				var o=data.webObject;
				$("#objectId").val(o.objectId);
				$("#objectIdText").text(o.objectId);
				$("#objectName").val(o.objectName);
				$("#objectType").val(o.objectType);
				$("#objectSeq").val(o.objectSeq);
				$("#objectSeqText").text(o.objectSeq);
				$("#objectTypeText").text(o.objectType);
				$("#how").val(o.how);
				$("#howText").text(o.how);
				$("#propertyValue").val(o.propertyValue);
				$("#pageUrl").val(o.pageUrl);	
			}else{
				layer.alert(data.msg,{icon:5});
			}
		});
	}
	$("#form-webObject-edit").validate({
		rules:{
			objectName:{
				required:true,
				minlength:2,
				maxlength:200
			},
			objectType:{
				required:true
			},
			how:{
				required:true
			},
			propertyValue:{
				required:true
			}
		},
		messages:{
			objectType:"必须选择正确的对象类型",
			how:"必须选择可用的对象获取方式"
		},
		ignore: "",
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			var formData = $(form).serialize();
			$.post("webObject-edit",formData,function(data){
				if(data.returnCode==0){										
					var index = parent.layer.getFrameIndex(window.name);
					parent.$('#btn-refresh').click();
					parent.layer.close(index);
				}else{
					layer.alert(data.msg, {icon: 5});
				}			
			});			
		}
	});
});

//相关可供选择的objectType和how的选项先用固定的,以后会从数据库中获取
var objectTypes={
		"button":"html页面上的按钮",
		"text_field":"html页面上普通的文本输入框",
		"link":"html页面上可供点击的超链接",
		"url":"普通的url字符串",
		"textarea":"html页面的文本区域、多行文本输入框",
		"select":"html页面上的下拉框",
		"radio":"html页面上的单选框",
		"checkbox":"多选框",
		"text":"普通文本,例如&lt;p&gt;标签,&lt;span&gt;标签等",
		"-":"其他类型"
};

var hows={
		"-":"该对象不需要获取方式,可能只是个常量,例如url等",
		"Id":"通过id属性来查找html页面上的一个唯一元素(正常情况下,一个html页面上是不会出现两个相同id的元素的)",
		"LinkText":"通过链接文字来精确查找对应的link元素",
		"Name":"通过name属性来查找html页面上的元素,如果有多个相同name的元素在此页面上,你可以通过设置[查找顺序]来定位指定位置的元素",
		"TagName":"通过html标签来定位html上的指定元素,如果有多个相同tagName的元素在此页面上,你可以通过设置[查找顺序]来定位指定位置的元素",
		"XPath":"正常情况下,通过id来定位html页面元素是最简单快速的,如果不能通过id来定位,推荐使用XPath方法,具体的使用方法你可以参考XPath说明文档",
		"ClassName":"通过className来定位html页面元素,如果有多个相同className的元素在此页面上,你可以通过设置[查找顺序]来定位指定位置的元素"
};

//选择ObjectType
function chooseObjectType(){
	choosePopup(objectTypes,"objectType","选择对象类型","760px","180px");
}


//选择how
function chooseHow(array,typeName,titleName,w,h){
	choosePopup(hows,"how","选择对象获取方式","600px","180px");
}



function choosePopup(array,typeName,titleName,w,h){
	var html='';
	var htmlName='';
	var htmlVal='';
	$.each(array, function(key, val){
		htmlName+='<input type="button" value="'+key+'" class="btn btn-danger radius objectTypeCss"/>&nbsp;&nbsp;';
		htmlVal+='<span class="typeDesc" style="float:left; padding:5px 20px;display:none;color:red;">'+val+'</span>';
	});
	html='<div><br>&nbsp;&nbsp;&nbsp;'+htmlName+'<br><br><div>'+htmlVal+'</div></div>';
	var index=layer.open({
		  title: titleName,
		  type: 1,
		  skin: 'layui-layer-rim', //加上边框		
		  area: [w, h], //宽高
		  content: html
		});
	$(".objectTypeCss").on("click",function(){
		 $(this).addClass("btn-danger").siblings().removeClass("btn-danger");
		 $(".typeDesc").eq($(".objectTypeCss").index(this)).show().siblings().hide();
	});
	$(".objectTypeCss").on("dblclick",function(){
		var t=$(this).val();
		$("#"+typeName+"Text").text(t);
		$("#"+typeName).val(t);
		layer.close(index);
	});
	$(".objectTypeCss").eq(0).click();
}

function reduceOjectSeq(){
	var seq=$("#objectSeqText").text();
	if(seq==0){
		return;
	}
	$("#objectSeqText").text(seq-1);
	$("#objectSeq").val(seq-1);
}

function addObjectSeq(){
	var seq=$("#objectSeqText").text();
	$("#objectSeqText").text(parseInt(seq)+1);
	$("#objectSeq").val(parseInt(seq)+1);
}