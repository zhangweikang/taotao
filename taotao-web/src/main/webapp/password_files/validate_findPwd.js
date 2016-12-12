var sourceId = "usersafe";//ͼƬ��֤���������
function historyFocus(){
	$("#mobile").removeClass().addClass("itxt text-1 text-mar highlight1");
	$("#mobile_error").removeClass().addClass("msg-text").html("������������ɶ������ջ����ֻ��");
}

function historyBlur(){
	$("#mobileTwo").val($("#mobile").val());
	$("#mobile").removeClass().addClass("itxt text-1 text-mar");
	$("#mobile_error").removeClass().html("");
	var mobile = $("#mobile").val();
	if(!mobile){
		$("#mobile").removeClass().addClass("itxt text-1 text-mar");
		$("#mobile_error").removeClass().html("");
		return;
	}
	checkHistoryMobile();
}

function checkHistoryMobile(){
	var mobile = $("#mobile").val();
	if (!mobile) {
		$("#mobile").removeClass().addClass("itxt text-1 text-mar highlight2");
		$("#mobile_error").removeClass().addClass("msg-error").html("�������ֻ��");
		return;
	}
	var re = /^1{1}[3,4,5,7,8]{1}\d{9}$/; // �ж��Ƿ�Ϊ���ֵ�������ʽ
	if (!re.test(mobile)) {
		$("#mobile").removeClass().addClass("itxt text-1 text-mar highlight2");
		$("#mobile_error").removeClass().addClass("msg-error").html("�ֻ�Ÿ�ʽ��������������");
		return false;
	}
	$("#mobile").removeClass().addClass("itxt text-1 text-mar");
	$("#mobile_error").removeClass().html("");
	return true;
}

function codeFocus(){
	$("#code").removeClass().addClass("itxt text-1 highlight1");
	$("#code_error").removeClass().addClass("msg-text").html("");
}

function codeBlur(){
	$("#code").removeClass().addClass("itxt text-1");
	$("#code_error").removeClass().html("");
}

function checkCode(){
	var code = $("#code").val();
	if(!code){
		$("#code").removeClass().addClass("itxt text-1 highlight2");
		$("#code_error").removeClass().addClass("msg-error").html("��������֤��");
		return false;
	}
	$("#code").removeClass().addClass("itxt text-1");
	$("#code_error").removeClass().html("");
	return true;
}


function authCodeFocus(){
	$("#authCode").removeClass().addClass("itxt text-1 highlight1");
	$("#authCode_error").removeClass().addClass("msg-text").html("");
}

function authCodeBlur(){
	$("#authCode").removeClass().addClass("itxt text-1");
	$("#authCode_error").removeClass().html("");
}

function historyNameFocus(){
	$("#historyName_error").html("");
	$("#historyName").removeClass().addClass("itxt highlight1");
}

function checkHistoryName(){
	var historyName = $("#historyName").val();
	if (!historyName) {
		$("#historyName").removeClass().addClass("itxt highlight2");
		$("#historyName_error").html("������������ɶ������ջ�������");
		return false;
	}
	$("#historyName").removeClass().addClass("itxt");
	$("#historyName_error").html("");
	return true;
}

function mobileFocus(){
	$("#mobile").removeClass().addClass("itxt highlight1");
	$("#mobile_error").removeClass().addClass("msg-text").html("������������ɶ������ջ����ֻ��");
}
function mobileBlur(){
	$("#mobile").removeClass().addClass("itxt");
	$("#mobile_error").removeClass().html("");
	var mobile = $("#mobile").val();
	if(!mobile){
		$("#mobile").removeClass().addClass("itxt");
		$("#mobile_error").removeClass().html("");
		return;
	}
	checkMobile();
}

function checkMobile(){
	var mobile = $("#mobile").val();
	if (!mobile) {
		$("#mobile").removeClass().addClass("itxt highlight2");
		$("#mobile_error").removeClass().addClass("msg-error").html("�������ֻ��");
		return;
	}
	var re = /^1{1}[3,4,5,7,8]{1}\d{9}$/; // �ж��Ƿ�Ϊ���ֵ�������ʽ
	if (!re.test(mobile)) {
		$("#mobile").removeClass().addClass("itxt highlight2");
		$("#mobile_error").removeClass().addClass("msg-error").html("�ֻ�Ÿ�ʽ��������������");
		return false;
	}
	$("#mobile").removeClass().addClass("itxt");
	$("#mobile_error").removeClass().html("");
	return true;
}

function passwordFocus(passwordId){
	$("#pwdstrength").hide();
	$("#password").removeClass().addClass("itxt highlight1");
	$("#password_error").removeClass().addClass("msg-text").html("����ĸ�����ֻ����������������ַ���ɵ�6-20λ����ַ���ִ�Сд��");
}

function passwordBlur(){
	$("#password").removeClass().addClass("itxt");
	var password = $("#password").val();
	if(!password){
		$("#password").removeClass().addClass("itxt");
		$("#password_error").removeClass().html("");
		$("#pwdstrength").hide();
		return;
	}
	checkNewPasswordForm();
	$("#repassword").focus();
}

function passwordKeyup(){
	var password = $("#password").val();
	var reg = new RegExp("^.*([\u4E00-\u9FA5])+.*$", "g");
	if (password.length < 6) {
		$("#password").removeClass().addClass("itxt text-error highlight1");
		$("#password_error").removeClass().html("");
		return false;
	}else {
		var   pattern_1   =   /^.*([\W_])+.*$/i;
		var   pattern_2   =   /^.*([a-zA-Z])+.*$/i;
		var   pattern_3   =   /^.*([0-9])+.*$/i;
		var strength = 0;
		if(password.length>10){
			strength++;
		}
		if(pattern_1.test(password)){
			strength++;
		}
		if(pattern_2.test(password)){
			strength++;
		}
		if(pattern_3.test(password)){
			strength++;
		}
		if(strength<=1){
			$("#pwdstrength").show();
			//$("#pwdstrength").removeClass().addClass("strengthA");
			$("#pwdstrength i").removeClass().addClass('safe-rank03');
			$("#password").removeClass().addClass("itxt");
			$("#password_error").removeClass().html("");
		}
		if(strength==2){
			$("#pwdstrength").show();
			//$("#pwdstrength").removeClass().addClass("strengthB");
			$("#pwdstrength i").removeClass().addClass('safe-rank04');
			$("#password").removeClass().addClass("itxt");
			$("#password_error").removeClass().html("");
		}
		if(strength>=3){
			$("#pwdstrength").show();
			$("#pwdstrength i").removeClass().addClass('safe-rank06');
			//$("#pwdstrength").removeClass().addClass("strengthC");
			$("#password").removeClass().addClass("itxt");
			$("#password_error").removeClass().html("");
		}
	}
}

function repasswordFocus(passwordId){
	$("#repassword").removeClass().addClass("itxt highlight1");
	$("#repassword_error").removeClass().addClass("msg-text").html("���ٴ�����������");
}

function repasswordBlur(){
	$("#repassword").removeClass().addClass("itxt");
	var repassword = $("#repassword").val();
	if(!repassword){
		$("#repassword").removeClass().addClass("itxt");
		$("#repassword_error").removeClass().html("");
		return;
	}
	isSamePassword();
}

function isSamePassword(){
	var password = $("#password").val();
	var repassword = $("#repassword").val();
	if(!repassword){
		$("#repassword").removeClass().addClass("itxt highlight2");
		$("#repassword_error").removeClass().addClass("msg-error").html("���ٴ�����������");
		return false;
	}
	if(password != repassword){
		$("#repassword").removeClass().addClass("itxt highlight2");
		$("#repassword_error").removeClass().addClass("msg-error").html("������������벻һ�£�����������");
		return false;
	}
	$("#repassword_error").removeClass().html("");
	return true;
}


function checkNewPasswordForm(){
	var password = $("#password").val();
	if(!password){
		$("#password").removeClass().addClass("itxt text-error highlight2");
		$("#password_error").removeClass().addClass("msg-error").html("����������");
		return false;
	}
	var reg = new RegExp("^.*([\u4E00-\u9FA5])+.*$", "g");
	if (reg.test(password)) {
		$("#password").removeClass().addClass("itxt text-error highlight2");
		$("#password_error").removeClass().addClass("msg-error").html("�����ʽ����ȷ������������");
		return false;
	} else if (password.length < 6) {
		$("#password").removeClass().addClass("itxt text-error highlight2");
		$("#password_error").removeClass().addClass("msg-error").html("���볤�Ȳ���ȷ������������");
		return false;
	} else if (password.length > 20) {
		$("#password").removeClass().addClass("itxt text-error highlight2");
		$("#password_error").removeClass().addClass("msg-error").html("���볤�Ȳ���ȷ������������");
		return false;
	} else {
		var   pattern_1   =   /^.*([\W_])+.*$/i;
		var   pattern_2   =   /^.*([a-zA-Z])+.*$/i;
		var   pattern_3   =   /^.*([0-9])+.*$/i;
		var strength = 0;
		if(password.length>10){
			strength++;
		}
		if(pattern_1.test(password)){
			strength++;
		}
		if(pattern_2.test(password)){
			strength++;
		}
		if(pattern_3.test(password)){
			strength++;
		}
		if(strength<=1){
			$("#password").removeClass().addClass("itxt text-error highlight2");
			$("#password_error").removeClass().addClass("msg-error").html("����̫�����б������գ��������ɶ����ַ���ɵĸ�������");
			return false;
		}
		if(strength==2){
			$("#pwdstrength").show();
			$("#pwdstrength i").removeClass().addClass('safe-rank04');
			//$("#pwdstrength").removeClass().addClass("strengthB");
			$("#password").removeClass().addClass("itxt");
			$("#password_error").removeClass().html("");
		}
		if(strength>=3){
			$("#pwdstrength").show();
			$("#pwdstrength i").removeClass().addClass('safe-rank06');
			//$("#pwdstrength").removeClass().addClass("strengthC");
			$("#password").removeClass().addClass("itxt");
			$("#password_error").removeClass().html("");
		}
	}
	return true;
}

function usernameOnblur(){
	$("#username").removeClass().addClass("itxt");
	var username = $("#username").val();
	if(!username){
		$("#username").removeClass().addClass("itxt");
		$("#username_error").removeClass().html("");
		return;
	}
	checkUsername();
}

function usernameOnfocus(){
	var username = $("#username").val();
	if(username == "�û���/����/����֤�ֻ�"){
		$("#username").val("");
	}
	$("#username").removeClass().addClass("itxt highlight1");
	$("#username_error").removeClass().addClass("msg-text").html("����������û���/����/����֤�ֻ�");
}

function checkUsername(){
	var username = $("#username").val();
	if(!username){
		$("#username").removeClass().addClass("itxt highlight2");
		$("#username_error").removeClass().addClass("msg-error").html("����д����û���/����/����֤�ֻ�");
		return false;
	}

	if(username == "�û���/����/����֤�ֻ�"){
		$("#username").val("");
		$("#username").removeClass().addClass("itxt highlight2");
		$("#username_error").removeClass().addClass("msg-error").html("����д����û���/����/����֤�ֻ�");
		return false;
	}

	$("#username").removeClass().addClass("itxt");
	$("#username_error").removeClass().html("");
	return true;
}

function nameOnfocus(){
	var username = $("#username").val();
	$("#username").removeClass().addClass("itxt highlight1");
	$("#username_error").removeClass().addClass("msg-text").html("�������û���");
}

function nameOnblur(){
	$("#username").removeClass().addClass("itxt");
	var username = $("#username").val();
	if(!username){
		$("#username").removeClass().addClass("itxt");
		$("#username_error").removeClass().html("");
		return;
	}
	checkName();
}
function checkName(){
	var username = $("#username").val();
	if(!username){
		$("#username").removeClass().addClass("itxt highlight2");
		$("#username_error").removeClass().addClass("msg-error").html("�������û���");
		return false;
	}

	if(username.replace(/[^\x00-\xff]/g,"**").length > 20){
		$("#username").removeClass().addClass("itxt highlight2");
		$("#username_error").removeClass().addClass("msg-error").html("�û�����ȷ");
		return false;
	}

	$("#username").removeClass().addClass("itxt");
	$("#username_error").removeClass().html("");
	return true;
}

function selectVerifyType(){
	var type = $("#type").val();
	if(type == "mobile"){
		$("#mobileDiv").show();
		$("#emailDiv").hide();
	}else if(type == "email"){
		$("#mobileDiv").hide();
		$("#emailDiv").show();
	}
}

function noChooseEmail(){
	$("#usernameDiv").hide();
}

function chooseInputUsername(email){
	if($("#chooseSubmit").attr("disabled")) {
		return;
	}
	if(!checkName()){
		 return;
	}
	$("#chooseSubmit").attr("disabled","disabled");
	var username = $("#username").val();
	jQuery.ajax({
		type : "post",
		dataType: "json",
		url : "/findPwd/chooseInputUsername.action?email="+email+"&username="+escape(username),
		success : function(data) {
			if(data && data.result == "ok"){
				window.location.href="/findPwd/findPwd.action?k="+data.k;
			}else if(data && data.result == "none"){
				$("#username").removeClass().addClass("itxt highlight2");
				$("#username_error").removeClass().addClass("msg-error").html("�������û���ƥ�䣬����������");
				$("#chooseSubmit").removeAttr("disabled");
				verc(uuid);
			}else{
				alert("�������ӳ�ʱ�������Ժ�����");
				$("#chooseSubmit").removeAttr("disabled");
				verc(uuid);
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#chooseSubmit").removeAttr("disabled");
			verc(uuid);
    	}
	});
}

var rsaKey;
function bodyRSA()
{
    setMaxDigits(130);
    rsaKey = new RSAKeyPair("10001","",
        "b316e0613bb3dd9d42f99f6591912fea92cb6e574c579232c50f259470a363691978d4f88c3959cf9b4d9e97ef9d43c2ad486437507624fc81e025082c9cd275d40fe1b318720099ec791ebae4faa52875dd4c8ae9dc2c17449138206f2110a70a26ba309e5c5e080003ccc2984dfbe9baf355fd0787fd882068c3273f5671e9");
}


function updatePassword(key, needMobile, needHistoryName) {
	if($("#resetPwdSubmit").attr("disabled")) {
		return;
	}
	if(needMobile == "true" && !checkMobile()){
		return;
	}
	if(needHistoryName == "true" && !checkHistoryName()){
		return;
	}
	if(!checkNewPasswordForm(newPassword)){
		 return;
	}
	if(!isSamePassword()){
		return;
	}
	$("#resetPwdSubmit").attr("disabled","disabled");
	var newPassword = $("#password").val();

    bodyRSA();
    newPassword  = encryptedString(rsaKey, newPassword);

	jQuery.ajax({
		type : "post",
		dataType: "json",
		url : "/findPwd/doResetPwd.action?key="+key+"&password="+encodeURIComponent(newPassword)+"&mobile="+$("#mobile").val()+"&historyName="+encodeURI(encodeURI($("#historyName").val())),
		success : function(data) {
			if(data && data.resultCode == "ok"){
				window.location.href = "/findPwd/resetPwdSuccess.action";
			}else if(data && data.resultCode == "passwordError"){
				$("#pwdstrength").hide();
				$("#password").removeClass().addClass("itxt highlight2");
				$("#password_error").removeClass().addClass("msg-error").html("�����ʽ����ȷ������������");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "mobileError"){
				$("#pwdstrength").hide();
				$("#mobile").removeClass().addClass("itxt highlight2");
				$("#mobile_error").removeClass().addClass("msg-error").html("δ����ʷ�����в鵽�ú��룬��ú�����С�����ⶩ�����롣����������");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "lock"){
				alert("��Ĳ�������Ƶ�������Ժ����ԡ�");
				window.location.href = "/findPwd/index.action";
			}else if(data && data.resultCode == "historyNameLock"){
				$("#historyName").removeClass().addClass("itxt highlight2")
				$("#historyName_error").html("�����������10�Σ�24Сʱ�ڲ����ô˷�ʽ�������롣����ϵ�ͷ�400-606-5500ת7�����һ�����");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "historyNameError"){
				var remain = data.historyNameRemainNum;
				$("#historyName").removeClass().addClass("itxt highlight2")
				$("#historyName_error").html("��ʷ�ջ����������24Сʱ�ڻ�������"+remain+"��");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "passwordEquals"){
				$("#pwdstrength").hide();
				$("#password").removeClass().addClass("itxt highlight2");
				$("#password_error").removeClass().addClass("msg-error").html("������;�����̫���ƣ����������á�");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "pinEquals"){
				$("#pwdstrength").hide();
				$("#password").removeClass().addClass("itxt highlight2");
				$("#password_error").removeClass().addClass("msg-error").html("�������˻���̫���ƣ����������á�");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "payPwdEquals"){
				$("#pwdstrength").hide();
				$("#password").removeClass().addClass("itxt highlight2");
				$("#password_error").removeClass().addClass("msg-error").html("�������֧������̫���ƣ����������á�");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "historyPwdEquals"){
                $("#pwdstrength").hide();
                $("#password").removeClass().addClass("itxt highlight2");
                $("#password_error").removeClass().addClass("msg-error").html("�����벻����������ʹ�ù��������ͬ�����������á�");
                $("#resetPwdSubmit").removeAttr("disabled");
            }else if(data && data.resultCode == "lockEquals"){
				$("#pwdstrength").hide();
				$("#password").removeClass().addClass("itxt highlight2");
				$("#password_error").removeClass().addClass("msg-error").html("�������б������գ����������ø߰�ȫ�Եĸ�������");
				$("#resetPwdSubmit").removeAttr("disabled");
			}else if(data && data.resultCode == "timeOut"){
				alert("������ʱ");
				window.location.href="/findPwd/index.action";
			}else{
				alert("�������ӳ�ʱ���������޸ĵ�¼����");
				$("#resetPwdSubmit").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#resetPwdSubmit").removeAttr("disabled");
    	}
	});
}

function sendFindPwdCode(k){
	if($("#sendMobileCode").attr("disabled")) {
		return;
	}
	$("#sendMobileCode").attr("disabled","disabled");
	jQuery.ajax({
		type : "get",
		url : "/findPwd/getCode.action?k="+k,
		success : function(data) {
			if(data == "1") {
				$("#timeDiv .ftx-01").text(119);
				$("#sendMobileCodeDiv").hide();
				$("#timeDiv").show();
				setTimeout(countDown, 1000);
				$("#code").removeAttr("disabled");
				$("#submitCode").removeAttr("disabled");
			}else if(data == "kError"){
				window.location.href="/findPwd/index.action";
			}else if(data == "-2") {
				alert("120���ڽ��ܻ�ȡһ����֤�룬���Ժ�����");
				$("#sendMobileCode").removeAttr("disabled");
			}else if(data == "-3") {
				window.location.href="/findPwd/getCodeCountOut.action";
			}else if(data == "lock"){
				window.location.href="/findPwd/lock.action";
			}else{
				alert("�������ӳ�ʱ�������»�ȡ��֤��");
				$("#sendMobileCode").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#sendMobileCode").removeAttr("disabled");
    	}
	});
}

function sendFindPwdHistoryCode(k){
	if($("#sendMobileCode").attr("disabled")) {
		return;
	}

	if(!checkHistoryMobile()){
		return;
	}

	$("#sendMobileCode").attr("disabled","disabled");
	jQuery.ajax({
		type : "get",
		url : "/findPwd/getHistoryCode.action?k="+k+"&mobile="+$("#mobile").val(),
		success : function(data) {
			if(data == "1") {
				$("#timeDiv .ftx-01").text(119);
				$("#sendMobileCodeDiv").hide();
				$("#timeDiv").show();
				setTimeout(countDown, 1000);
				$("#code").removeAttr("disabled");
				$("#submitCode").removeAttr("disabled");
			}else if(data == "-6"){
				$("#mobile").removeClass().addClass("itxt text-1 text-mar highlight2");
				$("#mobile_error").removeClass().addClass("msg-error").html("δ����ʷ�����в鵽�ú��룬��ú�����С�����ⶩ�����롣����������");
				$("#sendMobileCode").removeAttr("disabled");
			}else if(data == "-2") {
				alert("120���ڽ��ܻ�ȡһ����֤�룬���Ժ�����");
				$("#sendMobileCode").removeAttr("disabled");
			}else if(data == "-3") {
				window.location.href="/findPwd/getCodeCountOut.action";
			}else if(data == "lock"){
				window.location.href="/findPwd/lock.action";
			}else if(data.status == "mobileFailure"){
				$("#mobile").removeClass().addClass("itxt text-1 text-mar highlight2");
				$("#mobile_error").removeClass().addClass("msg-error").html("�ֻ�Ÿ�ʽ��������������");
				$("#sendMobileCode").removeAttr("disabled");
			}else if(data == "unpass") {
				alert("��ͨ����������֤���ͽ��������֤");
				window.location.href="/findPwd/index.action";
			}else{
				alert("�������ӳ�ʱ�������»�ȡ��֤��");
				$("#sendMobileCode").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#sendMobileCode").removeAttr("disabled");
    	}
	});
}

function sendFindPwdEmail(k){
	if($("#sendMail").attr("disabled")) {
		return;
	}
	$("#sendMail").attr("disabled","disabled");
	jQuery.ajax({
    	type : "get",
    	url : "/findPwd/sendValidEmail.action?k="+k,
    	success : function(data) {
    		if(data == "1"){
    			window.location.href="/findPwd/sendValidEmailSuccess.action?k="+k;
    		}else if(data == "-3"){
    			window.location.href="/findPwd/sendEmailCountOut.action";
    		}else if(data == "none"){
    			window.location.href="/findPwd/index.action";
    		}else if(data == "lock"){
    			window.location.href="/findPwd/lock.action";
    		}else{
    			alert("�������ӳ�ʱ����������");
    			$("#sendMail").removeAttr("disabled");
    			verc(uuid);
    		}
    	},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#sendMail").removeAttr("disabled");
			verc(uuid);
    	}
    });
}

function doIndex(uuid){
	if($("#findPwdSubmit").attr("disabled")) {
		return;
	}
	if(!checkUsername()){
		return;
	}
	
	$("#findPwdSubmit").attr("disabled","disabled");
	
	var username = $("#username").val();
	jQuery.ajax({
		type : "get",
		dataType: "json",
		url : "/findPwd/doIndex.action",
		data : "&uuid="+uuid+"&sourceId="+sourceId+"&username="+escape(username),
		success : function(data) {
			if(data && data.result == "ok"){
				//
				window.location.href="/findPwd/findPwd.action?k="+data.k;
			}else if(data && data.result == "choose"){
				window.location.href="/findPwd/choose.action?k="+data.k;
			}else if(data && data.result == "none"){
				$("#username").removeClass().addClass("itxt highlight2");
				$("#username_error").removeClass().addClass("msg-error").html("��������˻�����ڣ���˶Ժ��������롣");
				$("#findPwdSubmit").removeAttr("disabled");
				verc(uuid);
			}else if(data && data.result == "needUsername"){
				window.location.href="/findPwd/inputUsername.action?email="+username;
			}else if(data && data.result == "usernameFailure"){
				$("#username").removeClass().addClass("itxt highlight2");
				$("#username_error").removeClass().addClass("msg-error").html("�������û���");
				$("#findPwdSubmit").removeAttr("disabled");
				verc(uuid);
			}else{
				alert("�������ӳ�ʱ���������޸ĵ�¼����");
				$("#findPwdSubmit").removeAttr("disabled");
				verc(uuid);
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#findPwdSubmit").removeAttr("disabled");
			verc(uuid);
    	}
	});
}

function checkEmailOnly(email){

	if($("alreadyCheck").val() == "1"){
		if($("needUsername").val() == "1"){
			$('#usernameDiv').show();
		}
		return;
	}

	jQuery.ajax({
		type : "get",
		url : "/findPwd/checkEmailOnly.action?email="+email,
		success : function(data) {
			if(data == "only"){
				$("#needUsername").val("0");
				$("#alreadyCheck").val("1");
				$('#usernameDiv').hide();
			}else if(data == "notOnly"){
				$("#needUsername").val("1");
				$("#alreadyCheck").val("1");
				$('#usernameDiv').show();
			}else if(data == "none"){
				window.location.href = "/findPwd/index.action";
			}else{
				$("#usernameRadio").attr("checked","checked");
			}
		},
    	error : function() {
			$("#usernameRadio").attr("checked","checked");
    	}
	});
}

function doChoose(k){
	if($("#chooseSubmit").attr("disabled")){
		return ;
	}

	if($("input:checked").val() == "email" && $("#needUsername").val() == "1" && !checkName()){
		return;
	}
	var username = $("#username").val();
	$("#chooseSubmit").attr("disabled","disabled");
	jQuery.ajax({
		type : "get",
		url : "/findPwd/doChoose.action?k="+k+"&type="+$("input:checked").val()+"&username="+escape(username),
		success : function(data) {
			if(data && data == "ok"){
				window.location.href = "/findPwd/findPwd.action?k="+k;
			}else if(data && data == "none"){
				window.location.href = "/findPwd/index.action";
			}else if(data && data == "notSame"){
				$("#username").removeClass().addClass("itxt highlight2");
				$("#username_error").removeClass().addClass("msg-error").html("�������û���ƥ�䣬����������");
				$("#chooseSubmit").removeAttr("disabled");
			}else if(data && data == "usernameError"){
				$("#username").removeClass().addClass("itxt highlight2");
				$("#username_error").removeClass().addClass("msg-error").html("�������û���ƥ�䣬����������");
				$("#chooseSubmit").removeAttr("disabled");
			}else if(data && data == "emailInfoError"){
				window.location.href = "/findPwd/index.action";
			}else{
				alert("�������ӳ�ʱ��������");
				$("#chooseSubmit").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ��������");
			$("#chooseSubmit").removeAttr("disabled");
    	}
	});
}

function validFindPwdCode(k){
	if($("#submitCode").attr("disabled")){
		return ;
	}
	var code = $("#code").val();
	if(!checkCode()){
		return ;
	}
	$("#submitCode").attr("disabled","disabled");
	jQuery.ajax({
		type : "get",
		dataType: "json",
		url : "/findPwd/validFindPwdCode.action?code="+code+"&k="+k,
		success : function(data) {
			if(data && data.result == "ok"){
				window.location.href = "/findPwd/resetPassword.action?key="+data.key;
			}else if(data && data.result == "codeFailure"){
				$("#code").removeClass().addClass("itxt text-1 highlight2");
				$("#code_error").removeClass().addClass("msg-error").html("��֤�����");
				$("#submitCode").removeAttr("disabled");
			}else if(data && data.result == "visitLock"){
				$("#code").removeClass().addClass("itxt text-1 highlight2");
				$("#code_error").removeClass().addClass("msg-error").html("��֤�����");
				$("#submitCode").removeAttr("disabled");
			}else if(data && data.result == "lock"){
				window.location.href = "/findPwd/lock.action";
			}else{
				alert("�������ӳ�ʱ�������Ժ�����");
				$("#submitCode").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#submitCode").removeAttr("disabled");
    	}
	});
}

function validFindPwdHistoryCode(k){
	if($("#submitCode").attr("disabled")){
		return ;
	}
	var code = $("#code").val();
	if(!checkCode()){
		return ;
	}
	$("#submitCode").attr("disabled","disabled");
	jQuery.ajax({
		type : "get",
		dataType: "json",
		url : "/findPwd/validFindPwdHistoryCode.action?code="+code+"&k="+k,
		success : function(data) {
			if(data && data.result == "ok"){
				window.location.href = "/findPwd/resetPassword.action?key="+data.key;
			}else if(data && data.result == "timeOut"){
				window.location.href = "/findPwd/index.action";
			}else if(data && data.result == "codeFailure"){
				$("#code").removeClass().addClass("itxt text-1 highlight2");
				$("#code_error").removeClass().addClass("msg-error").html("��֤�����");
				$("#submitCode").removeAttr("disabled");
			}else if(data && data.result == "visitLock"){
				$("#code").removeClass().addClass("itxt text-1 highlight2");
				$("#code_error").removeClass().addClass("msg-error").html("��֤�����");
				$("#submitCode").removeAttr("disabled");
			}else if(data && data.result == "lock"){
				window.location.href = "/findPwd/lock.action";
			}else{
				alert("�������ӳ�ʱ�������Ժ�����");
				$("#submitCode").removeAttr("disabled");
			}
		},
    	error : function() {
			alert("�������ӳ�ʱ�������Ժ�����");
			$("#submitCode").removeAttr("disabled");
    	}
	});
}

function checkCode(){
	var code = $("#code").val();
	if(!code){
		$("#code_error").removeClass().addClass("msg-error").html("��������֤��");
		return false;
	}
	$("#code_error").removeClass().html("");
	return true;
}

function checkAuthCode(){
	var authCode = $("#authCode").val();
	if(!authCode){
		$("#authCode").removeClass().addClass("itxt text-1 highlight2");
		$("#authCode_error").removeClass().addClass("msg-error").html("��������֤��");
		return false;
	}
	$("#authCode").removeClass().addClass("itxt text-1");
	$("#authCode_error").removeClass().html("");
	return true;
}

function countDown(){
	var time = $("#timeDiv .ftx-01").text();
	$("#timeDiv .ftx-01").text(time - 1);
	if (time == 1) {
		$("#sendMobileCode").removeAttr("disabled");
		$("#timeDiv").hide();
		$("#sendMobileCodeDiv").show();
		$("#send_text").hide();
	} else {
		setTimeout(countDown, 1000);
	}
}

function verc(uuid){
	$("#authCode").val("");
	$("#authCode").focus();
	$("#JD_Verification1").attr("src","http://authcode.jd.com/verify/image?acid="+uuid+"&srcid=" + sourceId + "&_t="+new Date().getTime());
}


//function vercAcid(uuid){
//	$("#authCode").val("");
//	$("#authCode").focus();
//	$("#JD_Verification1").attr("src","http://authcode.jd.com/verify/image?acid="+uuid+"&srcid=" + sourceId + "&_t="+new Date().getTime());
//}