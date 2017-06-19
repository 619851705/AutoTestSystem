var state = 'pending', uploader;
var mode = GetQueryString("mode");
var scriptName = parent.$("#scriptPath").val();
var compliteUploadFlag = parent.$("#compliteUploadFlag").val();
var fileKind = GetQueryString("fileKind");
var currfile;
$(function() {
    state = 'pending';
    uploader = WebUploader.create({pick:'#picker', resize:false, swf:'../../libs/webuploader/Uploader.swf', server:'webScript-uploadScript', disableGlobalDnd:true, auto:false, fileNumLimit:2, fileSizeLimit:20*1024*1024, fileSingleSizeLimit:20*1024*1024,formData:{fileKind:fileKind}});
    uploader.on('fileQueued', function(file) {
    	if(($('#thelist').children()).length>0){
    		var item = ($('#thelist').children())[0];
            uploader.removeFile(item.id, true);
            $(item).remove();
    	}
    	currfile = file;
        $('#thelist').append('<div id="' + file.id + '" class="fl b14b"><div class="fn">' + file.name + '<span class="state" id="st' + file.id + '"></span></div><div class="fd"><input type="button" name="fdb" id="fdb'+ file.id +'" class="bs1" value="删 除" onclick="delf(this)" /></div><div class="clr"></div></div>');
    });
    uploader.on('uploadProgress', function(file, percentage) {
        $('#st' + file.id).text('上传中');
    });
    uploader.on('uploadSuccess', function(file, data) {
        $('#st' + file.id).text('已上传');
        $("#fdb"+ file.id).remove();
        if(data.returnCode == 0) {
            $("#ae").text(data.msg);
            if(mode=="rb"){
            	parent.$("#fileName_rb").text(file.name);
            	parent.$("#uploadBtnRbText").text("重新上传ruby文件");
            }else{
            	parent.$("#fileName_feature").text(file.name);           
            	parent.$("#uploadBtnFeaText").text("重新上传feature文件");
            }
            if((scriptName!=null&&scriptName!="")||parent.$("#ifPublic").val()=="0"){
            	parent.$("#scriptPath").val(data.scriptPath); 
            	parent.$("#compliteUploadFlag").val("0");
            }else{
            	parent.$("#scriptPath").val(data.scriptPath); 
            	parent.$("#compliteUploadFlag").val("");
            }
                              
        } else {
            $("#ae").text(data.msg);
        }
    });
    uploader.on('uploadError', function(file) {
        $('#st' + file.id).text('上传出错');
    });
    uploader.on('all', function(type) {
        if(type === 'startUpload') {
            state = 'uploading';
            $('#upbtn').text('暂停上传');
        } else if(type === 'stopUpload') {
            state = 'paused';
            $('#upbtn').text('开始上传');
        } else if(type === 'uploadFinished') {
            state = 'done';
            $('#upbtn').text('开始上传');
        }
    });
    $('#upbtn').on('click', function() {
    	if(scriptName!=null&&scriptName!=""&&compliteUploadFlag!=null&&compliteUploadFlag!=""){
    		scriptName = "";
    	}
    	if(scriptName!=null&&scriptName!=""){
    		if(currfile.name!=scriptName+"."+mode){
    			$("#ae").text("上传的文件名必须为"+scriptName+"."+mode);
    			return false;
    		}
    	}else{
    		if(currfile.name.substring(currfile.name.indexOf(".")+1)!=mode){
    			$("#ae").text("上传的文件后缀名必须为"+"."+mode);
    			return false;
    		}
    	}
    	 if(state === 'uploading') {
             uploader.stop();
         } else {
             uploader.upload();
         }
    });
});

//移除不需要上传的文件
function delf(node) {
	layer.confirm('确定不需要上传该文件吗?',function(index){
		layer.close(index);
		var item = node.parentNode.parentNode;
        uploader.removeFile(item.id, true);
        $(item).remove();
	});
}

function GetQueryString(name)
{
var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
var r = window.location.search.substr(1).match(reg);
if(r!=null)return decodeURIComponent(r[2]); return null;
}