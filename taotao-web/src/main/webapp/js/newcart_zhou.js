/*
* 购物车
*
* modify by tongen@
* date 2014.11.18
*/
define(function(require, exports, module) {
    // 引入资源库
   /* var area  =  require('jdf/1.0.0/ui/area/1.0.0/area');
    var switchable   = require('jdf/1.0.0/ui/switchable/1.0.0/switchable');
    var dialog  =  require('jdf/1.0.0/ui/dialog/1.0.0/dialog');
    var login = require('jdf/1.0.0/unit/login/1.0.0/login');
    var notify = require('jdf/1.0.0/unit/notif/1.0.0/notif');
    var gotop  =  require('jdf/1.0.0/ui/gotop/1.0.0/gotop');
    var tips  =  require('jdf/1.0.0/ui/tips/1.1.0/tips');
    var trimpath = require('jdf/1.0.0/unit/trimPath/1.0.0/trimPath');
    var lazyload = require('jdf/1.0.0/ui/lazyload/1.0.0/lazyload');
    var global = require('jdf/1.0.0/unit/globalInit/1.0.0/globalInit');
    var fixable = require('jdf/1.0.0/ui/fixable/1.0.0/fixable');
*/
	
	 var login = require('login/login');
	
    var cartObj = {
        iurl: PurchaseAppConfig.SortedDynamicDomain,
        orderInfoUrl: this.iurl + "/gotoOrder/gotoOrder.action",
        alamp:null,
        ivender:0,
        ipLocation:'',

        outSkus: $('#outSkus').val() || '',
        allSkuIds: $('#allSkuIds').val() || '',

        init: function(){

/*            // 是否升级浏览器
            if(typeof showUpBrowserTips == 'function'){
                showUpBrowserTips();
            }
*/
            // 配送地址
            this.initArea();

//            this.initSearchUnit();

            // 绑定事件
            this.bindCartEvent();

            // 库存状态处理
            this.updateStoreState();

            // 扩展属性：礼品购、闪购、国际站等
            this.loadIconsFromExt();

            // 延保信息
            this.initYanBao();

            // 初始化京东国际的按钮
            this.initJInitButtons();

            //改变全部商品下的下划线
            this.resetUnderline();

            // 初始化吸底
            this.ceilingAlamp();

            //更新店铺信息
            this.getVenderInfo($("#venderIds").val());

            // 推荐位 鼠标滚动到下方时展开。
            //this.initRecommendAndCollection();
        },

        // 绑定事件：全部改为delegate
        bindCartEvent: function(){
            var me = this;

            // 购物车页面的登陆按钮
            $('.nologin-tip').delegate('a', 'click', function(){
                me.goToLogin();
            });

            // 删除商品 打开是否删除浮层
            $('.cart-warp').delegate('.cart-remove', 'click', function(){
                me.removeSkuDlg($(this));
            });

            // 删除商品 浮层上删除、是否批量操作
            $('body').delegate('.select-remove', 'click', function(){
                me.remove($(this), $(this).attr('data-batch') || false);
                $.closeDialog();
            });

            // 删除商品 浮层上移到我的关注、是否批量操作
            $('body').delegate('.re-select-follow', 'click', function(){
                me.follow($(this).attr('data-bind'), $(this).attr('data-batch') || false, null, $(this).attr('selectstate'));
                $.closeDialog();
            });

            // 移到我的关注
            $('.cart-warp').delegate('.cart-follow', 'click', function(){
                me.followSkuDlg($(this));
            });

            $('body').delegate('.select-follow', 'click', function(){
                me.follow($(this).attr('data-bind'), $(this).attr('data-batch') || false, $(this).attr('data-more'), $(this).attr('selectstate'));
                $.closeDialog();
            });

            $('body').delegate('.cancel-follow', 'click', function(){
                $.closeDialog();
            });

            //批量删除
            $('#cart-floatbar').delegate('a.remove-batch', 'click', function(){
                me.removeSkuDlg($(this));
            });

            //批量移到
            $('#cart-floatbar').delegate('a.follow-batch', 'click', function(){
                me.followSkuDlg($(this));
            });

            // 删除赠品
            $('.cart-warp').delegate('.remove-gift', 'click', function(){
                me.removeGift($(this).attr('id'));
            });

            // 商品自带赠品无货时删除
            $('.cart-warp').delegate('.remove-selfgift', 'click', function(){
                me.removeSelfgift($(this).attr('id'));
            });

            // 商品自带赠品有货时还原
            $('.cart-warp').delegate('.resume-selfgift', 'click', function(){
                me.resumeSelfgift($(this).attr('id'));
            });

            // 到货通知
            notify({el:$('.cart-notify')});

            // 选择一个商品、选择多个商品
            this.bindCheckEvent();

            // 增减商品数量
            this.bindskuNumEvent();

            // 满赠、满减
            this.initGiftEvent();

            // 重新购买
            this.initRebuyEvent();

            // 促销优惠
            this.initPromotionEvent();

            // 京豆优惠
            this.initJbeanEvent();

            // 选择京东服务
            this.initJdServices();

            //购买礼品包装
            this.initGiftcardEvent();

            // 底部：选中商品展开收起。
            this.initShowListEvent();

            // 去结算：普通
            $('.cart-warp').delegate('.submit-btn', 'click', function(){
                var binter = ($(this).attr('data-bind') == 2) ? true : false;
                me.gotoBalance(binter);
            });

            // 去结算：普通
            $('.cart-warp').delegate('.common-submit-btn', 'click', function(){
                me.gotoBalance();
            });

            // 去结算：京东国际
            $('.cart-warp').delegate('.jdInt-submit-btn', 'click', function(){
                me.gotoBalance(true);
            });

            // 清除页面上的窗口
            $('body').delegate('#container', 'click', function(event){
                var tg = event.target;

                // 目标不是窗口
                var notPromotionTip = $(tg).hasClass('promotion-tit') || !$(tg).hasClass('promotion-tips') && !$(tg).parents('.promotion-tips').length;
                var notServiceTip = $(tg).hasClass('jd-service-tit') || !$(tg).hasClass('service-tips') && !$(tg).parents('.service-tips').length;
                var notSelectTip = !$(tg).parents('.selected-item-list').length;
                var notGiftBox = !$(tg).parents('.gift-box').length;

                if(!notPromotionTip || !notSelectTip || !notServiceTip || !notGiftBox){
                    return;
                }

                if(notPromotionTip){
                    $('.promotion-tips').each(function(){
                        $(this).slideUp(200);
                        if(me.isLowIE()){
                            $(this).closest('.item-item').parent().css('z-index', 'auto');
                            $(this).closest('.item-item').css('z-index', 'auto');
                        }
                    });
                }

                if(notServiceTip){
                    $('.service-tips').each(function(){
                        $(this).slideUp(200);
                        if(me.isLowIE()){
                            $(this).closest('.item-item').parent().css('z-index', 'auto');
                            $(this).closest('.item-item').css('z-index', 'auto');
                        }
                    });
                }

                // 点击元素外其他地方，收起。
                if(notSelectTip){
                    $('.selected-item-list').hide();
                    $('.amount-sum b').removeClass('down').addClass('up');
                }

                if(notGiftBox){
                    $('.gift-box').each(function(){
                        $(this).hide();
                        if(me.isLowIE()){
                            $(this).parent().css('z-index', 'auto');
                        }
                    });
                }
            });

			/*
            // 猜你喜欢中的加入购物车
            $('body').delegate('#c-tabs .p-btn a', 'click', function(){
                me.addSkuToCartReBuy($(this).attr('_pid'), 1, 1, 0, 0);
            });
			
            // 返回顶部
            $('#bp-backtop').gotop({
                hasAnimate: true
            });
            */
        },

        //异步获取店铺的信息
        getVenderInfo: function(venderIds) {
            function resetVender(venderIds){
                if(!venderIds){
                    return;
                }
                var venders = venderIds.split(",");
                for(var i=0, l=venders.length; i<l; i++) {
                    $("#venderId_" + venders[i]).html("第三方商家").attr("href", "javascript:;").attr("target", "_self");
                }
            }

            jQuery.ajax({
                type     : "POST",
                async : true,
                dataType : "json",
                url  : this.iurl + '/getVenders.action',
                data : {venderIds : venderIds, random : Math.random()},
                success : function(result) {
                    result = result.sortedWebVenderResult;
                    if(result && result.success){
                        var popinfos = result.orderPopInfos;
                        var len = popinfos && popinfos.length || 0;
                        if(!len){
                            resetVender(venderIds);
                        }
                        for(var i = 0; i < len; i++) {
                            var info = popinfos[i];
                            var el = $("#venderId_" + info.venderId);
                            el.html(info.shopName || "第三方商家")
                              .attr("href", info.shopUrl || 'javascript:;');

                            if(info.shopUrl){
                                el.attr('target', '_blank');
                            }
                        }
                    } else{
                        resetVender(venderIds);
                    }
                },
                error:function(XMLHttpResponse ){
                	resetVender(venderIds);
                }
            });
        },

        // 数据刷新有两种：
        // 刷局部：1）单个商品；2）满赠；3）满减
        // 单选、增减数量、
        // 刷购物车：1）刷单个商家 2）刷所有商家
        // 全选、选择地址
        updateCartInfo: function(url, param, errorMessage, callback){
            var me = this;

            jQuery.ajax({
                type     : "POST",
                dataType : "json",
                url  : url,
                data : param + "&outSkus=" + this.outSkus + "&random=" + Math.random() + "&locationId=" + this.ipLocation,
                success : function(result) {
            	    me.setLogin(result);
                    result = result.sortedWebCartResult;

                    if(result && result.l == 1){
                        me.goToLogin();
                        return;
                    }

                    if(result && result.redirectUrl && result.redirectUrl != "cart"){
                        window.location.href= result.redirectUrl;
                        return;
                    }

                    if(result && result.success){
                        if(result.addSkuLimitState == "Add_Item_Fail" ){
                            $.closeDialog();
                            me.showAlertDlg('添加商品失败，已超出购物车最大容量！');
                            return;
                        }

                        var modifyResult = result.modifyResult;
                        if(me.isCartEmpty(modifyResult)){
                            return;
                        }
                        me.updateCartGoodsInfo(modifyResult);
                        me.updateTotalInfo(result);
                        
                        // 当购物车为空时，刷新当前页面，让其走getCurrentCartNew.action,重新获取页面
                        if(!$('#cart-list').length){
                        	window.location.reload();
                        }
                        
                        if(callback){
                            callback();
                        }

                        // 库存状态处理
                        me.updateStoreState(result);

                        me.initYanBao();

                        // 扩展属性：礼品购、闪购、国际站等
                        me.loadIconsFromExt(result);

                        me.getVenderInfo(result.venderIds);

                        //me.initRecommendAndCollection(result.ids);
                    }else{
                        $("#cart-loading-dialog").hide();

                        var rem=result.errorMessage;
                        if(rem.indexOf("@_@") != -1){//@_@是后台传值的约定
                            errorMessage = "商品数量不能大于" + rem.split("@_@")[1] + "。";
                        }

                        if(errorMessage){
                            me.showAlertDlg(errorMessage);
                        }
                    }
                },
                error:function(XMLHttpResponse ){
                }
            });
        },

        // 选中或去掉一个商品，
        // 对 select 操作(取消或勾选)
        // 对select操作可以处理为一个。
        // 更新商品
        updateProductInfo: function (url,params,errorMessage,callback){
            var me = this;
            jQuery.ajax({
                type : "POST",
                dataType : "json",
                url : url,
                data: params,
                success : function(result) {
            	    me.setLogin(result);
                    result = result.sortedWebCartResult;
                    if(result && result.l == 1){
                        me.goToLogin();
                        return;
                    }

                    if(result && result.success){
                        if(result.addSkuLimitState == "Add_Item_Fail" ){
                            //info = '添加商品失败，已超出购物车最大容量！';
                            $.closeDialog();
                            me.showAlertDlg('添加商品失败，已超出购物车最大容量！');
                            return;
                        }

                        var modifyResult = result.modifyResult;
                        me.updateProductGoodsInfo(modifyResult);
                        me.updateTotalInfo(result);

                        if(callback){
                            callback(result);
                        }

                        // 库存状态处理
                        me.updateStoreState(result);

                        me.initYanBao();

                        // 扩展属性：礼品购、闪购、国际站等
                        me.loadIconsFromExt(result);

                        //me.initRecommendAndCollection(result.ids);
                    }else{
                        //服务端返回的错误信息
                        $("#cart-loading-dialog").hide();
                        if(errorMessage){
                            me.showAlertDlg(errorMessage);
                        }
                    }
                },
                error:function(XMLHttpResponse){}
            });
        },

        // 更新店铺
        updateVenderInfo: function(url, param, errorMessage, callback, obj, aimnode){
            var outSkus = this.outSkus;
            this.doing = true;

            var me = this;
            jQuery.ajax({
                type : "POST",
                dataType : "json",
                url : url,
                data : param + "&outSkus=" + outSkus + "&random=" + Math.random() + "&locationId=" + this.ipLocation,
                success : function(result) {
                    me.doing = false;
                    me.setLogin(result);
                    result = result.sortedWebCartResult;
                    if(result && result.l == 1){
                        me.goToLogin();
                        return;
                    }

                    if(result && result.success){
                        if(result.addSkuLimitState == "Add_Item_Fail" ){
                            $.closeDialog();
                            me.showAlertDlg('添加商品失败，已超出购物车最大容量！');
                            return;
                        }

                        var modifyResult = result.modifyResult;
                        if(me.isCartEmpty(modifyResult)){
                            return;
                        }
                        me.updateVenderGoodsInfo(modifyResult);
                        me.updateTotalInfo(result);

                        if(callback){
                            callback(result);
                        }

                        // 库存状态处理
                        me.updateStoreState(result);

                        me.initYanBao();

                        // 扩展属性：礼品购、闪购、国际站等
                        me.loadIconsFromExt(result);

                        var info = '';

                        if(modifyResult && modifyResult.poolSkuUnique == 1) {
                            info = '购买一件以上时，不享受本次满减。';
                        }
                        if(modifyResult && modifyResult.maxSkuNumInPool > 0) {
                            info = '购买单个商品超过' + modifyResult.maxSkuNumInPool + '件不享受优惠。';
                        }

                        if(info && $('#' + aimnode).length){
                            info = '<div class="op-tipmsg" style="position:absolute;left:40px;top:100px;z-index:100;display:none;"><span class="s-icon warn-icon"></span>' + info + '</div>';
                            var curnode = $('#' + aimnode).closest('.quantity-form');

                            if(curnode.length){
                                me.showTipInfo(curnode, info, false, true);
                            }
                        }

                        //me.initRecommendAndCollection(result.ids);
                    }else{
                        $("#cart-loading-dialog").hide();

                        var rem=result.errorMessage;
                        if(rem.indexOf("@_@") != -1){//@_@是后台传值的约定
                            errorMessage = "商品数量不能大于" + rem.split("@_@")[1];
                        }
                        if(errorMessage){
                            me.showAlertDlg(errorMessage);
                        }

                        if($(obj).length){
                            $(obj).val($(obj).attr("id").split("_")[3]);
                        }
                    }
                },
                error:function(XMLHttpResponse ){
                }
            });
        },

        // 局部刷新，更新item数据
        // 更新商品信息
        updateProductGoodsInfo: function(modifyResult){
            if(!modifyResult){
                return false;
            }

            var venderid = modifyResult.modifyVenderId;
            var mid = modifyResult.modifyProductId;
            var id = "#product_promo_" + mid;
            if($(id).length <= 0){
                id = "#product_" + mid;
            }
            $(id).prop('outerHTML', modifyResult.modifyHtml);
        },

        // 局部刷新，更新item数据
        // 更新商品信息
        updateVenderGoodsInfo: function(modifyResult){
            if(!modifyResult){
                return false;
            }

            var venderid = modifyResult.modifyVenderId;

            // 店铺为空
            if(modifyResult.venderIsEmpty){
                $('#vender_' + venderid).prop('outerHTML', '');
            } else{
                var venderel = $('#venderId_'+venderid);
                var shopname = venderel.html();
                var shopurl  = venderel.attr("href");

                $('#vender_'   + venderid).prop('outerHTML', modifyResult.modifyHtml);
                $('#venderId_' + venderid).html(shopname);
                $('#venderId_' + venderid).attr("href", shopurl);
            }
        },

        isCartEmpty: function(modifyResult){
            if(!modifyResult){
                return false;
            }

            // 判断店铺是否为空、购物车是否为空
            // 购物车为空
            if(modifyResult.cartIsEmpty){
                window.location.href = this.iurl + '/getCurrentCartNew.action?rd=' + Math.random();
                return true;
            }
        },

        // 局部刷新，更新item数据
        // 更新商品信息
        updateCartGoodsInfo: function(modifyResult){
            if(!modifyResult){
                return false;
            }

            $('#cart-list').prop('innerHTML',modifyResult.modifyHtml);
        },

        // 修改购物车累计信息 (价格、数量、全选状态等)
        updateTotalInfo: function(result){
            if(!result || !result.modifyResult){
                return false;
            }
            var modifyResult = result.modifyResult;

            var totalSkuPrice = modifyResult.totalPromotionPrice || '0.00';
            var finalSkuPrice = modifyResult.finalPrice || '0.00';
            var rePrice = modifyResult.totalRePrice || '0.00';
            var count = modifyResult.selectedCount;

            if(count == 0){
                totalSkuPrice = '0.00';
                finalSkuPrice = '0.00';
            }

            $('#product_' + modifyResult.mid + ' .p-sum strong').html(totalSkuPrice);
            $(".totalRePrice").html("- &#x00A5;" + rePrice);
            $(".amount-sum em").html(count);
            $(".sumPrice em").attr("data-bind", finalSkuPrice)
                            .html("&#x00A5;"+ finalSkuPrice);
            $('.number').html(count);

			this.toggleAll(result.checkIsAll==1)
            $('#checkedCartState').val(modifyResult.checkedCartState);
            this.initJInitButtons();

            //改变全部商品下的下划线
            this.resetUnderline();
        },

        //得到库存
        updateStoreState: function(result) {
            var locationId = this.ipLocation;
            var ids = result&&result.allSkuIds || this.allSkuIds;//$("#allSkuIds").val();
            var outSkus = result&&result.outSkus || this.outSkus;

            // 从结算页返回的数据中查找赠品、换购商品无货的状态。
            if(outSkus){
                var outSkusArr = outSkus.split(',');
                $('.remove-gift').each(function(){
                    var _ids = $(this).attr('id').split('_');
                    var _id = _ids[1];
                    for(var i=0,l=outSkusArr.length; i<l; i++){
                        if(outSkusArr[i] == _id){
                            var itemnode = $(this).closest('.item-item');
                            if(!$('.quantity-txt', itemnode).length){
                                $('.quantity-form', itemnode).after('<div class="ac ftx-03 quantity-txt"><span style="color:#e4393c">无货</span></div>');
                                itemnode.addClass('item-invalid');
                            }
                            break;
                        }
                    }
                });
            }

            if(!locationId || !ids){
                return;
            }

            // 获取其他商品的库存状态
            locationIdArray = locationId.split("-");
            locationId = locationIdArray[0] + "," + locationIdArray[1] + "," + locationIdArray[2] + "," + (locationIdArray[3]||0);
            var skuNum = ids.replace(/,/g,",1;")+",1";//商品数量传1
            var url = PurchaseAppConfig.stockDomain + "/mget?app=cart_pc&ch=1&skuNum="+skuNum+"&area="+locationId+"&r="+Math.random()+"&callback=?";
            $.getJSON(url, function(result) {
            	if(!result) {
                    return;
                }
                var states = result;
                $(".quantity-txt").each(function() {
                    var ss = $(this).attr('_stock');
                    if(!ss){
                        return;
                    }

                    // 父节点
                    var pnode = $(this).parents('.item-item');

                    ss = ss.split("_");
                    var id = ss[ss.length - 1];
                    for (var skuId in states) {
						var state = states[skuId];
                        if (skuId == id) {
                            var info;
                            switch (state.a) {
                                case "33": info = "有货"; break;
                                case "36": info = "预订<a class='tips-i' href='#none' clstag='clickcart|keycount|xincart|cart_skuYuDing' data-tips='商品到货后发货，现在可下单'>&nbsp;</a>"; break;
                                case "39":
                                case "40": info = "<div style='line-height:37px'>有货<font style='color:#ffaa71;line-height:2px;display:block;'>2-6天可发货</font></div>"; break;
                                default: info = "<span style='color:#e4393c'>无货</span>";
                            }
                            //--如果订单返回到购物车,实际商品是无货,则把库存状态强制设置为无货
                            var skus = outSkus.split(',');
                            for(var i=0,len=skus.length; i<len; i++){
                                if(skus[i] == id){
                                    info = "<span style='color:#e4393c'>无货</span>";
                                    // 库存为无货时，置灰背景、不勾选。
                                    pnode.addClass('item-invalid');
                                    // $('input[type=checkbox]', pnode).prop('checked',false)
                                    //                         .attr('disabled', true);
                                    break;
                                }
                            }

                            // 紧急库存显示
                            if(state.c != "-1" && state.c){
                                info = '<span style="color:#e4393c">仅剩' + state.c + '件</span>'
                            }

                            $(this).html(info);
							
                            if(state.a == "36"){
                                $('.tips-i').tips({
                                    type:'hover',
                                    hasArrow:true,
                                    hasClose:false,
                                    align:['top','left'],
                                    autoWindow:true,
                                    callback: function(){
                                        $('.ui-tips-arrow').css('left', '3px');
                                    }
                                });
                            }

                            //无货商品处理: 到货通知
                            if (info == "<span style='color:#e4393c'>无货</span>") {
                                // 暂时不处理，库存状态可能有出入。逻辑保留注释状态。@tongen
                                // pnode.addClass('item-invalid');
                                // $('input[type=checkbox]', pnode).prop('checked',false)
                                //                                 .attr('disabled', true);
                                if(!pnode.find('.cart-notify').length){
                                    var remove = pnode.find(".cart-remove");
                                    if(remove.length){
                                        var rids = remove.attr('id').split('_');
                                        var skuid = rids[2];

                                        var notifyhtml = '<a data-sku="' + skuid + '" data-type="2" href="javascript:void(0);" class="cart-notify" clstag="clickcart|keycount|xincart|cart_daoHuoLogin">到货通知</a>';
                                        remove.after(notifyhtml);
                                    }
                                }
                            }
                            break;
                        }
                    }
                });
            });
        },

        // 礼品购
        loadIconsFromExt: function (result){
            var allSkuIds = result&&result.allSkuIds || this.allSkuIds;
            if (!allSkuIds) {
                return;
            }

            //国际站 写在店铺上；商品不显示。
            try {
                var skuIdsArray = allSkuIds.split(",");
                // 去重。
                var obj = {};
                var skuIdsArray2 = [];
                var skuId = '';

                for(var i=0; i<skuIdsArray.length; i++){
                    skuId = skuIdsArray[i];
                    if(!obj[skuId]){
                        obj[skuId] = true;
                        skuIdsArray2.push(skuId);
                    }
                }

                for(i=0;i<skuIdsArray2.length ;i++){
                    skuId = skuIdsArray2[i];
                    // 京东国际
                    if(skuId>=1950000000 && skuId<=1999999999){
                        $('.p-name a').each(function(){
                            var hid = $(this).attr('href');
                            if(hid.indexOf(skuId) != -1 && $(this).html().indexOf('jdint-icon') == -1){
                                $(this).html('<em class="jdint-icon"></em>' + $(this).html());
                            }
                        });
                    }
                }
            }catch(e){}

            $.ajax({
                url: PurchaseAppConfig.AsyncDomain + "/loadIconsFromExt.action",
                type: "POST",
                dataType: "json",
                data: "allSkuIds=" + allSkuIds + "&rd=" + Math.random(),
                cache: false,
                success: function(data) {
                    var skuMap = data.productExtIconsMap;
                    for(var skuId in skuMap){
                        var tags = skuMap[skuId];
                        // 礼品购
                        if(tags.giftBuy){
                            $("[_giftcard=giftcard_" + skuId + ']').html('<i class="jd-giftcard-icon"></i><a data-tips="选择礼品卡包装，将单独下单结算" clstag="clickcart|keycount|xincart|cart_lipin" class="ftx-03 gift-packing" href="javascript:void(0);">购买礼品包装</a>');
                        }
                    }
                }
            });
        },

        // 延保
        initYanBao: function(){
            var ids = $("#ids").val();
            var len = $("#cart-list input[type=checkbox]:checked").length;
            if(!ids && !len){
                return;
            }

            var me = this;
            // 延保信息的对象
            me.ybobjs = {};

            jQuery.ajax({
                type : "POST",
                dataType : "json",
                url : this.iurl + "/queryProductYb.action?random=" + Math.random(),
                data : null,
                success : function(result) {
                    var ybinfos = result.productYbInfos;
                    if( !ybinfos || !ybinfos.length){
                        return;
                    }

                    // 找到支持延保的节点
                    for(var i=0,len = ybinfos.length; i<len; i++){
                        var ybinfo = ybinfos[i];

                        // 支持延保的节点，并加入延保信息的位置，加入
                        var node = null;
                        var suitId = ybinfo.suitId;
                        var wid = ybinfo.wid;

                        if(ybinfo.suitId){
                            node = $('#product_promo_' + suitId + ' [_yanbao=yanbao_' + wid + '_' + suitId + ']');
                        } else{
                            node = $('#product_' + wid + ' [_yanbao=yanbao_' + wid + '_' + suitId + ']');
                        }

                        if(node){
                            node.html('<i class="jd-service-icon"></i><a data-tips="购买京东服务" class="ftx-03 jd-service" href="#none">购买京东服务</a>');
                        }

                        // 将延保信息存储在一个对象里。
                        me.ybobjs[wid + '_' + suitId] = {};
                        me.ybobjs[wid + '_' + suitId].data = ybinfo;
                    }
                },
                error:function(XMLHttpResponse){
                }
            });
        },

        removeSkuDlg: function(el){
            // 1. 单个、数量变为0；2. 删除套装的子商品；（非虚拟组套能删）3.批量选中删除
            var msg = '您可以选择移到关注，或删除商品。';

            if(!el.hasClass("remove-batch") && el.attr("selectstate") == "1"){
                msg += '操作后，其他商品不享受套装优惠。';
            }
            var bhtml = el.hasClass('remove-batch') ? 'data-batch="true"' : '';

            if(bhtml){
                // 判断是否勾选商品
                if(!$(".item-selected").length){
                    this.showAlertDlg('请至少选中一件商品！');
                    return;
                }
            }
            var html = '<div class="tip-box icon-box">'
                        + '<span class="warn-icon m-icon"></span>'
                        + '<div class="item-fore">'
                        + '<h3 class="ftx-04">删除商品？</h3><div class="ftx-03">' + msg + '</div>'
                        + '</div>'
                        + '<div class="op-btns ac">'
                        + '<a href="#none" class="btn-9 select-remove" selectstate="' + (el.attr("selectstate") || '')
                        + '" data-show="" data-bind="' + (el.attr("id") || '')
                        + '" data-name="' + (el.attr("data-name") || '')
                        + '" data-more="' + (el.attr("data-more") || '')
                        + '" ' + bhtml + '>删除</a>'
                        + '<a href="#none" class="btn-1 ml10 re-select-follow" selectstate="' + (el.attr("selectstate") || '') + '" data-bind="' + (el.attr("id") || '')
                        + '" ' + bhtml + ' clstag="clickcart|keycount|xincart|cart_sku_del_gz">移到我的关注</a>'
                        + '</div>'
                        + '</div>';

            $('body').dialog({
                title: '删除',
                width: 400,
                height: 100,
                type: 'html',
                source: html
            });
        },

        // 删除商品
        remove: function(el, bbatch){
            //批量删除
            if(bbatch){
                this.removeBatch();
            } else{
                this.removeProduct(el);
            }
        },

        removeBatch: function(){
            var me = this;
            var html = '';

            var selected = $(".item-selected");
            if(selected && selected.length){//如果有选中商品
                $('#cart-list .item-selected').each(function(){
                    var removeitem = $('.cart-remove', $(this));
                    if(removeitem.length){
                        var curid = removeitem.attr('id');
                        var type = curid.split("_")[3];
                        //单品，满返套装中的单品，满赠套装中的单品：删除商品后可以重新购买
                        if(type == SkuItemType.Sku || type == SkuItemType.SkuOfManFanPacks || type == SkuItemType.SkuOfManZengPacks){
                            var id = curid.split("_")[2];
                            var name = removeitem.attr("data-name");
                            var ss = removeitem.attr("data-more").split("_");
                            var price = ss[1];
                            var num = ss[2];

                            html += me.rebuyHtml(id, name, price, num, curid);
                        }
                    }
                });

                this.updateCartInfo(this.iurl + '/batchRemoveSkusFromCart.action',
                    null,
                    '批量删除商品失败',
                    function(){
                        if(html){
                            $(".cart-removed").append(html);
                            $(".cart-removed").show();
                        }
                    }
                );
            }else{
                this.showAlertDlg('请至少选中一件商品！');
            }
        },

        removeProduct: function(el){
            var curid = el.attr('data-bind');
            var ss = curid.split("_");
            var venderid = ss[1];
            var id = ss[2];
            var type = ss[3];
            var targetId = 0;
            var packId = 0;
            if(ss.length==5 || ss.length==6){
                targetId = ss[4];
                if(ss.length==6){
                    packId = ss[5];
                }
            }
            var params = "venderId=" + venderid + "&pid=" + id
                        + "&ptype=" + type
                        + "&packId=" + packId
                        + "&targetId=" + targetId;
            //单品，满返套装中的单品，满赠套装中的单品：删除商品后可以重新购买
            if(type == SkuItemType.Sku || type == SkuItemType.SkuOfManFanPacks || type == SkuItemType.SkuOfManZengPacks){
            	//有重复的先删掉
                $("#removedShow-"+id).remove();
                var name = el.attr("data-name");
                ss = el.attr("data-more").split("_");

                var price = ss[1];
                var num = ss[2];
                var html = this.rebuyHtml(id, name, price, num, curid);

                this.removeSku(params, id, function(){
                    if(html){
                        $(".cart-removed").append(html);
                        $(".cart-removed").show();
                    }
                });
            } else{
                this.removeSku(params, id, null, el.attr("selectstate"));
            }
        },

        rebuyHtml: function(id, name, price, num, curid){
            return '<div class="r-item" id="removedShow-' + id + '">'
                    + '<div class="r-name"><a href="http://item.jd.com/' + id + '.html">'+ name +'</a></div>'
                    + '<div class="r-price"><strong>&#x00A5;' + price + '</strong></div>'
                    + '<div class="r-quantity">' + num + '</div>'
                    + '<div class="r-ops"><a class="mr10 re-buy" _id="' + id + '_' + num + '" href="javascript:void(0);" clstag="clickcart|keycount|xincart|reAddedSku">重新购买</a><a class="cart-follow" href="javascript:void(0);" clstag="clickcart|keycount|xincart|SaveFavorite" id="' + curid + '" data-more="bremoved">移到我的关注</a></div></div>';
        },

        //删除商品
        removeSku: function(params, pid, callback, selectstate){
            var actionUrl='';
            if(this.checkSku(pid)){
                actionUrl = this.iurl + "/removeSkuFromCart.action?rd=" + Math.random();
                if(2 == selectstate){
                    actionUrl += "&selectState_=2";
                }

                this.updateVenderInfo(actionUrl, params, "删除商品失败", callback);

                // 删除商品日志
                try{
                    var __jda = readCookie("__jda");
                    var uuid = __jda ? __jda.split(".")[1] : false;
                    log('item','010002', uuid, $.jCookie("pin"), pid, 'del');
                }catch(err){
                }
            } else{
                this.showAlertDlg('对不起，您删除的商品不存在！');
            }
        },

        followSkuDlg: function(el){
            // 1. 单个、数量变为0；2. 删除套装的子商品；（非虚拟组套能删）3.批量选中删除
            var msg = '移动后选中商品将不在购物车中显示。';

            if(!el.hasClass("follow-batch") && el.attr("selectstate") == "1"){
                msg += '操作后，其他商品不享受套装优惠。';
            }

            var bhtml = el.hasClass('follow-batch') ? 'data-batch="true"' : '';
            if(bhtml){
                // 判断是否勾选商品
                if(!$(".item-selected").length){
                    this.showAlertDlg('请至少选中一件商品！');
                    return;
                }
            }
            var html = '<div class="tip-box icon-box">'
                        + '<span class="warn-icon m-icon"></span>'
                        + '<div class="item-fore">'
                        + '<h3 class="ftx-04">移到关注</h3><div class="ftx-03">' + msg + '</div>'
                        + '</div>'
                        + '<div class="op-btns ac">'
                        + '<a href="#none" class="btn-1 select-follow" selectstate="' + (el.attr("selectstate") || '')
                        + '" data-show="" data-bind="' + (el.attr("id") || '')
                        + '" data-name="' + (el.attr("data-name") || '')
                        + '" data-more="' + (el.attr("data-more") || '')
                        + '" ' + bhtml + '>确定</a>'
                        + '<a href="#none" class="btn-9 ml10 cancel-follow">取消</a>'
                        + '</div>'
                        + '</div>';

            $('body').dialog({
                title: '关注',
                width: 400,
                height: 100,
                type: 'html',
                source: html
            });
        },

        follow: function(pids, bbatch, bremoved, selectstate){
            //批量移到我的关注
            if(bbatch){
                this.followBatch();
            } else {
                this.followProduct(pids, bremoved, selectstate);
            }
        },

        followBatch: function(){
            var selected = $(".item-selected");
            if(selected && selected.length){//如果有选中商品
                var html = '';
                var pids = '';
                $('#cart-list .item-selected').each(function(){
                    var followitem = $('.cart-follow', $(this));
                    if(followitem.length){
                        var id = followitem.attr("id").split("_")[2];
                        pids += id + ',';
                    }
                });
                pids = pids.substring(0, pids.length-1);
                this.followDo(pids);
            }else{
                this.showAlertDlg('请至少选中一件商品！');
            }
        },

        followProduct:function(pids, bremoved, selectstate){
            // 移到关注；从列表中删除item
            var ss = pids.split("_");
            var venderid = ss[1];
            var id = ss[2];
            var type = ss[3];
            var targetId = 0;
            var packId = 0;
            if(ss.length==5){
                targetId = ss[4];
            }else if(ss.length==6){
                targetId = ss[4];
                packId = ss[5];
            }

            var objel = $('#' + pids);
            var params = "venderId=" + venderid + "&pid=" + id
                        + "&ptype=" + type
                        + "&packId=" + packId
                        + "&targetId=" + targetId;
            this.followDo(id, objel, bremoved, params, selectstate);
        },

        followDo: function(id, objel, bremoved, params, selectstate){
            var me = this;
            if ($("#isLogin").val() == 0) {
                var cartLoginUrl = PurchaseAppConfig.Domain + "/cart.html?rd="+Math.random();
                login({
                    modal: true,//false跳转,true显示登录注册弹层
                    returnUrl: cartLoginUrl,
                    'clstag1': "login|keycount|5|5",
                    'clstag2': "login|keycount|5|6",
                    complete: function() {
                        me.setLogin();
                        me.followSku(id, objel, bremoved, params, selectstate);
                    }
                });
            } else {
                me.followSku(id, objel, bremoved, params, selectstate);
            }
        },

        followSku: function(pids, objel, bremoved, params, selectstate){
            var me = this;
            if(!pids){
                return;
            }
            $.getJSON("http://follow.soa.jd.com/product/batchfollow?productIds=" + pids + "&random=" + Math.random() + "&callback=?", function(result) {
                    if(result.success){
                        var info = '<div class="op-tipmsg follow-tip" style="position:absolute;width:140px;display:none;color:#71B247;z-index:100;"><span class="s-icon succ-icon"></span>成功移到我的关注！</div>';
                        me.showTipInfo(objel, info, false);

                        if(objel){
                            // 单品、右侧操作栏
                            if(bremoved){
                                objel.parents('.r-item').remove();
                                if(!$(".cart-removed .r-item").length){
                                    $(".cart-removed").hide();
                                }
                            } else{
                                me.removeSku(params, pids, null, selectstate);
                            }
                        } else{
                            // 批量删除、底部操作
                            me.updateCartInfo(me.iurl + '/batchRemoveSkusFromCart.action',
                                null,
                                '批量删除商品失败',
                                function(){
                                    console.log('删除成功');
                                }
                            );
                        }
                    }
                }
            );
        },

        initRebuyEvent: function(){
            var me = this;

            $('.cart-warp').delegate('.re-buy', 'click', function(){
                var _this = $(this);
                var ids = _this.attr('_id').split('_');

                //调用购买接口；
                var pid = ids[0];
                var ptype = 1;
                var num = ids[1];
                var packId = 0;
                var targetId = 0;

                me.addSkuToCartReBuy(pid, ptype, num, packId, targetId, function(){
                    //删掉已重新购买的数据
                    _this.parents('.r-item').remove();
                    if(!$(".cart-removed .r-item").length){
                        $(".cart-removed").hide();
                    }
                });
            });
        },

        addSkuToCartReBuy: function(pid,ptype,num,packId,targetId,callback){
            if(this.checkSku(pid)){
                var params = "pid=" + pid
                            + "&ptype=" + ptype
                            + "&pcount=" + num
                            + "&packId=" + packId
                            + "&targetId=" + targetId;
                this.updateCartInfo(this.iurl+'/addSkuToCartAsync.action',
                    params,
                    '添加商品失败',
                    function(){
                        if(callback){
                            callback();
                        }
                    }
                );
            } else{
                this.showAlertDlg('对不起，您添加的商品不存在！');
            }
        },

        initGiftEvent: function(){
            var me = this;

            // 初始化 领取赠品、更换商品
            $('.cart-warp').delegate('a.trade-btn', 'click', function(event){
                me.clearDialog();

                var pnode = $(this).parents('.item-full');
                var giftEl = $('.gift-box', pnode);

                if(me.isLowIE()){
                    $(this).closest('.item-header').parent().css('z-index', 2);
                }

                // todo @tongen IE7
                giftEl.css('top', this.offsetTop + 'px');
                giftEl.css('left', (this.offsetLeft + this.offsetWidth + 10) + 'px');
                giftEl.show();

                var arrawEle = giftEl.find('.gift-arr');

                toFixUpLoc($(this), giftEl, arrawEle);
                toFixDownLoc(giftEl, arrawEle);

                // 已勾选商品数
                var selnum = $('.gift-goods input:checked', giftEl).length;
                $('.gift-mt .num', giftEl).html(selnum);

                if($(this).html().indexOf('换购') != -1){
                    log('newcart', 'clickcart','huangouclick');
                } else {
                    log('newcart', 'clickcart','zengpinclick');
                }

                event.stopPropagation();
                return false;
            });

            $('.cart-warp').delegate('.gift-box input[type=checkbox]', 'click', function(){
                var pnode = $(this).parents('.gift-box');
                var promotionId = pnode.attr('id');
                promotionId = promotionId.substring(promotionId.lastIndexOf('_')+1);

                // 最多可以换购、领取的赠品数
                var maxnum = $('#gift_num_' + promotionId).val();
                var selnum = $('.gift-goods input:checked', pnode).length;

                if(selnum > maxnum){
                    $(this).prop('checked', false);
                    selnum = maxnum;
                    // 弹出小浮层，2秒后消失
                    var info = '<div class="op-tipmsg" style="position:absolute;left:40px;top:100px;z-index:100;display:none;"><span class="s-icon warn-icon"></span>最多可选择' + maxnum + '件~</div>';
                    me.showTipInfo($(this), info, true);
                }

                $('.gift-mt .num', pnode).html(selnum);
            });

            $('.cart-warp').delegate('a.select-gift', 'click', function(){
                var pnode = $(this).parents('.gift-box');
                var promotionId = pnode.attr('id');
                promotionId = promotionId.substring(promotionId.lastIndexOf('_')+1);

                // 最多可以换购、领取的赠品数
                var maxnum = $('#gift_num_' + promotionId).val();
                var selnum = $('.gift-goods input:checked', pnode).length;
                if(selnum > maxnum){
                    return;
                }

                var checks = $('.gift-goods input', pnode);
                var giftsid = '';
                for(var i=0,l=checks.length; i<l; i++){
                    var el = checks[i];
                    if($(el).prop('checked')){
                        giftsid += $(el).attr('id') + ',';
                    }
                }
                giftsid = giftsid.substring(0, giftsid.length-1);

                me.updateGifts(giftsid, promotionId);
                me.closeTipInfo();
            });

            $('.cart-warp').delegate('a.cancel-gift', 'click', function(){
                $(this).closest('.gift-box').hide();
                me.closeTipInfo();

                if(me.isLowIE()){
                    $(this).closest('.gift-box').parent().css('z-index', 'auto');
                }
            });

            $('.cart-warp').delegate('.gift-box .close', 'click', function(){
                $(this).parents('.gift-box').hide();
                me.closeTipInfo();

                if(me.isLowIE()){
                    $(this).closest('.gift-box').parent().css('z-index', 'auto');
                }
            });
        },

        updateGifts: function(giftsid, promotionId){
            var me = this;
            var act = giftsid ? '/addGiftsOfMZ.action' : '/clearGiftsOfMZ.action';
            var params = "pid=" + giftsid
                        + "&promoID=" + promotionId
                        + "&random=" + Math.random();

            this.updateProductInfo(this.iurl + act,
                                    params,
                                    '添加赠品失败',
                                    function(){
                                        $('#gift-box_' + promotionId).hide();
                                        if(me.isLowIE()){
                                            $('#gift-box_' + promotionId).parent().css('z-index', 'auto');
                                        }
                                    }
                                );
        },

        removeGift: function(rid){
            var ss = rid.split('_');
            var pid = ss[1];
            var ptype = ss[2];
            var promoId = ss[3];

            var params = 'pid=' + pid
                        + '&ptype=' + ptype
                        + '&promoID=' + promoId
                        + "&random=" + Math.random();

            this.updateProductInfo(this.iurl + '/removeGiftOfMZ.action',
                                params,
                                '删除赠品失败'
                                );
        },

        // 单品自带赠品，无货时删除
        removeSelfgift: function(id){
            var ss = id.split("_");
            var pid = ss[0];
            var targetId = ss[1];
            var ptype = ss[2];
            var promoID = ss[3];

            if(this.checkSku(targetId)){
                var params = "pid=" + pid
                            + "&targetId=" + targetId
                            + "&ptype=" + ptype
                            + "&promoID=" + promoID
                            + "&outSkus=" + this.outSkus
                            + "&rd=" + Math.random();

                this.updateProductInfo(this.iurl + '/removeGiftFromCart.action',
                    params,
                    '删除赠品失败'
                    );
            } else{
                this.showAlertDlg('对不起，您删除的赠品不存在！');
            }
        },

        // 单品自带赠品，有货时还原
        resumeSelfgift: function(id){
            var ss = id.split("_");
            var pid = ss[0];
            var ptype = ss[1];
            var promoID = ss[2];

            if(this.checkSku(pid)){
                var params = "pid=" + pid
                            + "&ptype=" + ptype
                            + "&promoID=" + promoID
                            + "&outSkus=" + this.outSkus
                            + "&rd=" + Math.random();

                this.updateProductInfo(this.iurl + '/backGiftFromCart.action',
                    params,
                    '恢复赠品失败'
                    );
            } else{
                this.showAlertDlg('对不起，主商品不存在！');
            }
        },

        initGiftcardEvent: function(){
            $('.cart-warp').delegate('a.gift-packing', 'click', function(){

                var pid = $(this).closest('.promise').attr('_giftcard').split('_')[1];

                var html = '<div class="tip-box icon-box giftcard-box">'
                        + '<span class="qm-icon m-icon"></span>'
                        + '<div class="item-fore">'
                        + '<h3 class="ftx-04">该商品支持购买礼品包装和贺卡服务，继续？</h3>'
                        + '<div class="mt5">可选择精美包装、填写贺卡内容、上传温馨视频，作为礼品赠送家人、朋友、恋人、合作伙伴。（港澳台、偏远地区除外）</div>'
                        + '<div class="ftx-03 mt10">选择包装 > 填写贺卡内容 > 上传温馨视频 > OK，下单支付! > TA收到惊喜</div>'
                        + '</div>'
                        + '<div class="op-btns ac"><a href="javascript:void(0);" clstag="clickcart|keycount|xincart|cart_lipinset" class="btn-1 select-giftcard" _pid="' + pid + '">去购买</a><a href="#none" class="btn-9 ml10 del cancel-giftcard">放弃</a></div>'
                        + '</div>';

                var dlg = $('body').dialog({
                    title: '购买礼品包装',
                    width: 450,
                    height: 150,
                    type: 'html',
                    source:html
                });
            });

            $('body').delegate('a.select-giftcard', 'click', function(){
                window.open('http://cart.gift.jd.com/cart/addGiftToCart.action?pid=' + $(this).attr('_pid') + '&pcount=1&ptype=1', '_blank');
                $.closeDialog();
            });

            $('body').delegate('a.cancel-giftcard', 'click', function(){
                $.closeDialog();
            });
        },

        bindCheckEvent: function(){
            // 回调后复选框是可选的。
            $("input[type=checkbox]").attr("disabled",false)
                                     .removeAttr("disabled");

            $("input[checked=checked]").attr({"checked":"checked"});

            this.toggleSelect();
            this.toggleShopSelect();
            this.toggleSingleSelect();
        },

        // 是否全选
        isAllCheck: function(cart){
            if(!cart) return false;
            cart = cart.replace(/\\/g,"");
            cart = cart.replace(/\"/g,"");
            cart = cart.substring(1,cart.length-1);
            if(cart.indexOf("&ct&:0") == -1){
                return true;
            }else{
                return false;
            }
        },

        // type:单品1、店铺2
        toggleCheckbox: function(type, cb, el){
            // 回调后复选框是可选的。
            $("input[type=checkbox]").attr("disabled",false)
                                     .removeAttr("disabled");

            var flag = true;
            var shopEl = null;

            if(!cb){
                flag = false;
            }

            if(type == 1){
                // 遍历店铺子节点：
                var shopList = null;
                var shopEl = null;
                var shopEls = $('input[name=checkShop]', $('#cart-list'));
                for(var i=0, l=shopEls.length; i<l; i++){
                    var els = $('input[name=checkItem]', $(shopEls[i]).parents('.shop').next());
                    for(var j=0,k=els.length; j<k; j++){
                        if($(els[j]).attr('value') == el.attr('value')){
                            shopList = $(shopEls[i]).parents('.shop').next();
                            shopEl = shopEls[i];
                            break;
                        }
                    }
                }

                var cels = $('input[name=checkItem]', shopList);
                for(var i=0, l=cels.length; i<l; i++){
                    if(!$(cels[i]).prop('checked')){
                        flag = false;
                        break;
                    }
                }
                this.toggleSingle(type, flag, shopList);
                this.toggleShop(type, flag, shopEl);
            } else if(type == 2){
                shopEl = el;
                shopList = el.parents('.shop').next();
                this.toggleSingle(type, flag, shopList);
                this.toggleShop(type, flag, shopEl);
            }
        },

        // 全选
        toggleAll: function(flag){
            $("input[name=toggle-checkboxes]").each(function(){
                $(this).prop("checked",flag);
            });
        },

        // 对指定的店铺勾选；否则所有店铺
        toggleShop: function(type, flag, el){
            if(el){
                $(el).prop('checked', flag);
            } else{
                $('input[name=checkShop]').each(function(){
                    $(this).prop('checked', flag);
                });
            }
        },

        toggleSingle: function(type, flag, el){
            if(type == 1){
            } else if(type == 2){
                $('input[name=checkItem]', el).each(function(){
                    $(this).prop('checked', flag);
                });
            } else if(type == 3){
                $('input[name=checkItem]').each(function(){
                    $(this).prop('checked', flag);
                });
            }
        },

        // 选择后需要重新计算价格，页面刷新。
        //全选
        toggleSelect: function(){
            var me = this;
            $('.cart-warp').delegate('input[name=toggle-checkboxes]', 'click', function(){
                //点击复选框后所有复选框不可选
                $("input[type=checkbox]").attr("disabled",true);

                var selected = $(this).prop("checked");

                var act = selected ? 'selectAllItem' : 'cancelAllItem';
                var tip = selected ? '全部勾选商品失败' : '全部取消商品失败';

                me.updateCartInfo(me.iurl + "/" + act + ".action" , '' , tip , function(){
                	$("input[name=toggle-checkboxes]").attr("disabled",false).removeAttr("disabled");
                } );
            });
        },

        //勾选店铺
        toggleShopSelect: function(){
            var me = this;
            $('.cart-warp').delegate('input[name=checkShop]', 'click', function(){
                //点击复选框后所有复选框不可选
                $("input[type=checkbox]").attr("disabled",true);

                var mEl = $(this);
                var venderid = mEl.parents('.cart-tbody').attr('id');
                venderid = venderid.substring(venderid.lastIndexOf('_')+1);

                // 店铺下所有主商品
                var pids = '';
                var items = $('input[type=checkbox]', $('#vender_' + venderid));

                for(var i=0,l=items.length; i<l; i++){
                    var pid = $(items[i]).attr('p-type');
                    if(pid){
                        pids += pid + ',';
                    }
                }

                pids = pids.substring(0, pids.length-1);

                var selected = mEl.prop("checked");
                var act = selected ? 'selectVenderItem' : 'cancelVenderItem';
                var tip = selected ? '勾选店铺商品失败' : '取消店铺商品失败';

                me.updateVenderInfo(me.iurl + '/' + act + '.action',
                    'venderId=' + venderid + '&p_types= ' + pids,
                    tip,
                    function(){
                        var el = $('#vender_' + venderid + ' input[name=checkShop]');
                        el.prop("checked", true);
                        me.toggleCheckbox(2, selected, el);
                    }
                );
            });
        },

        //单选：对店铺和全选都有影响。
        toggleSingleSelect: function(){
            var me = this;
            $('.cart-warp').delegate('input[name=checkItem]', 'click', function(){

                //点击复选框后所有复选框不可选
                $("input[type=checkbox]").attr("disabled", true);

                var mEl = $(this);

                var arr = mEl.val().split("_");
                var pid = arr[0];
                var ptype = arr[1];
                var targetId = 0;

                if(arr.length == 3){
                    targetId = arr[2];
                }

                if(me.checkSku(pid)){

                    //是否勾选商品
                    var cb = mEl.prop("checked");
                    var act = cb ? 'selectItem' : 'cancelItem';
                    var tip = cb ? '勾选商品失败，请刷新页面重试。' : '取消商品失败，请刷新页面重试。';

                    var outSkus = me.outSkus;
                    var params = "&outSkus=" + outSkus
                                + "&pid=" + escape(pid)
                                + "&ptype=" + escape(ptype)
                                + "&packId=0"
                                + "&targetId=" + escape(targetId)
                                + "&promoID=" + escape(targetId)
                                + "&locationId=" + me.ipLocation;

                    me.updateProductInfo(me.iurl + "/" + act + ".action?rd" + Math.random(),
                        params,
                        tip,
                        function(result){
                            $("input[type=checkbox]").attr("disabled", false).removeAttr("disabled");
                            me.toggleCheckbox(1, cb, mEl);
                        }
                    );
                }else{
                    me.showAlertDlg('对不起，您选择的商品不存在！');
                }
            });
        },

        initJdServices: function(){
            //选择京东服务
            var me = this;
            $('.cart-warp').delegate('.promise .jd-service', 'click', function(event){
                me.clearDialog();

                var pnode = $('.service-tips', $(this).parents('.p-extend'));
                var mnode = $(this).parents('.promise');

                if(!pnode || pnode && !pnode.length){
                    var pids = mnode.attr('_yanbao').split('_');
                    var data = me.ybobjs[pids[1] + '_' + pids[2]].data;
                    var selectedArr = [];

                    var ybhtml = '<div class="service-tips" style="display: block;">'
                                + '<div class="jd-service-tit">购买京东服务<b></b></div>'
                                + '<div class="sku-edit-cont"><dl class="jd-service"><dt>京东服务：</dt><dd>';

                    var ghtml = '';
                    for(var i=0,tgroups=data.platformGroups,tlen=tgroups.length; i<tlen; i++){
                        var curgroup = tgroups[i];

                        ghtml += '<div class="yb-item-cat yb-item-cat-r' + curgroup.groupId + '"><span class="yb-item-more">更多</span>';

                        var curgroups = curgroup.platformConfigVOs;
                        var curitem = null;
                        var bsel = false;

                        // 如果有选中节点，则用选中节点做头。
                        for(var j=0,glen=curgroups.length; j<glen; j++){
                            if(curgroups[j].selected){
                                bsel = true;
                                selectedArr.push(curgroups[j].platformId);
                                curitem = curgroups[j];
                                break;
                            }
                        }

                        // 如果没有选中节点，则需要第一个做头。
                        curitem = curitem || curgroups[0];
                        var selhtml = '<div class="item yb-item-tit '
                                    + (bsel ? "selected" : "") + '"><b></b>'
                                    + '<a target="_blank" href="http://fuwu.jd.com" class="yb-link"><i class="yb-ico"></i></a><a class="yb-item" href="#none" _id="' + curitem.skuId + '_' + curitem.platformId + '_' + curitem.rSuitId + '">'+ curitem.platformName + '&#x00A5;' + curitem.price + '</a>'
                                    + '</div>';

                        var unselhtml = '<div class="yb-item-list">';
                        for(var j=0,glen=curgroups.length; j<glen; j++){
                            var curitem = curgroups[j];
                            if(curitem.selected || (!bsel && j==0)){
                                continue;
                            } else{
                                unselhtml += '<div class="item"><b></b>'
                                        + '<a target="_blank" href="http://fuwu.jd.com" class="yb-link"><i class="yb-ico"></i></a>'
                                        + '<a class="yb-item" href="#none" _id="'  + curitem.skuId + '_' + curitem.platformId + '_' + curitem.rSuitId + '">'+ curitem.platformName + '￥' + curitem.price + '</a></div>';
                            }
                        }
                        ghtml += selhtml + unselhtml + '</div></div>';
                    }
                    ybhtml += ghtml + '</dd></dl>'
                            + '<div class="jd-service-tips"><span class="tips-tit">已选：</span>'
                            + '<span class="tips-cont"></span><span class="clr"></span></div>'
                            + '<div class="op-btns ac mt10"><a class="btn-1 select-service" href="#none">确定</a><a class="btn-9 ml10 del cancel-service" href="#none">取消</a></div</div></div>';
                    mnode.after(ybhtml);
                    pnode = $('.service-tips', $(this).parents('.p-extend'));

                    me.ybobjs[pids[1] + '_' + pids[2]].selectedData = selectedArr;
                }

                me.setServicesTips(pnode);

                if(me.isLowIE()){
                    $(this).closest('.item-item').parent().css('z-index', 2);
                    $(this).closest('.item-item').css('z-index', 20);
                }

                // 受父节点position影响。
                var mnodeoffset = $(this).closest('.p-extend').position();
                pnode.css('top', (mnodeoffset.top + 40) + 'px');
                // 113:图片80+1+1+10 + 图片17+4
                pnode.css('left', (mnodeoffset.left + 113) + 'px');
                pnode.slideDown(200);

                log('newcart', 'clickcart','fuwuclick');

                event.stopPropagation();
                return false;
            });

            $('.cart-warp').delegate('.service-tips', 'click', function(event){
                $('.yb-item-cat', $(this)).each(function(){
                    $(this).removeClass('yb-item-hover');
                });
            });

            $('.cart-warp').delegate('.yb-item-tit .yb-item', 'click', function(event){
                // 将该浮层上的select都收起
                var ddel = $(this).closest('dd');
                $('.yb-item-cat', ddel).each(function(){
                    $(this).removeClass('yb-item-hover');
                });

                // 展开浮层
                $(this).closest('.yb-item-cat').addClass('yb-item-hover');

                // 选中当前节点
                $(this).closest('.yb-item-tit').toggleClass('selected');

                me.setServicesTips(ddel.closest('.service-tips'));

                event.stopPropagation();
                return false;
            });

            $('.cart-warp').delegate('.yb-item-list .yb-item', 'click', function(event){
                // 选中的节点
                var curnode = $(this).parents('.item');
                var newhtml = curnode.html();

                // title节点
                var pnode = $(this).parents('.yb-item-cat');
                var tnode = $('.yb-item-tit', pnode);

                var html = tnode.html();

                curnode.html(html);
                tnode.html(newhtml);
                tnode.addClass('selected');
                pnode.removeClass('yb-item-hover');

                me.setServicesTips(pnode.closest('.service-tips'));

                event.stopPropagation();
                return false;
            });

            $('.cart-warp').delegate('.cancel-service', 'click', function(event){
                $(this).parents('.service-tips').hide();
                if(me.isLowIE()){
                    $(this).closest('.item-item').parent().css('z-index', 'auto');
                    $(this).closest('.item-item').css('z-index', 'auto');
                }

                //event.stopPropagation();
                //return false;
            });

            $('.cart-warp').delegate('.select-service', 'click', function(event){
                var _this = $(this);
                // 选中的服务
                var pnode = $(this).closest('.service-tips');

                var mnode = pnode.prev();
                var pids = mnode.attr('_yanbao').split('_');
                var selectedData = me.ybobjs[pids[1] + '_' + pids[2]].selectedData;

                var groups = $('.yb-item-tit', pnode);
                var suitId = '';
                var platformIds = '';
                var skuId = '';
                for(var i=0,len=groups.length; i<len; i++){
                    var curgroup = groups[i];
                    var ids = $('.yb-item', $(curgroup)).attr('_id').split('_');
                    suitId = ids[2];
                    skuId = ids[0];
                    if($(curgroup).hasClass('selected')){
                        platformIds += ids[1] + ',';
                    }
                }

                platformIds = platformIds.substring(0, platformIds.length-1);

                if(selectedData == platformIds){
                    pnode.hide();
                    if(me.isLowIE()){
                        pnode.closest('.item-item').parent().css('z-index', 'auto');
                        pnode.closest('.item-item').css('z-index', 'auto');
                    }

                    return;
                }

                // 类型 表示单品和赠品的
                var ptype = suitId ? SkuItemType.YbOfPacks : SkuItemType.YbOfSkusOrGifts;
                var ybNums = 1;

                var params = '';
                var act = '';

                if(platformIds){
                    // 添加延保
                    //curitem.platformId + '_' + curitem.rSuitId + '_' + curitem.skuId
                    params = 'packId=' + suitId
                        + '&pid=' + platformIds
                        + '&ptype=' + ptype
                        + '&targetId=' + skuId
                        + '&ybNums=' + ybNums;
                    act = "/addYbSkusToCart.action";
                    log('newcart', 'clickcart', 'cart_fuwujia');
                } else{
                    params = 'packId=' + suitId
                        + '&ptype=' + ptype
                        + '&targetId=' + skuId;
                    act = "/clearSkuYbsToCart.action";
                    log('newcart', 'clickcart', 'cart_fuwujian');
                }

                me.updateCartInfo(me.iurl + act,
                    params,
                    '',
                    function(result){
                        pnode.hide();
                        if(me.isLowIE()){
                            pnode.closest('.item-item').parent().css('z-index', 'auto');
                            pnode.closest('.item-item').css('z-index', 'auto');
                        }

                        me.ybobjs[pids[1] + '_' + pids[2]].selectedData = platformIds;
                    }
                );

                //event.stopPropagation();
                //return false;
            });
        },

        setServicesTips: function(ppnode){
            var selnodes = $('.selected', ppnode);

            var html = '';
            for(var i=0,len=selnodes.length; i<len; i++){
                html += $('.yb-item', selnodes[i]).text() + '，';
            }
            html = html.substring(0, html.length-1);

            $('.tips-cont', ppnode).html(html);

            if(html){
                $('.jd-service-tips', ppnode).show();
            } else{
                $('.jd-service-tips', ppnode).hide();
            }
        },

        //促销优惠
        initPromotionEvent: function(){
            var me = this;
            $('.cart-warp').delegate('a.sales-promotion', 'click', function(event){
                me.clearDialog();

                // 受父节点的position 影响。tongen
                var offset = $(this).position();
                $(this).next().css('left', (offset.left) + 'px');
                $(this).next().css('top', (offset.top + 20) + 'px');
                $(this).next().slideDown(200);

                if(me.isLowIE()){
                    $(this).closest('.item-item').parent().css('z-index', 10);
                    $(this).closest('.item-item').css('z-index', 20);
                }

                log('newcart', 'clickcart','youhuiclick');

                event.stopPropagation();
                return false;
            });

            // 促销浮层里的操作：打开select
            $('.cart-warp').delegate('.promotion-more', 'click', function(event){
                var bshow = $(this).hasClass('hover');
                if(!bshow){
                    $(this).addClass('hover').siblings().removeClass('hover');
                    $('.cont', $(this)).show();
                } else{
                    $(this).removeClass('hover').siblings().removeClass('hover');
                    $('.cont', $(this)).hide();
                }

                event.stopPropagation();
                return false;
            });

            // 促销浮层内部操作:选择某li
            $('.cart-warp').delegate('.promotion-more li', 'click', function(event){
                var pnode = $(this).parents('.promotion-more');
                $('.tit', pnode).html($(this).html() + '<b></b>');
                pnode.removeClass('hover');
                $('.cont', pnode).hide();

                event.stopPropagation();
                return false;
            });

            // 促销浮层内部操作：确定
            $('.cart-warp').delegate('.select-promotion', 'click', function(event){

                var el = $(this);
                var pnode = el.closest('.promotion-tips');
                var data = $('.tit a', pnode).attr('_promotionid');
                var datas = data.split("_");
                var venderid = datas[0];
                var modifyid = datas[1];
                var pid = datas[2];
                var ptype = datas[3];
                var curPromoId = datas[4];
                if(!curPromoId){
                    curPromoId = 0;
                }
                var targetId = curPromoId;
                // 不使用优惠的时候~~
                if(targetId == modifyid || curPromoId==0 && modifyid<0) {
                    pnode.hide();
                    if(me.isLowIE()){
                        pnode.closest('.item-item').parent().css('z-index', 'auto');
                        pnode.closest('.item-item').css('z-index', 'auto');
                    }
                    event.stopPropagation();
                    return false;
                }

                var params = "venderId=" +venderid
                            + "&pid=" + pid
                            + "&ptype=" + ptype
                            + "&promoID=" + curPromoId
                            + "&targetId=" + targetId
                            + (modifyid ? ("&modifyPromoID=" + modifyid) : '');

                me.updateVenderInfo(me.iurl + '/changePromotion.action',
                                    params,
                                    '', function(){
                                        pnode.hide();
                                        if(me.isLowIE()){
                                            pnode.closest('.item-item').parent().css('z-index', 'auto');
                                            pnode.closest('.item-item').css('z-index', 'auto');
                                        }
                                    });

                log('newcart', 'clickcart','youhuiokclick');

                // event.stopPropagation();
                // return false;
            });

            // 促销浮层内部操作：取消
            $('.cart-warp').delegate('.cancel-promotion', 'click', function(event){
                $(this).closest('.promotion-tips').slideUp(200);
                if(me.isLowIE()){
                    $(this).closest('.item-item').parent().css('z-index', 'auto');
                    $(this).closest('.item-item').css('z-index', 'auto');
                }

                //event.stopPropagation();
                //return false;
            });
        },

        initJbeanEvent: function(){
            // 使用京豆优惠、取消京豆优惠
            var me = this;
            $('.cart-warp').delegate('.beans-info a', 'click', function(){
                var data = $(this).attr("id");
                var datas = data.split("_");

                var type = datas[0];
                var pid = datas[1];
                var ptype = datas[2];
                var curPromoId = datas[3] || 0;
                var params = '';

                if(type.indexOf('cancel') != -1){
                    var params = "pid=" + pid
                                + "&ptype=" + ptype
                                + "&promoID=" + curPromoId
                                + "&targetId=" + curPromoId
                                + "&modifyProductPromoID=-1";
                } else{
                    var modifyProductPromId = datas[4];
                    var params = "pid=" + pid
                                + "&ptype=" + ptype
                                + "&promoID=" + curPromoId
                                + "&targetId=" + curPromoId;
                    if(modifyProductPromId){
                        params += "&modifyProductPromoID=" + modifyProductPromId;
                    }
                }

                me.updateProductInfo(me.iurl + "/changeJbeanPromotion.action?rd" + Math.random(),
                    params,
                    '',
                    function(result){
                        // $("input[type=checkbox]").attr("disabled", false).removeAttr("disabled");
                        // me.toggleCheckbox(1, cb, mEl);
                    }
                );
            });
        },

        // 增加交互效果
        // 数量增减的时候，确定频率，频率太高，不请求数据。只按最后一个请求数据
        // 输入数量值
        // 在最小购买数量——最大库存数量和最大商品数量，范围内任意加减;
        // 最大、最小购买量时，弹提示。
        // 最大库存小于最大购买量时，弹提示。

        // 先请求接口，再回调交互。
        bindskuNumEvent:function(){
            var me = this;

            $('.cart-warp').delegate('a.increment', 'click', function(){
                if(me.doing) return;
                me.doing = true;

                var anode = $(this);
                var pnode = anode.parent();
                var inputEl = $('input', pnode);
                var cur = inputEl.val();
                cur++;
                inputEl.css('color','#fff');

                var uphtml = '<span class="upspan"><span style="position:relative;">' + (cur-1) + '</span></span>';
                var downhtml = '<span style="top:28px;" class="downspan"><span style="position:relative;">' + cur + '</span></span>';
                pnode.prepend(uphtml);
                pnode.append(downhtml);

                $(".upspan span:last").animate({top: -28}, "10");
                $(".downspan span:last").animate({top: -28}, "10",function(){
                    $('.downspan,.upspan').remove();
                    inputEl.css('color','#333');
                    inputEl.val(cur);
                    me.addSkuNum(anode);
                });
            });

            $('.cart-warp').delegate('a.decrement', 'click', function(){
                if(me.doing) return;

                var anode = $(this);
                if(anode.hasClass('disabled')){
                    return;
                }

                me.doing = true;

                var pnode = anode.parent();
                var inputEl = $('input', pnode);
                var cur = inputEl.val();
                cur--;
                if(cur == 0){
                    return;
                }

                inputEl.css('color','#fff');
                var uphtml = '<span class="upspan"><span style="position:relative;">' + (cur+1) + '</span></span>';
                var downhtml = '<span style="top:-28px;" class="downspan"><span style="position:relative;">' + cur + '</span></span>';
                pnode.prepend(uphtml);
                pnode.append(downhtml);

                $(".upspan span:last").animate({top: 28}, "10");
                $(".downspan span:last").animate({top: 28}, "10",function(){
                    $('.downspan,.upspan').remove();
                    inputEl.css('color','#333');
                    inputEl.val(cur);
                    me.minusSkuNum(anode);
                });
            });

            //商品数量文本框获取焦点，保存之前的值
            $('.cart-warp').delegate('div.quantity-form input', 'focus', function(event){
                var val = parseInt($(this).val());
                if(isNaN(val)){
                    return;
                }

                if(val){
                    $("#changeBeforeValue").val(val);
                    $("#changeBeforeId").val($(this).attr("id"));
                }
            });

            //改变商品数量
            $('.cart-warp').delegate('div.quantity-form input', 'change', function(event){
                var val = parseInt($(this).val());
                if(!val){
                    $(this).val(1);
                }
                me.inputSkuNum(this);
            });
        },

        // 增加商品数量+
        addSkuNum: function(obj){
            var ss =$(obj).attr("id").split("_");
            var venderid = ss[1];
            var pid = ss[2];
            var pcount = ss[3];
            var ptype = ss[4];
            var targetId = 0;
            if(ss.length==7){
                targetId = ss[6];
            }
            var promoID = $(obj).parent().attr("promoid") || 0;
            var num = parseInt(pcount) + 1;

            ss[3] = num;
            var aimnode = ss.join('_');

            if(num > 0 && num < 1001){
                var params = "venderId=" + venderid
                    + "&pid="+pid
                    + "&pcount="+num
                    + "&ptype="+ptype
                    + "&targetId="+targetId
                    + "&promoID="+promoID;
                this.changeSkuNum(params, $(obj).prev(), aimnode);
            }else{
                //还原input值
                if(!num){
                    $(obj).prev().val(pcount);
                }
            }
        },

        //减少商品数量-
        minusSkuNum: function(obj){
            var ss =$(obj).attr("id").split("_");
            var venderid = ss[1];
            var pid = ss[2];
            var pcount = ss[3];
            var ptype = ss[4];
            var targetId = 0;
            //减的时候没有数量限制的数量 所以数据比add和input都少一位。
            if(ss.length==6){
                targetId = ss[5];
            }

            var promoID = $(obj).parent().attr("promoid") || 0;
            var num = parseInt(pcount) - 1;

            ss[3] = num;
            var aimnode = ss.join('_');

            if(num > 0 && num < 1001){
                var params = "venderId=" + venderid
                        + "&pid="+pid
                        + "&pcount="+num
                        + "&ptype="+ptype
                        + "&targetId="+targetId
                        + "&promoID="+promoID;
                this.changeSkuNum(params, $(obj).next(), aimnode);
            }else{//还原input值
                if(!num){
                    $(obj).next().val(pcount);
                }
            }
        },

        //修改商品数量
        inputSkuNum: function(obj){
            var ss =$(obj).attr("id").split("_");
            var venderid = ss[1];
            var pid = ss[2];
            var pcount = ss[3];
            var ptype = ss[4];
            var targetId = 0;
            var packId = 0;
            if(ss.length==7){
                targetId = ss[6];
            }
            if(ss.length==8){
                targetId = ss[6];
                packId = ss[7];
            }
            var promoID = $(obj).parent().attr("promoid") || 0;
            var num = $(obj).val();

            ss[3] = num;
            var aimnode = ss.join('_');

            if(num > 0 && num < 1001 && pcount != num){
                var params = "venderId=" + venderid
                        + "&pid="+pid
                        + "&pcount="+num
                        + "&ptype="+ptype
                        + "&targetId="+targetId
                        + "&packId="+packId
                        + "&promoID="+promoID;

                this.changeSkuNum(params, $(obj), aimnode);
            }else{//还原input值
                if(num != 0){
                    $(obj).val(pcount);
                }
            }
        },

        changeSkuNum: function(params, obj, aimnode){
            var me = this;
            var mEl = $('input[type=checkbox]', obj.closest('.item-form'));
            this.updateVenderInfo(this.iurl + "/changeSkuNumInCart.action",
                params,
                "修改商品数量失败",
                function(){
                    me.toggleCheckbox(1, true, mEl);
                },
                obj,
                aimnode
            );
        },

        //检查商品id
        checkSku: function(pid){
            if(!pid || isNaN(pid)){
                return false;
            }

            if(parseInt(pid) > 0){
                return true;
            }
            return false;
        },

        initArea: function(){
            //选定地址后，写入cookie，下次打开浏览器时显示上次保存数据。
            // cookie有数据，显示cookie；没有数据默认北京？
            // 根据地址查库存。
            this.ipLocation = $.jCookie("ipLoc-djd") || "1-0-0";

            // 设置cart.jd.com 下该cookie的过期时间为过去，即删除该cookie。
            $.jCookie("ipLoc-djd", this.ipLocation, {expires: -10});

            var me = this;

            $('#jdarea').area({scopeLevel: 3, showLoading: false, initArea: me.ipLocation, cookieOpts:{path: '/', domain: 'jd.com'}, onReady:function(local){
                $.jCookie("ipLoc-djd", me.ipLocation, {path: '/', domain: 'jd.com'});
            }, onChange: function(area, local){
                var ips = local.provinceId + '-' + local.cityId + '-' + local.districtId;
                $.jCookie("ipLoc-djd", ips, {path: '/', domain: 'jd.com'});
                me.ipLocation = ips;
                me.updateCartInfo(me.iurl + '/getCart.action' , null , '获取购物车失败' , null );
            }});
        },

    

        resetUnderline: function(){
            $('.floater').width($('.switch-cart-item').width());
        },

        //弹出登录层，登录成功后跳转到购物车页面
        goToLogin: function(){
            try {
                var cartLoginUrl = PurchaseAppConfig.Domain + "/cart.html?rd="+Math.random();
                $("#isLogin").val(0);
                var me = this;
                login({
                    modal: true,//false跳转,true显示登录注册弹层
                    returnUrl: cartLoginUrl,
                    'clstag1': "login|keycount|5|5",
                    'clstag2': "login|keycount|5|6",
                    firstCheck: false,
                    complete: function() {
                        me.setLogin();
                        window.location.href = cartLoginUrl;
                    }
                });
            } catch (e) {
                window.location.href = cartLoginUrl;
            }
        },

        /**
         * 设置登录标记
         * @param result
         */
        setLogin: function(result){
        	if(result){
        		var isLogin = result.isLogin ? "1" : "0";
        		$("#isLogin").val(isLogin);
        		return;
        	}
            var nologinEl = $('.nologin-tip');
            if(nologinEl&&nologinEl.length){
                nologinEl.remove();
            }
            var loginEl = $('#isLogin');
            if(loginEl&&loginEl.length){
                loginEl.val(1);
            }
        },

        // 去结算
        gotoBalance: function(binter){
            var selected = $(".item-selected");
            if(selected && selected.length){
                try {
                    // 区分京东国际结算 和 京东结算
                    var orderInfoUrl = PurchaseAppConfig.DynamicDomain +"/gotoOrder/gotoOrder.action"
                                    + (binter ? '?flowId=10&rd=' : '?rd=') + Math.random();

                    if ($("#isLogin").val() == 0) {
                        $("#isLogin").val(0);
                        login({
                            modal: true,//false跳转,true显示登录注册弹层
                            returnUrl: orderInfoUrl,
                            'clstag1': "login|keycount|5|5",
                            'clstag2': "login|keycount|5|6&sourcePage=noReg",
                            firstCheck: false,
                            complete: function() {
                                $("#isLogin").val(1);
                                window.location.href = orderInfoUrl;
                            }
                        });
                    } else {
                        $('.submit-btn').hide();
                        $('.submit-btn').after("<span class='checkout-state'>正在转向订单信息填写页面，请稍候！</span>");
                        window.location.href = orderInfoUrl;
                    }
                } catch (e) {
                    window.location.href = orderInfoUrl;
                }
            }else{
                this.showAlertDlg('请至少选中一件商品！');
            }
        },

        clearDialog: function(){
            var me = this;

            $('.item-item').each(function(){
                $(this).parent().css('z-index', 'auto');
                $(this).css('z-index', 'auto');
            });
            // 清除页面窗口
            $('.promotion-tips').each(function(){
                $(this).slideUp(200);
            });

            $('.service-tips').each(function(){
                $(this).slideUp(200);
            });

            $('.gift-box').each(function(){
                $(this).hide();
                if(me.isLowIE()){
                    $(this).closest('.item-header').parent().css('z-index', 'auto');
                }
            });

            $('.selected-item-list').hide();
            $('.amount-sum b').removeClass('down').addClass('up');
        },

        // 吸底
        ceilingAlamp: function(){
            try{
                var lampEl = $('.ui-ceilinglamp-1');
                if(!this.alamp || !lampEl.length){
                    this.alamp = new ceilinglamp($('.cart-toolbar'));
                    this.alamp.options = {
                        currentClassName:'fixed-bottom',
                        zIndex:999,
                        top:0,
                        pos:1,
                        bchange:1,
                        sCss:'fdibu',
                        dCss:'fdibucurrent',
                        callback:show
                    };
                    this.alamp.init();
                }
            }catch(err){
                console.log('ceilinglamp error.');
            }

            // 吸底回调
            function show(ev){
                var el = $('.cart-toolbar');
                if(ev == 'show'){
                    $('.fore1', el).hide();
                    $('.fore2', el).show();
                } else {
                    $('.fore2', el).hide();
                    $('.fore1', el).show();
                }
            }
        },

        initJInitButtons: function(){
            var state= $('#checkedCartState').val();
            if(state == 1 || state == 4){
                $('.normal').show();
                $('.combine').hide();
                $('.submit-btn').attr('data-bind', state);
            }else if(state == 2){
                $('.normal').show();
                $('.combine').hide();
                $('.submit-btn').attr('data-bind', state);
            }else if(state == 3){
                $('.combine').show();
                $('.jdInt-submit-btn').show();
                $('.common-submit-btn').show();
                $('.normal').hide();
            }
        },

        initShowListEvent: function(){
            var me = this;
            $('body').delegate('.amount-sum', 'click', function(event){
                var _this = $('b', $(this));
                var pnode = _this.closest('.toolbar-wrap');

                if(_this.hasClass('up')){
                    // 获取选中商品列表
                    var outSkus = me.outSkus;
                    jQuery.ajax({
                        type     : "POST",
                        dataType : "json",
                        url  : me.iurl + '/getSwitchableBody.action',
                        data : "outSkus=" + outSkus + "&random=" + Math.random(),
                        success : function(result) {
                    	    me.setLogin(result);
                            result = result.sortedWebCartResult;
                            if(result && result.l == 1){
                                me.goToLogin();
                                return;
                            }

                            $('.selected-item-list').prop('outerHTML', result.modifyResult.switchableBodyHtml);
                            $('.selected-item-list', pnode).show();

                            var state= $('#checkedCartState').val();
                            if(state == 1 || state == 4 || state == 2){
                                $('.normal-selected-list').show();
                                $('.combine-selected-list').hide();
                            }else if(state == 3){
                                $('.combine-selected-list').show();
                                $('.normal-selected-list').hide();
                            }

                            // 同一类商品
                            var sele = $('.selected-inner');

                            sele.find('div').switchable({
                                type:'slider',
                                prevClass:'prev',
                                nextClass:'next',
                                pagCancelClass:'disabled',
                                seamlessLoop:false,
                                step:10,
                                visible:10,
                                hasPage:true,
                                autoLock:true,
                                width:92
                            });

                            if (sele.find('li').length > 10) {
                                sele.find('.prev').show();
                                sele.find('.next').show().removeClass('disabled');
                            }

                            // 京东国际和普通商品
                            var iele = $('.int-selected-inner');

                            iele.find('div').switchable({
                                type:'slider',
                                prevClass:'prev',
                                nextClass:'next',
                                pagCancelClass:'disabled',
                                seamlessLoop:false,
                                step:5,
                                visible:5,
                                hasPage:true,
                                autoLock:true,
                                width:92
                            });

                            if (iele.find('li').length > 5) {
                                iele.find('.prev').show();
                                iele.find('.next').show().removeClass('disabled');
                            }

                            // 普通商品
                            var nele = $('.normal-selected-inner');

                            nele.find('div').switchable({
                                type:'slider',
                                prevClass:'prev',
                                nextClass:'next',
                                pagCancelClass:'disabled',
                                seamlessLoop:false,
                                step:4,
                                visible:4,
                                hasPage:true,
                                autoLock:true,
                                width:92
                            });

                            if (nele.find('li').length > 4) {
                                nele.find('.prev').show();
                                nele.find('.next').show().removeClass('disabled');
                            }
                        },
                        error:function(XMLHttpResponse ){
                        }
                    });

                    _this.removeClass('up').addClass('down');
                    log('newcart', 'clickcart','suolvetuzhankai');
                } else{
                    $('.selected-item-list', pnode).hide();
                    _this.removeClass('down').addClass('up');
                    log('newcart', 'clickcart','suolvetuzhedie');
                }
                event.stopPropagation();
                return false;
            });
        },

        initRecommendAndCollection: function(ids){
            if(ids){
                $('#ids').val(ids);
            }

            //if($("#ids").val()){
           //  someMoreRecommend($("#ids").val() || '');
            //}
            
            // 点击推荐位中<加入购物车>按钮，当购物车为空时，也支持添加到购物车
           /* $('body').delegate('#c-tabs .p-btn a', 'click', function(){
            	cartObj.addSkuToCartReBuy($(this).attr('_pid'), 1, 1, 0, 0);
            });
            $('body').delegate('#favorite-products .login-in', 'click', function(){
                cartObj.goToLogin();
            });*/
        },

        showTipInfo: function(objel, info, bright, bup){
            $('body').append(info);

            var tipEl = $('.op-tipmsg');

            if(objel){
                var left = objel.offset().left;
                left = bright ? (left + 20) : (left - 60);

                var top = objel.offset().top;
                top = (bup ? (top-35) : top);

                tipEl.css('top', top + 'px');
                tipEl.css('left', left + 'px');
            } else{
                var width = ($('.follow-batch').offset().left + $('.follow-batch').outerWidth() + 5) + 'px';
                tipEl.css('bottom', '10px');
                tipEl.css('left', width);
                tipEl.css('position', 'fixed');
                tipEl.css('z-index', '999');
            }

            tipEl.show();

            setTimeout(function(){
                tipEl && tipEl.remove();
            }, 2000);
        },

        closeTipInfo: function(){
            $('.op-tipmsg') && $('.op-tipmsg').remove();
        },

        showAlertDlg: function(msg){
            $('body').dialog({
                title: '提示',
                width: 360,
                height: 60,
                type:'html',
                source: '<div class="tip-box icon-box">'
                        + '<span class="warn-icon m-icon"></span>'
                        + '<div class="item-fore">'
                        + '<h3 class="ftx-04">' + msg + '</h3>'
                        + '</div></div>'
            });
        },

        isLowIE: function(){
            if(document.documentMode == 7){
                return true;
            }

            if($.browser.isIE7() || $.browser.isIE6()){
                return true;
            }
        }
    };

    //窗口加载成功后的展示
    //页面进入加载购物车
    $(function(){
        if(/*@cc_on!@*/!1 && !window.XMLHttpRequest) {
            try {document.execCommand("BackgroundImageCache", false, true);} catch(e){}
        }

        if(typeof showUpBrowserTips == 'function'){
        	showUpBrowserTips();
        }
        // cartObj.initSearchUnit();
        if($('.login-btn').length){
            // 登陆
            $('body').delegate('a.login-btn', 'click', function(){
                cartObj.goToLogin();
            });
        } else if($('#cart-list').length){
        	// 绑定商品中功能按键的事件
        	cartObj.init();
        }
        // 推荐位 鼠标滚动到下方时展开。
        cartObj.initRecommendAndCollection();
        
        // 返回顶部
       /* $('#bp-backtop').gotop({
            hasAnimate: true
        });*/
    });
});