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
	 if("8" == type){
		 str = "../";
	 }
	 $.ajax({  
	        type: "post", 
	         url: str+"OrderServiceServlet",    
	         data:"method=GETLIST&type="+type+"&statues="+statues+"&num="+num+"&page="+page+"&sort="+sort+sear,
	         dataType: "",   
	         success: function (data) { 
	        	 var json =  $.parseJSON(data);
	        	  var html = json.html;
	        	 
	        	  count = json.count;
               
	        	 $("#table").append(html);
	        	 $("#count").html(count);
	        	
	           },   
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });

}


