var messageSceneId;
	var messageJson;
	var a=[];
	html='<br><table>';
	var jsonObj;
	var modeFlag=1;
	$(document).ready(function(){	
		messageSceneId= GetQueryString("messageSceneId");
		$(".input-text").focus(function(){
			$("#messageTip").text("输入该条数据的特征标识以区别不同的数据,建议填写类似phone_no,id_no,login_no等字段值");
		});
		$(".input-text").blur(function(){
			$("#messageTip").text("");
		});
		$(".textarea").focus(function(){
			$("#messageTip").text("请直接根据报文入参编辑,更改你需要更改的数据,请勿修改入参节点");
		});
		$(".textarea").blur(function(){
			$("#messageTip").text("");
		});				
		getMessageJson();
		
		$("#copyJson").zclip({
			path: "../../libs/ZeroClipboard.swf",
			copy: function(){
			return $(".textarea").val();
			},
			afterCopy:function(){/* 复制成功后的操作 */
				layer.msg('复制成功,CTRL+V粘贴',{icon:1,time:1500});
	        }
		});
	});
	
	//获取数据json
	function getMessageJson(){	
		$.get("messageScene-getMessageJson",{messageSceneId:messageSceneId},function(data){
			if(data.returnCode==0){
				messageJson=data.messageJson;
				$(".textarea").val(messageJson);
				//解析json报文
				jsonObj=eval('(' + messageJson + ')'); 
				parseJsonObj(jsonObj,"");
				html+='</table>';
				$("#nodeText").html(html);	
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});	
	}
	
	//重置
	function resetMessageJson(){
		$(".textarea").val(messageJson);
		$(".input-text").val("");
		$("#nodeText").html(html);
	}
	//保存数据
	function saveDataJson(){
		var SmessageJson=$(".textarea").val();
		if(modeFlag==0){
			for(var i=0;i<a.length;i++){
				var str2="jsonObj";
				var b=a[i].split(".");
				var value=$("#"+b[b.length-1]).val();
				for(var m=0;m<b.length;m++){
					str2+="[\""+b[m]+"\"]";
				}
				str2+="=\""+value+"\"";
				eval(str2);
			}
			SmessageJson=JSON.stringify(jsonObj);
		}
		var SdataDiscr=$(".input-text").val();
		if(SmessageJson!=null&&SmessageJson!=""&&SdataDiscr!=null&&SdataDiscr!=""){
			//验证入参正确性,验证节点和格式
			$.post("messageScene-updateDataJson",{dataJson:SmessageJson,messageSceneId:messageSceneId},function(data){
				if(data.returnCode==0){
					saveData(messageSceneId,SmessageJson,SdataDiscr);
				}else if(data.returnCode==1){
					layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
				}else if(data.returnCode==2){
					layer.msg('json格式错误',{icon:2,time:1500});
				}else if(data.returnCode==3){
					layer.msg('json入参节点出错,请检查',{icon:2,time:1500});
				}else{
					layer.alert(data.msg, {icon: 5});
				}
			});
		}else{
			layer.msg('请填写完整再提交保存',{icon:2,time:1500});
		}
	}
	
	function saveData(a,b,c){
		$.post("messageScene-saveData",{messageSceneId:a,dataJson:b,dataDiscr:c},function(data){
			if(data.returnCode==0){
				parent.$("#btn-refresh").click();
				var index = parent.layer.getFrameIndex(window.name); 
				parent.layer.close(index); 
				parent.layer.msg('增加成功',{icon:1,time:1500});
			}else if(data.returnCode==1){
				layer.msg('服务器内部错误,请稍后重试',{icon:2,time:1500});
			}else if(data.returnCode==2){
				layer.alert('已存在相同数据标识的数据,请更换',{icon:2});
			}else{
				layer.alert(data.msg, {icon: 5});
			}
		});
	}
	
	//切换模式
	function changeInputMode(){
		if(modeFlag==1){
			$("#nodeText").css("display","block");
			$(".textarea").css("display","none");
			modeFlag=0;
		}else{
			$("#nodeText").css("display","none");
			$(".textarea").css("display","block");
			modeFlag=1;
		}
	}
	
	//解析json报文,输入编辑的节点模式	
	function parseJsonObj(s,nodePath){
        for(var key in s){
            if(s[key] instanceof Object){
                nodePath+=key+".";
                parseJsonObj(s[key],nodePath);               
            }else{
                a.push(nodePath+key);
                html=html+'<tr><td>'+key+'</td><td><input type="text" class="input-text radius" id="'+key+'"  style="width:61%;" value="'+s[key]+'"></td></tr>';
            }
        }
        
    }