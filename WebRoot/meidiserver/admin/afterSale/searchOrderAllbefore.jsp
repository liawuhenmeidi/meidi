<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<!DOCtype html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body style="scoll:no">
    
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/calendar.js"></script> 
<script type="text/javascript" src="../../js/cookie/jquery.cookie.js"></script>
<script type="text/javascript"> 
 
function checkedd(){ 
	sear = "";
	var list = $("input");
	var oderStatus = "";
	var deliveryStatues = "";
	var sendcategoryname = "";
	var salecategoryname = "";
	for(var i=0;i<list.length;i++){
		var input = list[i];
		
		var inputType = $(input).attr("type");
		if("text" == inputType){
			var value = $(input).val();
			if(value != null && value != ""){
				var name = $(input).attr("name");
				if(name != null && name != "" && name != undefined)
				sear += "&"+name+"="+value;
			}
		}else if("radio" == inputType){
			var flag = $(input).is(':checked');
			if(flag){
				var value = $(input).val();
				if(value != null && value != ""){
					var name = $(input).attr("name");
					sear += "&"+name+"="+value;
				}
			}
			
		}else if("checkbox" == inputType){
			var flag = $(input).is(':checked');
			if(flag){
				var name = $(input).attr("name");
				var value = $(input).val();
				if("oderStatus" == name){
					oderStatus += value+",";
				}else if("deliveryStatues" == name){
					deliveryStatues += value+"," ;
				}else if("sendcategoryname" == name){
					sendcategoryname += value+",";
				}else if("salecategoryname" == name){
					salecategoryname += value +",";
				}
			}
		} 
	} 
	 
	if(oderStatus != null && oderStatus != ""){
		oderStatus = oderStatus.substring(0,oderStatus.length-1);
		sear += "&oderStatus="+oderStatus; 
	}
	if(deliveryStatues != null && deliveryStatues != ""){
		deliveryStatues = deliveryStatues.substring(0,deliveryStatues.length-1);
		sear += "&deliveryStatues="+ deliveryStatues;
	}
	if(sendcategoryname != null && sendcategoryname != ""){
		sendcategoryname = sendcategoryname.substring(0,sendcategoryname.length-1);
		sear += "&sendcategoryname="+ sendcategoryname;
	}
	if(salecategoryname != null && salecategoryname != ""){
		salecategoryname = salecategoryname.substring(0,salecategoryname.length-1);
		sear += "&salecategoryname="+ salecategoryname;
	} 
	$.cookie("sear", sear);
	
	if(sear != null && sear != ""){
		sear += "&searched=searched";
	}
	
	initOrder(type,statues,num,page,sort,sear);
	
	$("#wrapsearch").css("display","none"); 
	//window.opener.location.href="manualCheckout.jsp?search=true";
}
 
 function checkinit(){
	 var list = $("input");
		for(var i=0;i<list.length;i++){
			var input = list[i];
			
			var inputType = $(input).attr("type");
			if("text" == inputType){
				$(input).val("");
			}else if("radio" == inputType){
				var flag = $(input).is(':checked');
				if(flag){
					 $(input).attr("checked",false);
				}
			}else if("checkbox" == inputType){
				var flag = $(input).is(':checked');
				if(flag){
					$(input).attr("checked",false); 
					
				}
			}
		} 
    sear = "";
 }
 
 function tuichu(){
	 $("#wrapsearch").css("display","none"); 
 }
 //<input type="checkbox"  name="deliveryStatues" value="1"  id="deliveryStatues1" />
 function intsearch(){
	 $("#sendcategorynamecd <input>").remove();
	 $("#salecategorynamecd <input>").remove();
		if(null != jsonallp ){ 
			var htmlsend = "";
			var htmlsale = "";
			for(var i=0;i<jsonallp.length;i++){
				var op = jsonallp[i]; 
				//html += '<option id="'+op.id+'">'+op.type+'</option>';
				htmlsend += '<input type="checkbox"  name="sendcategoryname" value="'+op.id+'"  id="sendcategoryname'+op.id+'" />'+op.name+"  ";
				htmlsale += '<input type="checkbox"  name="salecategoryname" value="'+op.id+'"  id="salecategoryname'+op.id+'" />'+op.name+"  ";
			} 
			$("#sendcategorynamecd").append(htmlsend);
			$("#salecategorynamecd").append(htmlsale);
		}
		
		//$("#sendtype").autocomplete({ 
		//	 source: jsonallp
		//   });
	}

 
</script>

 
<div id="wrapsearch" style="position:fixed;text-align:center; top:50%;background-color:white; left:30%; margin:-20% 0 0 -20%; height:60%; width:80%; z-index:999;display:none"> 
<div >
<table  cellspacing="1" style="margin:auto;background-color:black; width:95%;height:300px;">  
		   
		
		 
		<tr class="asc">
		    <td align="center" >顾客姓名</td>
			<td align="center" > 
	        <input type="text"  name="username" id="username" value=""  />
			</td>
			<td align="center" >顾客电话</td>
			<td align="center" >
	        <input type="text"  name="phone1" id="phone1" value=""  />
			</td> 
			<td align="center" >安装网点</td>
			<td align="center" >
	        <input type="text"  name="dealSendid" id="dealSendid" value=""  />
			</td>	 
		</tr>
		
		<tr class="asc">
		   <td align="center" >送货型号</td>
			<td align="center" >
	        <input type="text"  name="sendtype" id="sendtype" value=""  />
			</td> 
			<td align="center" >送货地区</td>
			<td align="center" > <input type="text"  name="locates" id="locates" value=""  /></td>
			<td align="center" >送货地址</td>
			<td align="center" ><input type="text"  name="locateDetail" id="locateDetail" value=""  /></td>
		</tr>
		
		
		<tr class="asc">
		    <td align="center" >送货名称</td>
			<td align="center" id="sendcategorynamecd" colspan=5>
			</td>
         </tr>
		


		<tr class="asc">
		    <td width="33%" class="center" colspan="2"><input type="button" onclick="checkinit()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="清除" /></td>
			<td width="33%" class="center" colspan="2"><input type="button" onclick="tuichu()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="退出" /></td>
			<td width="34%" class="center" colspan="2"><input type="button" onclick="checkedd()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="搜索" /></td>
		</tr>
	
</table> 
</div>

</div>
<div id="dateadd" style="position:fixed;text-align:center; top:50%;background-color:white; left:30%; margin:-10% 0 0 -20%; height:5%; width:80%; z-index:999;display:none"> 
<div >
<table  cellspacing="1" style="margin:auto;background-color:black; width:95%;heigth:95%;"> 
 
		<tr class="asc"> 
		    <td align="center" >数据加载中......,请等待</td>
		</tr> 
</table> 
</div>

</div>
</body>
</html>
