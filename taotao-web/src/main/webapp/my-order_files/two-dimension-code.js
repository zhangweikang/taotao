/*
 myjd-2014 Compressed by uglify 
 Author:hanyuxinting 
 Date: 2014-08-19 
 */
$.fn.fixable=function(a){var b={x:"left",y:"top",xValue:0,yValue:0};a=$.extend(b,a);var c="BackCompat"==document.compatMode?document.body:document.documentElement,d=$(this),e=a,f=$.browser.msie&&6==$.browser.version,g={},h=e.xValue,i=e.yValue;if("center"==h){var j=d.outerWidth()/2;f?h=c.clientWidth/2-j:(g.marginLeft=-j,h="50%")}if("center"==i){var k=d.outerHeight()/2;f?i=c.clientHeight/2-k:(g.marginTop=-k,i="50%")}f?(g.position="absolute","top"==e.y&&d[0].style.setExpression("top","eval((document.documentElement||document.body).scrollTop+"+i+") + 'px'"),"bottom"==e.y&&d[0].style.setExpression("top","eval((document.documentElement||document.body).scrollTop+ "+-i+" + (document.documentElement||document.body).clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0))+'px'"),"left"==e.x&&d[0].style.setExpression("left","eval((document.documentElement||document.body).scrollLeft+"+h+") + 'px'"),"right"==e.x&&d[0].style.setExpression("left","(eval((document.documentElement||document.body).scrollLeft + "+-h+" + (document.documentElement||document.body).clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0)) + 'px'")):(g.position="fixed",g[e.x]=h,g[e.y]=i),d.css(g)},$(window).resize(function(){var a={},b=$(window).width(),c=$("#jd-code").width();c>(b-990)/2?(a.x="right",a.y="bottom",a.xValue=0,a.yValue="center",$("#jd-code").css("left","")):(a.x="left",a.y="bottom",a.xValue=(b-990)/2+1e3,a.yValue="center",$("#jd-code").css("right","")),$("#jd-code").fixable(a)}),$(window).resize();