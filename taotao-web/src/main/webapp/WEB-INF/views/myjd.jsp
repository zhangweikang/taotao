<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>账户信息</title>
<link type="text/css" rel="stylesheet"
	href="http://misc.360buyimg.com/jdf/1.0.0/unit/??ui-base/1.0.0/ui-base.css,shortcut/2.0.0/shortcut.css,global-header/1.0.0/global-header.css,myjd/2.0.0/myjd.css,nav/2.0.0/nav.css,shoppingcart/2.0.0/shoppingcart.css,global-footer/1.0.0/global-footer.css,service/1.0.0/service.css,basePatch/1.0.0/basePatch.css" />
<script type="text/javascript"
	src="http://misc.360buyimg.com/jdf/1.0.0/unit/??base/1.0.0/base.js,basePatch/1.0.0/basePatch.js"></script>
<link type="text/css" rel="stylesheet"
	href="http://misc.360buyimg.com/user/myjd-2015/css/myjd.info.css"
	source="combo" />
<link type="text/css" rel="stylesheet"
	href="http://misc.360buyimg.com/user/myjd-2015/widget/??common/common.css"
	source="widget" />
</head>

<!--set|info-->
<body myjd="_MYJD_info" menuId="set">
	<div id="shortcut-2014">
		<div class="w">
			<ul class="fl">
				<li class="dorpdown" id="ttbar-mycity"></li>
			</ul>
			<ul class="fr">
				<li class="fore1" id="ttbar-login"><a target="_blank"
					href="javascript:login();" class="link-login">你好，请登录</a>&nbsp;&nbsp;<a
					href="javascript:regist();" class="link-regist style-red">免费注册</a>
				</li>
				<li class="spacer"></li>
				<li class="fore2">
					<div class="dt">
						<a target="_blank" href="http://order.jd.com/center/list.action">我的订单</a>
					</div>
				</li>
				<li class="spacer"></li>
				<li class="fore3 dorpdown" id="ttbar-myjd">
					<div class="dt cw-icon">
						<i class="ci-right"><s>◇</s></i> <a target="_blank"
							href="http://home.jd.com/">我的京东</a>
					</div>
					<div class="dd dorpdown-layer"></div>
				</li>
				<li class="spacer"></li>
				<li class="fore4">
					<div class="dt">
						<a target="_blank" href="http://vip.jd.com/">会员俱乐部</a>
					</div>
				</li>
				<li class="spacer"></li>
				<li class="fore5">
					<div class="dt">
						<a target="_blank" href="http://b.jd.com/">企业频道</a>
					</div>
				</li>
				<li class="spacer"></li>
				<li class="fore6 dorpdown" id="ttbar-apps">
					<div class="dt cw-icon">
						<i class="ci-left"></i> <i class="ci-right"><s>◇</s></i> <a
							target="_blank" href="http://app.jd.com/">手机京东</a>
					</div>
				</li>
				<li class="spacer"></li>
				<li class="fore7 dorpdown" id="ttbar-atte">
					<div class="dt cw-icon">
						<i class="ci-right"><s>◇</s></i>关注京东
					</div>
					<div class="dd dorpdown-layer"></div>
				</li>
				<li class="spacer"></li>
				<li class="fore8 dorpdown" id="ttbar-serv">
					<div class="dt cw-icon">
						<i class="ci-right"><s>◇</s></i>客户服务
					</div>
					<div class="dd dorpdown-layer"></div>
				</li>
				<li class="spacer"></li>
				<li class="fore9 dorpdown" id="ttbar-navs">
					<div class="dt cw-icon">
						<i class="ci-right"><s>◇</s></i>网站导航
					</div>
					<div class="dd dorpdown-layer"></div>
				</li>
			</ul>
			<span class="clr"></span>
		</div>
	</div>
	<div id="o-header-2013">
		<div id="header-2013" style="display: none;"></div>
	</div>
	<div id="nav">
		<div class="w">
			<div class="logo">
				<a href="http://www.jd.com" target="_blank" class="fore1"></a> <a
					href="http://home.jd.com" target="_self" class="fore2">我的京东</a> <a
					href="http://www.jd.com" target="_blank" class="fore3">返回京东首页</a>
			</div>
			<div class="navitems">
				<ul>
					<li class="fore-1"><a target="_self" href="http://home.jd.com">首页</a>
					</li>
					<li class="fore-2"><a href="http://me.jd.com" target="_blank"
						clstag="click|keycount|myhome|hgr">个人主页</a></li>
					<li class="fore-3">
						<div class="dl" clstag="click|keycount|myhome|hsz">
							<div class="dt">
								<span class="myjd-set">账户设置</span> <b></b>
							</div>
							<div class="dd">
								<a tid="_MYJD_info" clstag="Homeyser"
									href="http://i.jd.com/user/info" target="_self"><span>个人信息</span></a>
								<a tid="_MYJD_safe" clstag="Homesafe"
									href="http://safe.jd.com/user/paymentpassword/safetyCenter.action"
									target="_self"><span>账户安全</span></a> <a tid="_MYJD_grade"
									clstag="Homegrade" href="http://usergrade.jd.com/user/grade"
									target="_self"><span>我的级别</span></a> <a tid="_MYJD_comments"
									clstag="homeadd"
									href="http://easybuy.jd.com/address/getEasyBuyList.action"
									target="_self"><span>收货地址</span></a> <a tid="_MYJD_share"
									clstag="Homeshare" href="http://share.jd.com/share/index.html"
									target="_self"><span>分享绑定</span></a> <a tid="_MYJD_rss"
									clstag="Homeedm"
									href="http://edm.jd.com/front/subscribe/index.aspx"
									target="_self"><span>邮件订阅</span></a> <a tid="_MYJD_recor"
									clstag="Homeedm" href="http://usergrade.jd.com/user/consume"
									target="_self"><span>消费记录</span></a> <a tid="_MYJD_app"
									clstag="Homeedm" href="http://fw.jd.com/home/auth_list.action"
									target="_blank"><span>应用授权</span></a> <a tid="_MYJD_pay"
									clstag="Homequick"
									href="https://cashier.jd.com/quick/quickBindCard.action"
									target="_blank"><span>快捷支付</span></a> <a tid="_MYJD_zpzz"
									clstag="Homezpzz"
									href="http://invoice.jd.com/user/userinfo/zpzz.html"
									target="_self"><span>增票资质</span></a>
							</div>
						</div>
					</li>
					<li class="fore-4">
						<div class="dl myjd-info" clstag="click|keycount|myhome|hsq">
							<div class="dt ">
								<span>社区</span> <b></b>
							</div>
							<div class="dd">
								<a tid="_MYJD_joy" clstag="Homemsg"
									href="http://joycenter.jd.com" target="_self"><span>消息精灵</span></a>
								<a tid="_MYJD_activities" clstag="Homeclub"
									href="http://luck.jd.com/myjd/myJoinActivity.html"
									target="_self"><span>我的活动</span></a> <a tid="_MYJD_circle"
									clstag="Homejoincircle"
									href="http://group.jd.com/circle/myjoincircle.htm"
									target="_self"><span>我的圈子</span></a> <a tid="_MYJD_mycollect"
									clstag="Homethread"
									href="http://group.jd.com/thread/mythread.htm" target="_self"><span>我的帖子</span></a>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="nav-r">
				<div id="search-2014">
					<ul id="shelper" class="hide"></ul>
					<div class="form">
						<input type="text"
							onkeydown="javascript:if(event.keyCode==13) search('key');"
							autocomplete="off" id="key" accesskey="s" class="text" />
						<button onclick="search('key');return false;"
							class="button cw-icon">
							<i></i>搜索
						</button>
					</div>
				</div>
				<div id="settleup-2014" class="dorpdown">
					<div class="cw-icon">
						<i class="ci-left"></i> <i class="ci-right">&gt;</i> <a
							target="_blank" href="http://cart.jd.com/cart/cart.html">我的购物车</a>
					</div>
					<div class="dorpdown-layer">
						<div class="spacer"></div>
						<div id="settleup-content">
							<span class="loading"></span>
						</div>
					</div>
				</div>
				<div id="hotwords-2014"></div>
			</div>
			<div class="clr"></div>
		</div>
	</div>

	<!--container start-->
	<div id="container">
		<div class="w">
			<div id="content">
				<div id="sub">
					<div id="menu">
						<dl class="fore1">
							<dt id="_MYJD_setting">设置</dt>
							<dd class="fore1_1" id="_MYJD_info">
								<a clstag="homepage|keycount|home2013|Homeyser"
									href="http://i.jd.com/user/info" target="_self">个人信息</a>
							</dd>
							<dd class="fore1_2" id="_MYJD_safe">
								<a clstag="homepage|keycount|home2013|Homesafe"
									href="http://safe.jd.com/user/paymentpassword/safetyCenter.action"
									target="_self">账户安全</a>
							</dd>
							<dd class="fore1_3" id="_MYJD_grade">
								<a clstag="homepage|keycount|home2013|Homegrade"
									href="http://usergrade.jd.com/user/grade" target="_self">我的级别</a>
							</dd>
							<dd class="fore1_4" id="_MYJD_comments">
								<a clstag="homepage|keycount|home2013|homeadd"
									href="http://easybuy.jd.com/address/getEasyBuyList.action"
									target="_self">收货地址</a>
							</dd>
							<dd class="fore1_5" id="_MYJD_share">
								<a clstag="homepage|keycount|home2013|Homeshare"
									href="http://share.jd.com/share/index.html" target="_self">分享绑定</a>
							</dd>
							<dd class="fore1_6" id="_MYJD_rss">
								<a clstag="homepage|keycount|home2013|Homeedm"
									href="http://edm.jd.com/front/subscribe/index.aspx"
									target="_self">邮件订阅</a>
							</dd>
							<dd class="fore1_7" id="_MYJD_recor">
								<a clstag="homepage|keycount|home2013|Homeedm"
									href="http://usergrade.jd.com/user/consume" target="_self">消费记录</a>
							</dd>
							<dd class="fore1_8" id="_MYJD_app">
								<a clstag="homepage|keycount|home2013|Homeedm"
									href="http://fw.jd.com/home/auth_list.action" target="_blank">应用授权</a>
							</dd>
							<dd class="fore1_9" id="_MYJD_pay">
								<a clstag="homepage|keycount|home2013|Homequick"
									href="https://quickpay.jd.com/bankCard.action" target="_blank">快捷支付</a>
							</dd>
							<dd class="fore1_10" id="_MYJD_zpzz">
								<a clstag="homepage|keycount|home2013|Homezpzz"
									href="http://invoice.jd.com/user/userinfo/zpzz.html"
									target="_self">增票资质</a>
							</dd>
						</dl>
					</div>
				</div>
				<div id="main">
					<div class="mod-main">
						<div class="mt">
							<ul class="extra-l">
								<li class="fore-1"><a href="http://i.jd.com/user/info">基本信息</a></li>
								<li class="fore-2"><a class="curr"
									href="http://i.jd.com/user/userinfo/showImg.html">头像照片</a></li>
								<li class="fore-3"><a
									href="http://i.jd.com/user/userinfo/more.html">更多个人信息</a></li>

							</ul>
						</div>
						<div class="mc update-face-cont">
							<div class="update-lcol">
								<div class="mb10">
									<span id="spanButtonPlaceHolder"></span> <img id="loading"
										class="float:left" src="http://i.jd.com/images/loading.gif"
										style="display: none" />
									<div id="messages"
										style="border: 1px solid #DB9A9A; background-color: #FFE8E8; color: #CC0000; text-align: left; display: none"></div>
									<input id="btnCancel" type="hidden" />
									<div class="ftx03">仅支持JPG、GIF、PNG、JPEG、BMP格式，文件小于4M</div>
								</div>
								<div class="img-b-cont img-cont ">
									<!--<div class="tip">编辑预览区</div>-->
									<div class="img-b">
										<img id="bigImage" name="bigImage" alt="" width="150"
											height="150" />
									</div>
								</div>
								<div class="smt">
									<h3>推荐头像</h3>
								</div>
								<div class="smc face-list">
									<ul class="imgUl"
										clstag="homepage|keycount|home2013|infoshowtx2">
										<li value="1"><img
											src="http://i.jd.com/defaultImgs/1.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="2"><img
											src="http://i.jd.com/defaultImgs/2.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="3"><img
											src="http://i.jd.com/defaultImgs/3.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="4"><img
											src="http://i.jd.com/defaultImgs/4.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="5"><img
											src="http://i.jd.com/defaultImgs/5.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="6"><img
											src="http://i.jd.com/defaultImgs/6.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="7"><img
											src="http://i.jd.com/defaultImgs/7.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="8"><img
											src="http://i.jd.com/defaultImgs/8.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="9"><img
											src="http://i.jd.com/defaultImgs/9.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="10"><img
											src="http://i.jd.com/defaultImgs/10.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="11"><img
											src="http://i.jd.com/defaultImgs/11.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="12"><img
											src="http://i.jd.com/defaultImgs/12.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="13"><img
											src="http://i.jd.com/defaultImgs/13.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="14"><img
											src="http://i.jd.com/defaultImgs/14.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="15"><img
											src="http://i.jd.com/defaultImgs/15.jpg" alt="" width="50"
											height="50" /><b></b></li>
										<li value="16"><img
											src="http://i.jd.com/defaultImgs/16.jpg" alt="" width="50"
											height="50" /><b></b></li>
									</ul>
									<div class="btns mt20">
										<a clstag="homepage|keycount|home2013|infoshowbtn"
											href="javascript:void(0)" class="btn-5 mr10"
											onclick="uploadDefaultImg()">保存</a>
									</div>
								</div>
							</div>
							<div class="update-rcol">
								<div class="smt">
									<h3>效果预览</h3>
								</div>
								<div class="smc">
									你上传的图片会自动生成2种尺寸，请注意小尺寸的头像是否清晰
									<div class="img-m-cont img-cont">
										<div class="img-s">
											<img id="midImage" name="midImage"
												src="http://storage.jd.com/i.imageUpload/71713137393937353438393231343239323431313030303338_mid.jpg" />
										</div>
									</div>
									100*100像素
									<div class="img-s-cont img-cont">
										<div class="img-s">
											<img id="smaImage" name="smaImage"
												src="http://storage.jd.com/i.imageUpload/71713137393937353438393231343239323431313030303338_sma.jpg" />
										</div>
									</div>
									50*50像素
								</div>
							</div>
							<div class="clr"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--container end-->

	<div id="service-2014">
		<div class="w">
			<dl class="fore1">
				<dt>购物指南</dt>
				<dd>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-29.html">购物流程</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-151.html">会员介绍</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-297.html">生活旅行/团购</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue.html">常见问题</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-136.html">大家电</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/index.html">联系客服</a>
					</div>
				</dd>
			</dl>
			<dl class="fore2">
				<dt>配送方式</dt>
				<dd>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-81-100.html">上门自提</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-81.html">211限时达</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/103-983.html">配送服务查询</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/109-188.html">配送费收取标准</a>
					</div>
					<div>
						<a target="_blank" href="http://en.jd.com/chinese.html">海外配送</a>
					</div>
				</dd>
			</dl>
			<dl class="fore3">
				<dt>支付方式</dt>
				<dd>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-172.html">货到付款</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-173.html">在线支付</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-176.html">分期付款</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-174.html">邮局汇款</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-175.html">公司转账</a>
					</div>
				</dd>
			</dl>
			<dl class="fore4">
				<dt>售后服务</dt>
				<dd>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/321-981.html">售后政策</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-132.html">价格保护</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/130-978.html">退款说明</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://myjd.jd.com/repair/repairs.action">返修/退换货</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://help.jd.com/user/issue/list-50.html">取消订单</a>
					</div>
				</dd>
			</dl>
			<dl class="fore5">
				<dt>特色服务</dt>
				<dd>
					<div>
						<a target="_blank"
							href="http://help.jd.com/user/issue/list-133.html">夺宝岛</a>
					</div>
					<div>
						<a target="_blank"
							href="http://help.jd.com/user/issue/list-134.html">DIY装机</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank" href="http://fuwu.jd.com/">延保服务</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank"
							href="http://giftcard.jd.com/market/index.action">京东E卡</a>
					</div>
					<div>
						<a rel="nofollow" target="_blank" href="http://mobile.jd.com/">京东通信</a>
					</div>
				</dd>
			</dl>
			<span class="clr"></span>
		</div>
	</div>
	<div class="w">
		<div id="footer-2014">
			<div class="links">
				<a rel="nofollow" target="_blank"
					href="http://www.jd.com/intro/about.aspx">关于我们</a>|<a
					rel="nofollow" target="_blank" href="http://www.jd.com/contact/">联系我们</a>|<a
					rel="nofollow" target="_blank"
					href="http://www.jd.com/contact/joinin.aspx">商家入驻</a>|<a
					rel="nofollow" target="_blank" href="http://jzt.jd.com">营销中心</a>|<a
					rel="nofollow" target="_blank" href="http://app.jd.com/">手机京东</a>|<a
					target="_blank" href="http://club.jd.com/links.aspx">友情链接</a>|<a
					target="_blank" href="http://media.jd.com/">销售联盟</a>|<a
					href="http://club.jd.com/" target="_blank">京东社区</a>|<a
					href="http://gongyi.jd.com" target="_blank">京东公益</a>|<a
					href="http://en.jd.com/" target="_blank">English Site</a>|<a
					href="http://help.en.jd.com/help/question-46.html" target="_blank">Contact
					Us</a>
			</div>
			<div class="copyright">
				北京市公安局朝阳分局备案编号110105014669&nbsp;&nbsp;|&nbsp;&nbsp;京ICP证070359号&nbsp;&nbsp;|&nbsp;&nbsp;<a
					target="_blank"
					href="http://img14.360buyimg.com/da/jfs/t256/349/769670066/270505/3b03e0bb/53f16c24N7c04d9e9.jpg">互联网药品信息服务资格证编号(京)-经营性-2014-0008</a>&nbsp;&nbsp;|&nbsp;&nbsp;新出发京零&nbsp;字第大120007号<br>
				<a rel="nofollow"
					href="http://misc.360buyimg.com/skin/df/i/com/f_music.jpg"
					target="_blank">音像制品经营许可证苏宿批005号</a>&nbsp;&nbsp;|&nbsp;&nbsp;出版物经营许可证编号新出发(苏)批字第N-012号&nbsp;&nbsp;|&nbsp;&nbsp;互联网出版许可证编号新出网证(京)字150号<br>
				<a href="http://misc.360buyimg.com/wz/wlwhjyxkz.jpg" target="_blank">网络文化经营许可证京网文[2014]2148-348号</a>&nbsp;&nbsp;违法和不良信息举报电话：4006561155&nbsp;&nbsp;Copyright&nbsp;&copy;&nbsp;2004-2015&nbsp;&nbsp;京东JD.com&nbsp;版权所有<br>京东旗下网站：<a
					target="_blank" href="http://www.360top.com/">360TOP</a>&nbsp;&nbsp;<a
					href="http://www.paipai.com/" target="_blank">拍拍网</a>&nbsp;&nbsp;<a
					href="https://www.wangyin.com/" target="_blank">网银在线</a>
			</div>
			<div class="authentication">
				<a rel="nofollow" target="_blank"
					href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202007080200026">
					<img width="103" height="32" alt="经营性网站备案中心"
					src="http://img12.360buyimg.com/da/jfs/t535/349/1185317137/2350/7fc5b9e4/54b8871eNa9a7067e.png"
					class="err-product" />
				</a>
				<script type="text/JavaScript">
					function CNNIC_change(eleId) {
						var str = document.getElementById(eleId).href;
						var str1 = str.substring(0, (str.length - 6));
						str1 += CNNIC_RndNum(6);
						document.getElementById(eleId).href = str1;
					}
					function CNNIC_RndNum(k) {
						var rnd = "";
						for (var i = 0; i < k; i++)
							rnd += Math.floor(Math.random() * 10);
						return rnd;
					}
				</script>
				<a rel="nofollow" target="_blank" id="urlknet" tabindex="-1"
					href="https://ss.knet.cn/verifyseal.dll?sn=2008070300100000031&amp;ct=df&amp;pa=294005">
					<img border="true" width="103" height="32"
					onclick="CNNIC_change('urlknet')" oncontextmenu="return false;"
					name="CNNIC_seal" alt="可信网站"
					src="http://img11.360buyimg.com/da/jfs/t643/61/1174624553/2576/4037eb5f/54b8872dNe37a9860.png"
					class="err-product" />
				</a> <a rel="nofollow" target="_blank"
					href="http://www.bj.cyberpolice.cn/index.do"> <img width="103"
					height="32" alt="朝阳网络警察"
					src="http://img11.360buyimg.com/da/jfs/t559/186/1172042286/2795/7d90b036/54b8874bN694454a5.png"
					class="err-product" />
				</a> <a rel="nofollow" target="_blank"
					href="https://search.szfw.org/cert/l/CX20120111001803001836"> <img
					width="103" height="32"
					src="http://img11.360buyimg.com/da/jfs/t451/173/1189513923/1992/ec69b14a/54b8875fNad1e0c4c.png"
					class="err-product" />
				</a> <a target="_blank" href="http://jubao.china.cn:13225/reportform.do">
					<img width="185" height="32"
					src="http://img10.360buyimg.com/da/jfs/t520/303/1151687373/1180/2f8340fc/54b8863dN8d2c61ec.png"
					class="err-product" />
				</a>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="http://wl.jd.com/wl.js"></script>

	<script type="text/javascript"
		src="http://misc.360buyimg.com/jdf/??lib/jquery-1.6.4.js,1.0.0/unit/libPath/1.0.0/libPath.js"></script>
	<script type="text/javascript"
		src="http://misc.360buyimg.com/user/myjd-2015/widget/??common/common.js"
		source="widget"></script>

	<script type="text/javascript" src="http://i.jd.com/js/swfupload.js"></script>
	<script type="text/javascript" src="http://i.jd.com/js/handlers.js"></script>
	<script type="text/javascript"
		src="http://i.jd.com/js/userinfo.upload.js"></script>
	<script type="text/javascript">
		document.domain = pageConfig.FN_getDomain();
		$(document)
				.ready(
						function() {
							$("#bigImage")
									.attr(
											'src',
											"http://storage.jd.com/i.imageUpload/71713137393937353438393231343239323431313030303338_big.jpg");
							var settings = {
								flash_url : "http://i.jd.com/commons/swfupload.swf",
								upload_url : "http://i.jd.com/user/upload/image.action",
								post_params : {
									"flashuploadimg" : "5C888AE7E8D1B87A1AA94282EDA1BF9F1904FC3FD1CC141D72384EA953808C58B7E019FB7A9DABB6EDA8188807DFAA96D0BF4809DA400EA11139D4E7355DB74AB87718085C7B1ABA3AABAD2DB7EE9C609D13E1984EBC9CC4C817D6D742BEFCD10708DAA621CFC3B46996E1C3CD0EBC47AD2343AA2010C31A6187DA532892BC571CF286A4BCE3080500638229B50EFBC0"
								},
								file_post_name : "file",
								file_size_limit : "4 MB",
								file_types : "*.jpg;*.gif;*.png;*.jpeg;*.bmp",
								file_types_description : "img",
								custom_settings : {
									cancelButtonId : "btnCancel"
								},
								debug : false,
								// Button settings
								button_image_url : "http://i.jd.com/images/perfect_bg.jpg",
								button_width : "202",
								button_height : "34",
								button_placeholder_id : "spanButtonPlaceHolder",
								button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,

								file_queued_handler : fileQueued,
								file_queue_error_handler : fileQueueError,
								file_dialog_complete_handler : fileDialogComplete,
								upload_error_handler : uploadError,
								upload_success_handler : uploadSuccess

							};
							swfu = new SWFUpload(settings);
						});

		function froward() {
			alert("头像上传成功");
			window.location.href = 'http://i.jd.com/user/userinfo/show.action?t='
					+ new Date().getTime();
		}
		function showMessages(str) {
			document.getElementById("messages").style.display = "block";
			$("#messages").html(str);
		}

		$(".imgUl").children().click(
				function() {
					if ($(this).attr("class") == ""
							|| $(this).attr("class") == undefined) {
						$(".imgUl").children().each(function() {
							$(this).removeClass("selected");
						});
						$(this).addClass("selected");
					} else {
						$(this).removeClass("selected");
					}
				})
	</script>

	<!--main-->
</body>
</html>



