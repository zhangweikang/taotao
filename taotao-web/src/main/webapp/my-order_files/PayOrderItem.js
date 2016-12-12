var payHtml = "<a href=\"{url}\" target=\"_blank\"><img width=\"46\" height=\"25\" style=\"display:inline;\" src=\"http://misc.360buyimg.com/jd2008/201010/skin/i/{img}.gif\"></a>";

var payToolBar = function() {
};

payToolBar.Ajax = function() {
	this.url = {
		"btn" : "http://odo.jd.com/payment/payBtn.action",
		"paid" : "http://odo.jd.com/payment/paid.action"
	}, this.GET = "GET", this.data = function() {
		return {
			json : arguments[0],
			r : Math.random()
		};
	};
};

payToolBar.Ajax.prototype = {
	Request : function(data, url, callback) {
		$.ajax( {
			type : this.GET,
			url : url,
			data : this.data(data),
			dataType : "jsonp",
			cache : false,
			success : callback
		});
	},
	Success : function(result) {
		if (result.result.isSuccess == true) {
            if(result.payList && result.payList.length > 0){
                with (result.payList[0]) {
                    var html = payHtml.replace("{url}", url).replace("{img}",
                            ishalf == true ? "btn201212" : "btn_pay");
                    $("#pay-button-" + id).html(html);
                }
            }
		}
	},
	Succeed : function(result) {
		if (result.result.isSuccess == true) {
			var html = "<ul class=\"fore\"><li class=\"fore1\">支付时间</li><li class=\"fore2\">支付单号</li><li class=\"fore3\">支付来源</li><li class=\"fore4\">金额</li></ul>";
			for(x in result.paidList){
				with(result.paidList[x]){
					html = html.concat("<ul class=\"fore0\"><li class=\"fore1\">"+payTime+"</li><li class=\"fore2\">"+payId+"</li><li class=\"fore3\">"+decodeURIComponent(payplat)+"</li><li class=\"fore4\"><strong class=\"ftx-01\">￥"+parseFloat(amount)+"</strong></li></ul>");
				}				
			}
			$(".tb-ul").html(html);
		}		
	}
};

payToolBar.todo = function() {
	var payOrder = $("#pay-button-order").html();
	if (payOrder != "" && payOrder != null && payOrder != "[]") {
		with ((new payToolBar.Ajax())) {
			Request("[" + payOrder + "]", url.btn, Success);
		}
	}
	
	with ((new payToolBar.Ajax())) {
		Request($("#orderid").val(), url.paid, Succeed);
	}
};
payToolBar.todo();