(function(){
	var hotwords="<strong>����������</strong><a href='http://search.jd.com/Search?keyword=%E9%AD%94%E9%94%A5%E8%80%B3%E6%9C%BA&enc=utf-8&jdr=hot' target='_blank'>ħ׶����</a><a href='http://search.jd.com/Search?keyword=%E6%99%BA%E8%83%BD%E7%94%B5%E8%A7%86&enc=utf-8&jdr=hot' target='_blank'>���ܵ���</a><a href='http://search.jd.com/Search?keyword=%E8%A1%A5%E6%B0%B4%E9%9D%A2%E8%86%9C&enc=utf-8&jdr=hot' target='_blank'>��ˮ��Ĥ</a><a href='http://search.jd.com/Search?keyword=%E6%91%84%E5%BD%B1&enc=utf-8&jdr=hot' target='_blank'>��Ӱ</a>";
	var keywords="����";
	$("#hotwords").html(hotwords);
	$("#key").val(keywords).bind("focus",function(){
		if (this.value==keywords){this.value="";this.style.color="#333"}
	}).bind("blur",function(){
		if (this.value==""){this.value=keywords;this.style.color="#999"}
	});
})();