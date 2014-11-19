<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<!DOCtype html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body style="scoll:no">
   
<!--   头部开始   --> 
<script type="text/javascript" src="../js/calendar.js"></script> 

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
			<td align="center" >pos(厂送)单号</td>
			<td align="center" >
	        <input type="text"  name="pos" id="pos" value=""  />
			</td>
			<td align="center" >OMS订单号</td>
			<td align="center" >
	        <input type="text"  name="sailId" id="sailId" value=""  />
			</td>
			<td align="center" >验证码(联保单)</td>
			<td align="center" >
	        <input type="text"  name="checked" id="checked" value=""  />
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
			<td align="center" >安装网点</td>
			<td align="center" >
	        <input type="text"  name="dealSendid" id="dealSendid" value=""  />
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
		    <td align="center" >送货名称</td>
			<td align="center" >
	        <input type="text"  name="categoryname" id="categoryname" value=""  />
			</td>
			<td align="center" >送货型号</td>
			<td align="center" >
	        <input type="text"  name="sendtype" id="sendtype" value=""  />
			</td> 
			<td align="center" >是已发提成</td> 
			<td align="center" >
	        <input type="text"  name="dealSendid" id="dealSendid" value=""  />
			</td>	 
		</tr>
		
		<tr class="bsc">
		<td align="center" >票面名称</td>
			<td align="center" >
	        <input type="text"  name="categoryname" id="categoryname" value=""  />
			</td>	
		<td align="center" >票面型号</td>
			<td align="center">
	        <input type="text"  name="saletype" id="saletype" value=""  />
			</td> 
		<td align="center" >是否给安装网点结款</td>
			<td align="center"  >
			   是<input type="radio"  name="statues4"  value="1"  id="statues41" />
	                           否<input type="radio"  name="statues4"  value="0"  id="statues40"/>
	                           任意<input type="radio"  name="statues4"  value=""  id="statues4"/>
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
			<td align="center" >预约日期</td>
			<td align="center" ><input class="date2" name="andate" type="text" id="andate" onclick="new Calendar().show(this);" /></td>
			<td align="center" >送货地区</td>
			<td align="center" > <input type="text"  name="locates" id="locates" value=""  /></td>
			<td align="center" >送货地址</td>
			<td align="center" ><input type="text"  name="locateDetail" id="locateDetail" value=""  /></td>
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
			<td align="center" colspan=3>
	                           已送货<input type="checkbox"  name="deliveryStatues" value="1"  id="deliveryStatues1" />
	                           已安装 <input type="checkbox"  name="deliveryStatues"  value="2" id="deliveryStatues2"/>
	                          未送货  <input type="checkbox"  name="deliveryStatues"  value="0" id="deliveryStatues0"/>
	                          已退货<input type="checkbox"  name="deliveryStatues"  value="-1" id="deliveryStatues-1"/>
	                   
			</td> 
			<td>备注</td>
			<td> <input type="text"  name="remark" id="remark" value=""  /></td>
		</tr>
		
		<tr class="bsc">
		    <td align="center" >厂送票是否已回</td>
			<td align="center" >
	                  是<input type="radio"  name="statues1"  value="1"  id="statues11"/>
	                 否<input type="radio"  name="statues1"  value="0"  id="statues10"/>
	                  任意<input type="radio"  name="statues1"  value=""  id="statues1" />
			</td>
			<td align="center" >厂送票是否已消</td>
			<td align="center" >
		        是<input type="radio"  name="statues2"  value="1"  id="statues21" />
	                   否<input type="radio"  name="statues2"  value="0"  id="statues20"/>
	                      任意<input type="radio"  name="statues2"  value=""  id="statues2" />
	        
			</td>
			<td align="center" >厂送票是否已结款</td>
			<td align="center" >
	                  是<input type="radio"  name="statues3"  value="1"  id="statues31"/>
	                   否<input type="radio"  name="statues3"  value="0"  id="statues30"/>
	                   任意<input type="radio"  name="statues3"  value=""  id="statues3"/>
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
