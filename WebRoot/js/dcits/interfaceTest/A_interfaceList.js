var table;
var interfaceId;   
var thisUrl = "interface-list";
    	$(document).ready(function(){
    	var tpl = $("#tpl").html();
        //预编译模板
        var template = Handlebars.compile(tpl);          
        table = $('.table-sort').DataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION,{
            "columns":[
                {
                	"data":null,
                	"render":function(data, type, full, meta){                       
                        return checkboxHmtl(data.interfaceName+'-'+data.interfaceCnName,data.interfaceId,"selectInterface");
                    }},
                {
                    "data":"interfaceId"},
                {
                	"className":"ellipsis",
				    "data":"interfaceName",
                	"render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
                	},
                {
            		"className":"ellipsis",
				    "data":"interfaceCnName",
				    "render":CONSTANT.DATA_TABLES.COLUMNFUN.ELLIPSIS
                		},
                {
                	"data":null,
                	"render":function(data, type, full, meta ){
                    	var btype;
                    	if(data.interfaceType=="SL"){
                    		btype = "受理类";
                    	}else{
                    		btype = "查询类";
                    	}
                        return btype;
                    }},
                {"data":"createTime","width":"120px"},
                {
                	"data":null,
                	"render":function(data, type, full, meta ){
                    	var bstatus;
                    	var btnstyle;
                    	if(data.status=="0"){
                    		bstatus = "可用";
                    		btnstyle = "success";
                    	}else{
                    		bstatus = "禁用";
                    		btnstyle = "disabled";
                    	}
                        var htmlContent = '<span class="label label-'+btnstyle+' radius">'+bstatus+'</span>';
                        return htmlContent;
                    }},
                {
                    "data":"user.realName"
                    	},
                {
                    "data":"lastModifyUser"
                    	},
                {
                    "data":null,
                    "render":function(data, type, full, meta){
                    	var context =
                        {
                            func: [
                                {"name": "管理", "fn": "showParameters(\'"+data.interfaceName+"入参管理\',\'A_interfaceParams.html\',\'"+data.interfaceId+"\')", "type": "primary"}
                            ]
                        };
                        var html = template(context);
                        return html;
                    }},
                {
                    "data":null,
                    "render":function(data, type, full, meta){
                    	var context = '<a style="text-decoration:none" class="ml-5" onClick="interface_edit(\'接口编辑\',\'A_interfaceEdit.html\',\''+data.interfaceId+'\')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="interface_del(this,\''+data.interfaceId+'\')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>';
                        return context;
                    }}
            ],
            "columnDefs": [				                          
                {"orderable":false,"aTargets":[0,7,8,10]}// 不参与排序的列
            ]
        }));
    });
	
    var interfaceName;
	/*批量删除接口*/
	function interface_batchDel(){
		var checkboxList = $(".selectInterface:checked");
		batchDelObjs(checkboxList,"interface-del",table);
	}
	
	/*管理接口的参数*/
	function showParameters(title,url,id,w,h){
		$("#selectInterfaceId").val(id);
		layer_show(title,url,'850','480');
	}

    /*接口-添加*/
    function interface_add(title,url,w,h){
    	layer_show(title,url,'850','480');
    }
    /*接口-编辑*/
    function interface_edit(title,url,id,w,h){
    	$("#selectInterfaceId").val(id);
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*接口-删除*/
    function interface_del(obj,id){
    	delObj("确认要删除吗？","interface-del",{interfaceId:id},obj);   	
    }