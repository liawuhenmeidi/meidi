var index = "";//当前焦点的ID//行点击事件，参数
 
$(function () { 
	$("#wrap").bind("scroll", function(){ 

		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		}); 

}); 

function fixation(){
	$("#wrap").bind("scroll", function(){ 

		if(pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)){
	        //滚动了竖直滚动条
	        pre_scrollTop=($("#wrap").scrollTop() || document.body.scrollTop);
	       
	        if(obj_th){
	            obj_th.style.top=($("#wrap").scrollTop() || document.body.scrollTop)+"px";
	        }
	    }
	    else if(pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)){
	        //滚动了水平滚动条
	        pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollLeft);
	    }
		});
}

function updateClass(obj) { 
	
	 if (obj.id != index) { //点击了非当前焦点的行
         obj.className = "asc_enable"; //将当前焦点的行设为红色
         if(index != null && index != ""){
        	 document.getElementById(index).className = "asc";//原先的当前焦点行设为灰色
         }
        
         //改变当前焦点标识
         index = obj.id;
     }

}
 
var pre_scrollTop=0;//滚动条事件之前文档滚动高度
var pre_scrollLeft=0;//滚动条事件之前文档滚动宽度
var obj_th;
window.onload =function () {
    pre_scrollTop=(document.documentElement.scrollTop || document.body.scrollTop);
    pre_scrollLeft=(document.documentElement.scrollLeft || document.body.scrollTop);
    obj_th=document.getElementById("th");
};

function changecss(){
	 $(".fixedHead ").css({ 
		 "position":"fixed"
		 }); 
	 
	 $(".tabled tr td").css({  
		 "width":"50px"
		 });  

	 $("#table").css({  
	     "width":"1100px",
	     "table-layout":"fixed"
	});
	 
	 $("#th").css({
		 "background-color" :"white",
		 "position" :"absolute",
		 "width" :"1100px",
		 "height" :"30px",
		 " top" :"0",
		 " left" :"0"
	 });
	 
	 $("#wrap").css({ 
	    
	    "position":"relative",
	    "padding-top":"30px",
	    "overflow":"auto",
	    "height":"400px"
	});

}


function initOrder(type,statues,num,page,sort,sear){
	 $("#page").val(page);
	 $("#table .asc").remove();  
	 $("#table .asc_enable").remove();
	 var str = "";
	 if("8" == type || "33" == type){
		 str = "../";
	 }  
	 $("#dateadd").css("display","block"); 
	 $.ajax({  
	        type: "post",  
	         url: str+"OrderServiceServlet",    
	         data:"method=GETLIST&type="+type+"&statues="+statues+"&num="+num+"&page="+page+"&sort="+sort+sear,
	         dataType: "",   
	         success: function (data) { 
	        	 var json =  $.parseJSON(data);
	        	  var html = json.html;
	        	 
	        	  count = json.count;
                  var date = json.date ; 
                  //alert(date);
                  if("" != date && null != date){
                	 date =  aftersale(date,statues);
                	 $("#table").append(date);
                  }
	        	 $("#table").append(html);
	        	
	        	 $("#count").html(count);
	        	 $("#dateadd").css("display","none"); 
	           },   
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 
	            } 
	           }); 
}
 


function seletall(all){
	if($(all).attr("checked")){
		$("input[type='checkbox'][id='check_box']").each(function(){
			$(this).attr("checked",true);
	     });
	}else if(!$(all).attr("checked")){
		$("input[type='checkbox'][id='check_box']").each(function(){
			$(this).attr("checked",false);
	     });
	};
}

function addcount(){
	 var totalcount = 0 ;
	 $("input[name='saleresut']").each(function(){
			var id = $(this).val();
			var price = $("#"+id+"left").val();
			if(!isNaN(price)){
				 totalcount += price*1;
		      } 
			
	     });
	 $("#addcount").html(totalcount);
}
 

function save(method){
	 var branch = new Array();
	 var mess = new Array();
	 var attract = new Array();
	 var message = "";
	 var url= "../";
	 //if(-1 != num){ 
	//	 alert("请选择行数为所有");
	//	 return false ;
	// } 
	 if("dealsendcharge" == method){
		 message = "您不能保存，请选择具体安装网点";
		 url = "";
	 }else if("sendcharge" == method){
		 message = "您不能保存，请选择具体送货员";
	 }else if("installcharge" == method){
		 message = "您不能保存，请选择具体安装员";
	 }else if("sendinstallcharge" == method){
		 message = "您不能保存，请选择具体送货员";
	 } 
	 
	 $("input[name='dealsendid']").each(function(){
			var dealsend = $(this).val();
			if($.inArray(dealsend,branch) == -1){
				branch.push(dealsend);
			}
	     }); 
	 if(branch.length >1){
		 alert(message);
		 return ;
	 }
	 
	 $("input[name='saleresut']").each(function(){
			var id = $(this).val();
			var price = $("#"+id+"left").val();
			mess.push(id+"-"+price+"-"+0);
	 });
	 
	// alert(mess);
	 //jPrompt('请输入keleyi.com或者其他:', 'keleyi.com(预填值)', 'Prompt对话框', function(r) {
		 //   if( r ) alert('You entered ' + r);
	//	});
	 var str = window.prompt("请输入文件名称","") ;
	 
	 if(str == null || str == ""){
		 alert("文件名称不能为空");
		 return false;
	 }

		
	 $("input[name ='orderid']").each(function(){          
			var str = this.value;
			if(str != null && str != ""){
				attract.push(str);
				}
	   	}); 
	  
	 $.ajax({  
	        type: "post",  
	         url: url+"server.jsp",   
	         data:"method="+method+"&id="+attract.toString()+"&branchid="+branch.toString()+"&name="+str+"&message="+mess,
	         dataType: "", 
	         success: function (data) {
	        	 initOrder(type,statues,num,page,sort,sear);
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        	 alert("执行失败"); 
	            } 
	           });
	 
 }


