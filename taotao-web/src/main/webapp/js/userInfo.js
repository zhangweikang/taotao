////获取购物行为
//$.ajax({
//    type: "GET",
//    url : "/user/userinfo/getUserCredit.action",
//    timeout: 3000,
//    success: function (result) {
//        $("#userCredit").addClass("rank-sh rank-sh0"+result);
//    }
//});

$("#nickName").focus(function() {
	$("#nickName_msg").parent().removeClass("prompt-error");
	$("#nickName_msg").parent().addClass("prompt-06");
	$("#nickName_msg").html("4-20个字符，可由中英文、数字、“_”、“-”组成");
	$("#nickName").attr("class","itxt");
});

//校验petName是否正确
$("#nickName").blur(function() {
	var nickName = $("#nickName").val();
	nickName=nickName.trim();
	$("#nickName").val(nickName);
	if (!validNickname(nickName)) {
		return;
	}
	else{
		$("#nickName").addClass("itxt itxt-succ");
	}
//    checkNickName(nickName);
});

$("#realName").focus(function() {
	$("#realName_msg").parent().removeClass("prompt-error");
	$("#realName_msg").parent().addClass("prompt-06");
	$("#realName_msg").html("请输入真实姓名，20个英文或10个汉字");
	$("#realName").attr("class","itxt");
});

$("#realName").blur(function() {
	var realName = $("#realName").val();
	realName=realName.trim();
	$("#realName").val(realName);
//	if (!validRealname(realName)) {
//		return;
//	}
});


$("#address").blur(function() {
//	delspace("address"); //首先去掉空格
	var addr = $("#address").val();
	// 判断是否为空
	if (addr.replace(/[^\x00-\xff]/g, "**").length > 120) {
		$("#address_msg").parent().addClass("prompt-error");
		$("#address_msg").parent().removeClass("prompt-06");
		$("#address_msg").html("长度超长");
	} else {
		$("#address_msg").html("");
	}
});


/**
 * 地址规则
 * @param address
 */
function validAddress(address){
//	delspace("address");
    if (address == null || address == "") {
    }else if(address.replace(/[^\x00-\xff]/g, "**").length > 120) {
		$("#address_msg").parent().addClass("prompt-error");
		$("#address_msg").parent().removeClass("prompt-06");
		$("#address_msg").html("长度超长");
		return false;
    }
    return true;
}


// 提交用户基本信息修改
function updateUserInfo() {
    var nickName=$("#nickName").val();
    var realName=$("#realName").val();
    var sex=$("input[name=sex]:checked").val();
    var birthday=$("#birthdayYear").val() + "-" + $("#birthdayMonth").val() + "-" + $("#birthdayDay").val();
    var province=$("#province").val();
    var city=$("#city").val();
    var county=$("#county").val();
    var address=$("#address").val();
//    var code=$("#code").val();
//    var rkey=$("#rkey").val();
    
    if (!validNickname(nickName)) {
        scroller("nickName", 500);
        return;
    }
    if(sex==null||sex==""){
        alert("请选择性别");
        return;
    }
//    if(!validRealname(realName)){
//        scroller("realName", 500);
//        return;
//    }
//    var hobby="";
//    $("#hobulid").children().each(function(){
//        if($(this).attr("class")=="selected"){
//        	hobby+=$(this).val()+",";
//        }
//    });
    
    if(!validAddress(address)){
        scroller("address", 500);
        return;
    }
    if(city==null||city==""||county==null||county==""){
    	$("#city_msg").parent().addClass("prompt-error");
        $("#city_msg").parent().removeClass("prompt-06");
        $("#city_msg").html("请选择所在地");
    	scroller("province", 500);
        return;
    }
    
    var datas = "";
    datas += "nickName=" + encodeURI(encodeURI(nickName));
    datas += "&realName=" + encodeURI(encodeURI(realName));
    datas += "&sex=" + sex;
    datas += "&birthday=" + birthday;
    datas += "&province=" + province;
    datas += "&city=" + city;
    datas += "&county=" + county;
    datas += "&address="+ encodeURI(encodeURI(address));
//    datas += "&hobby=" + hobby;
//    datas += "&code=" + code;
//    datas += "&rkey=" + rkey;
    
    $.post("http://www.taotao.com/service/user/userinfo/update",datas,function(){
    	
    	alert("修改成功!~")
    });
    
    
//    jQuery.ajax({
//        type : "post",
//        url : "/user/userinfo/updateUserInfo.action",
//        data : datas,
//        timeout: 10000,
////        success : function(html) {
////            if (html=="2") {
////                newBox();
////                setTimeout("jdThickBoxclose()", 3000);
////            } else if(html == "nicknameUsed"){
////            	$("#nickName_msg").parent().addClass("prompt-error");
////				$("#nickName_msg").parent().removeClass("prompt-06");
////				$("#nickName_msg").html("此昵称已被其他用户抢注，请修改");
////				$("#nickName").attr("class","itxt itxt-error");
////				scroller("nickName", 500);
////            } else {
////                alert("保存失败，请稍后再试...");
////            }
////        }
//    });
}


function newBox() {
    jQuery.jdThickBox({
        type: "text",
        title: "提示",
        width: 300,
        height:80,
        source: "<div class=\"tip-box icon-box\"><span class=\"succ-icon m-icon\"></span><div class=\"item-fore\"><h3 class=\"ftx02\">资料保存成功</h3><div class=\"op-btns\"><a href=\"javascript:void(0)\" class=\"btn-9\" onclick=\"jdThickBoxclose()\">关闭</a></div></div></div>",
        _autoReposi: true
    });
}


/**
 * 昵称规则
 * @param nickname
 */
function validNickname(nickName){
    if (nickName == "") {
        $("#nickName_msg").parent().addClass("prompt-error");
        $("#nickName_msg").parent().removeClass("prompt-06");
        $("#nickName").attr("class","itxt itxt-error");
        $("#nickName_msg").html("请输入昵称");
        return false;
    }
    var reg = new RegExp("^([a-zA-Z0-9_-]|[\\u4E00-\\u9FFF])+$", "g");
    var reg_number = /^[0-9]+$/; // 判断是否为数字的正则表达式
    if (reg_number.test(nickName)) {
        $("#nickName_msg").parent().addClass("prompt-error");
        $("#nickName_msg").parent().removeClass("prompt-06");
        $("#nickName_msg").html("昵称不能设置为手机号等纯数字格式，请您更换哦^^");
        $("#nickName").attr("class","itxt itxt-error");
        return false;
    } else if (nickName.replace(/[^\x00-\xff]/g, "**").length < 4 || nickName.replace(/[^\x00-\xff]/g, "**").length > 20) {
        $("#nickName_msg").parent().addClass("prompt-error");
        $("#nickName_msg").parent().removeClass("prompt-06");
        $("#nickName_msg").html("4-20个字符，可由中英文、数字、“_”、“-”组成");
        $("#nickName").attr("class","itxt itxt-error");
        return false;
    } else if (!reg.test(nickName)) {
        $("#nickName_msg").parent().addClass("prompt-error");
        $("#nickName_msg").parent().removeClass("prompt-06");
        $("#nickName_msg").html("昵称格式不正确");
        $("#nickName").attr("class","itxt itxt-error");
        return false;
    }
    return true;
}

/**
 * 真实姓名规则
 * @param realName
 */
//function validRealname(realName){
//    var uname_ = replaceChar(realName, "·"); // 去掉姓名中的·
//    var reg = new RegExp("^([a-zA-Z]|[\\u4E00-\\u9FFF])+$", "g");
//    if (realName == null || realName == "") {
//        $("#realName").addClass("red");
//        $("#realName_msg").parent().addClass("prompt-error");
//        $("#realName_msg").parent().removeClass("prompt-06");
//        $("#realName_msg").html("真实姓名不能为空");
//        $("#realName").attr("class","itxt itxt-error");
//        return false;
//    } else if (realName.indexOf("··") != -1) {
//        // 姓名中不允许有连续多个·
//        $("#realName").addClass("red");
//        $("#realName_msg").parent().addClass("prompt-error");
//        $("#realName_msg").parent().removeClass("prompt-06");
//        $("#realName_msg").html("真实姓名中不允许有连续多个·");
//        $("#realName").attr("class","itxt itxt-error");
//        return false;
//    } else if (realName.substring(0, 1) == "·" || realName.substring(realName.length - 1) == "·") {
//        // 姓名前后不能加·
//        $("#realName_msg").parent().addClass("prompt-error");
//        $("#realName_msg").parent().removeClass("prompt-06");
//        $("#realName_msg").html("真实姓名前后不能加·");
//        $("#realName").attr("class","itxt itxt-error");
//        return false;
//    } else if (!reg.test(uname_)) {
//        $("#realName_msg").parent().addClass("prompt-error");
//        $("#realName_msg").parent().removeClass("prompt-06");
//        $("#realName_msg").html("真实姓名中包含不符合规范的字符");
//        $("#realName").attr("class","itxt itxt-error");
//        return false;
//    } else if (realName.replace(/[^\x00-\xff]/g, "**").length < 4 || realName.replace(/[^\x00-\xff]/g, "**").length > 20) {
//    	 $("#realName_msg").parent().addClass("prompt-error");
//         $("#realName_msg").parent().removeClass("prompt-06");
//         $("#realName_msg").html("请输入真实姓名，英文长度:4-20   中文长度:2-10");
//         $("#realName").attr("class","itxt itxt-error");
//        return false;
//    }
//    $("#realName").attr("class","itxt itxt-succ");
//    $("#realName_msg").html("");
//    return true;
//}
//
//function checkNickName(nickName) {
//	
//	
////	jQuery.ajax({
////		type: "GET",
////		url : "/user/petName/checkPetName.action?callback=?",
////		data : "petNewName=" + encodeURI(encodeURI(nickName)),
////	    dataType: "jsonp",
////	    timeout: 6000,
////		success : function(obj) {
////			if ("0" == obj.type) {
////				$("#nickName_msg").parent().removeClass("prompt-error");
////				$("#nickName_msg").parent().addClass("prompt-06");
////				$("#nickName_msg").html("");
////				$("#nickName").attr("class","itxt itxt-succ");
////			}
////			if ("1" == obj.type) {
////				$("#nickName_msg").parent().addClass("prompt-error");
////				$("#nickName_msg").parent().removeClass("prompt-06");
////				$("#nickName_msg").html("此昵称已被其他用户抢注，请修改");
////				$("#nickName").attr("class","itxt itxt-error");
////			}
////		},
////		error: function(){
////	    	alert("网络异常，请稍后再试！");
////	    }
////	});
