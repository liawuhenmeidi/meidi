<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<!DOCtype html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body style="scoll:no">
   
<!--   头部开始   --> 
<script type="text/javascript" src="../../js/calendar.js"></script> 
<script type="text/javascript" src="../../js/cookie/jquery.cookie.js"></script>
<script type="text/javascript">
 
function checkedd(){ 
	var list = $("input");
	var oderStatus = "";
	var deliveryStatues = "";
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

</script>

 
<div id="wrapsearch" style="position:fixed;text-align:center; top:50%;background-color:#ACD6FF; left:30%; margin:-250px 0 0 -250px; height:500px; width:1000px; z-index:999;display:none"> 
<div >
<table  cellspacing="1" style="margin:auto;background-color:black; width:95%;height:450px;">  
		   
		<tr class="bsc">
		   	<td align="center" >单号</td> 
			<td align="center" >
	        <input type="text"  name="printlnid" id="printlnid" value=""  />
			</td>
			<td align="center" >门店名</td>
			<td align="center" >
	        <input type="text"  name="orderbranch" id="orderbranch" value=""  />
			</td>
			<td align="center" >销售员</td>
			<td align="center" >
	        <input type="text"  name="saleID" id="saleID" value=""  />
			</td>
		</tr>
		
		<tr class="bsc">
		    <td align="center" >顾客姓名</td>
			<td align="center" >
	        <input type="text"  name="username" id="username" value=""  />
			</td>
			<td align="center" >顾客电话</td>
			<td align="center" >
	        <input type="text"  name="phone1" id="phone1" value=""  />
			</td>
			<td align="center" >送货员</td>
			<td align="center" >
	        <input type="text"  name="sendId" id="sendId" value=""  />
			</td>	 
		</tr>
		
		<tr class="bsc">
		<td align="center" >开票日期</td>
			<td align="center" colspan=2 >
			<input class="date2" name="saledatestart" type="text" id="saledatestart" onclick="new Calendar().show(this);" />
                                         至
			<input class="date2" name="saledateend" type="text" id="saledateend" onclick="new Calendar().show(this);" />
			</td>
			<td align="center" >文员配单日期</td>
			<td align="center" colspan=2 > 
			<input class="date2" name="dealsendTimestart" type="text" id="dealsendTimestart" onclick="new Calendar().show(this);" />
                                         至
			<input class="date2" name="dealsendTimeend" type="text" id="dealsendTimeend" onclick="new Calendar().show(this);" />
			</td>	 
		</tr>
		
		<tr class="bsc">
		    <td align="center" >送货品类</td>
			<td align="center" >
	        <input type="text"  name="categoryname" id="categoryname" value=""  />
			</td>
			<td align="center" >送货型号</td>
			<td align="center" >
	        <input type="text"  name="sendtype" id="sendtype" value=""  />
			</td> 
			<td align="center" >安装员</td> 
			<td align="center" >
	        <input type="text"  name="installid" id="installid" value=""  />
			</td>	 
		</tr>
		
		<tr class="bsc">
		<td align="center" >票面品类</td>
			<td align="center" >
	        <input type="text"  name="categoryname" id="categoryname" value=""  />
			</td>	
		<td align="center" >票面型号</td>
			<td align="center">
	        <input type="text"  name="saletype" id="saletype" value=""  />
			</td> 
		<td align="center" >是否已回访</td>
			<td align="center"  >
			   是<input type="radio"  name="statuescallback"  value="1"  id="statuescallback1" />
	                           否<input type="radio"  name="statuescallback"  value="0"  id="statuescallback0"/>
	                           任意<input type="radio"  name="statuescallback"  value=""  id="statuescallback"/>
			</td>
		</tr>
		 <tr class="bsc">
		    <td align="center" >赠品</td>
			<td align="center" >
	        <input type="text"  name="username" id="username" value=""  />
			</td>
			<td align="center" >赠品是否已自提</td>
			<td align="center" >
		                是<input type="radio"  name="statues"  value="1"  id="statues01"/>
	                           否<input type="radio"  name="statues"  value="0"  id="statues00"/>
	                           任意<input type="radio"  name="statues"  value=""  id="statues0"/>
			</td>
			<td align="center" >先送货后安装</td>
			<td align="center" >
			 是<input type="radio"  name="deliverytype"  value="1"  id="deliverytype1" />
	                           否<input type="radio"  name="deliverytype"  value="0"  id="deliverytype0"/>
	                           任意<input type="radio"  name="deliverytype"  value=""  id="deliverytype"/>
			</td>	 
		</tr>
		<tr class="bsc">
		    <td align="center" >是否给送货员点结款</td>
			<td align="center"  >
			   是<input type="radio"  name="statuesPaigong"  value="1"  id="statuesPaigong1" />
	                           否<input type="radio"  name="statuesPaigong"  value="0"  id="statuesPaigong0"/>
	                           任意<input type="radio"  name="statuesPaigong"  value=""  id="statuesPaigong"/>
			</td>
			<td align="center" >是否给安装员点结款</td>
			<td align="center"  >
			   是<input type="radio"  name="statuesinstall"  value="1"  id="statuesinstall1" />
	                           否<input type="radio"  name="statuesinstall"  value="0"  id="statuesinstall0"/>
	                           任意<input type="radio"  name="statuesinstall"  value=""  id="statuesinstall"/>
			</td>
			<td align="center" >是否给送货安装员结款</td>
			<td align="center"  >
			   是<input type="radio"  name="statuesinstall"  value="1"  id="statuesinstall1" />
	                           否<input type="radio"  name="statuesinstall"  value="0"  id="statuesinstall0"/>
	                           任意<input type="radio"  name="statuesinstall"  value=""  id="statuesinstall"/>
			</td>
		
		</tr>
		
		<tr class="bsc">	 
			<td align="center" >上报状态</td>
			<td align="center" colspan=5>
	                         需派送<input type="checkbox"  name="oderStatus"  value="0"  id="oderStatus0"/>
	                         已自提<input type="checkbox"  name="oderStatus"  value="8"  id="oderStatus8"/>
	   只安装(门店提货)<input type="checkbox"  name="oderStatus"  value="9"  id="oderStatus9"/>
	    只安装(顾客已提)<input type="checkbox"  name="oderStatus"  value="10"  id="oderStatus10"/>
	                          换货单<input type="checkbox"  name="oderStatus"  value="20"  id="oderStatus20"/>
	                                
			</td>
		</tr>
		<tr class="bsc">	 
			<td align="center" >送货状态</td>  
			<td align="center" colspan=5>
	                           已送货<input type="checkbox"  name="deliveryStatues" value="1"  id="deliveryStatues1" />
	                           已安装 <input type="checkbox"  name="deliveryStatues"  value="2" id="deliveryStatues2"/>
	                          未送货  <input type="checkbox"  name="deliveryStatues"  value="0" id="deliveryStatues0"/>
	                          已退货<input type="checkbox"  name="deliveryStatues"  value="-1" id="deliveryStatues-1"/>
	                   
			</td> 
		</tr>
		
		

		<tr class="bsc">
		    <td width="33%" class="center" colspan="2"><input type="button" onclick="checkinit()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="清除" /></td>
			<td width="33%" class="center" colspan="2"><input type="button" onclick="tuichu()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="退出" /></td>
			<td width="34%" class="center" colspan="2"><input type="button" onclick="checkedd()"  style="background-color:#ACD6FF;font-size:25px;width:200px"  value="搜索" /></td>
		</tr>
	
</table> 
</div>

</div>

</body>
</html>
