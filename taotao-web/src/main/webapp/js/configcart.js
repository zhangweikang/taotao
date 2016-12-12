
// 登录

function goToLogin() {
	
	var pin = $.cookie("TT_TICKET");
	// alert(pin);
	if(pin==null){
		var cartUrl = "http://www.taotao.com/user/login.html";
		  window.location.href = cartUrl;
		//Dialog.open({URL:"cartUrl"});
		
		return ;
	}

}




/*$(function(){
	
	 $("#toggle-checkboxes_down").click(function(){
		 
		 $("input[name='checkItem']").attr("checked", $(this).is(':checked'));
		 
		 $("[name='checkItem'][checked]").each(function(){ 
        	 var a = $(this).val();
        	 alert(a); 
        	 
        	 
        	 
		 }) ; 
	 }); 
	 
	 
	 $("#toggle-checkboxes_up").click(function(){
		 
         $("input[name='checkItem']").attr("checked", $(this).is(':checked'));
         $("[name='checkItem'][checked]").each(function(){ 
        	 var a = $(this).val();
        	 alert(a); 
         }) ;
     }); 
     
	 
	 
});*/



/*

$(function(){
	

	var array = new Array();
	
	 $("#toggle-checkboxes_down").toggle(function(){
		 $("input[name='checkItem']").attr("checked", $(this).is(':checked'));
		 
		 $("[name='checkItem'][checked]").each(function(){ 
       	 var a = $(this).val();
       	 array.push(a);
		 }) ; 
		 var idstr=array.join(',');
			alert(idstr);
			return idstr;
	 }); 
	 
 $("#toggle-checkboxes_up").toggle(function(){
		 
         $("input[name='checkItem']").attr("checked", $(this).is(':checked'));
         
         $("[name='checkItem'][checked]").each(function(){ 
        	 var a = $(this).val();
        	 array.push(a);
         }) ;
         var idstr=array.join(',');
     	alert(idstr);
     	return idstr;
     }); 
	
	
});
*/

/*function totalSelect(){
	
	  $("#toggle-checkboxes_up").click(function(){
	      $("input[name='checkItem']").each(function(){
	    	     this.checked = $("##toggle-checkboxes_up")[0].checked;
	      });
     }); 
}*/

$(function(){
	
	 $("#toggle-checkboxes_up").click(function(){
	      $("input[name='checkItem']").each(function(){
	    	     this.checked = $("#toggle-checkboxes_up")[0].checked;
	      });
    }); 
	 
	 $("#toggle-checkboxes_down").click(function(){
	      $("input[name='checkItem']").each(function(){
	    	     this.checked = $("#toggle-checkboxes_down")[0].checked;
	      });
   });
	 
});





function goToOrder(){
//	alert("aaa");
	var array = new Array();
	
	 $("[name='checkItem'][checked]").each(function(){ 
    	 var a = $(this).val();
    	 array.push(a);
     }) ;
	
	var ids = array.join(',');
	
	 // alert(ids);
	
	if(ids==''){
	   return Dialog.confirm("没有选择商品");
	   // 
	}
	
	var url = "/order/create.html?ids="+ids;
	window.open(url);
}


function goToDelete(){
//	alert(d);
	var array = new Array();
	
	$("[name='checkItem'][checked]").each(function(){
		var a = $(this).val();
		array.push(a);
	});
	
	var ids = array.join(',');
	
	if(ids==''){
		// Dialog.confirm('警告：您确认要XXOO吗？',function(){Dialog.alert("yeah，周末到了，正是好时候")});
		Dialog.confirm('提示:没有选择商品');
		return ;
	}else{
		Dialog.confirm("警告:确定要删除吗?",function(){
			 var url = "/cart/delete.html?ids="+ids;
		     location.href=url;
		 });
		
		/*var d = Dialog.confirm("警告:确定要删除吗?");
		if(d){
			var url = "/cart/delete.html?ids="+ids;
			location.href=url;
		}*/
	}
	
}

// /cart/delete/${cart.id}.html
function goToDeleteOne(id){
	var url = "/cart/delete/"+id+".html";
	 Dialog.confirm("警告:确定要删除吗?",function(){
		 location.href=url;
	 });
	/*alert(id);
	alert(d);
	if(d){
		location.href=url;
	}*/
}
















