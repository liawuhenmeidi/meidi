<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="detaildynamic.jsp"%> 
<%  
Map<Integer,List<Gift>> gMap = GiftService.getmap(); 
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单详细页</title>

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="../css/songhuo.css">
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">

var id = "<%=id%>"; 
var statues = "";
var oppstatues = "<%=modify%>" ; 
var orpstatues = "<%=returns%>" ;
var huanhuo = "<%=huanhuo%>"; 
var canupdate = "<%=canupdate%>";
var delivery = "<%=delivery%>";
var imageflag = 1 ;
function updateOeder(){ 
	if(1 == canupdate){
	   if(delivery == 0){
	   $.ajax({   
	        type:"post",  
	         url:"server.jsp",
	         //data:"method=list_pic&page="+pageCount,
	         data:"method=printlnStatues&oid="+id,
	         dataType: "", 
	         success: function (data) {  
	        	 statues = data ;
	        	 if(statues == 1){ 
	        			 if(oppstatues == 2 ){
	 	      				window.location.href="order.jsp?id="+id;
	 	      				return ;  
	 	      			}else if(oppstatues == -1){
	 	      				alert("已配送，请联系公司文员进行修改");
	 	      				//window.location.href="updatedingmayPrintl.jsp?id="+id;
	 	      			    return ; 
	 	      			}if(oppstatues == 4 ){
	 	      				alert("已配送，请联系公司文员进行修改");
	 	      				//var question = confirm("您的申请被拒绝，确定要重新提交申请吗？");
	 	      				//if(question != "0"){
	 	      				// window.location.href="updatedingmayPrintl.jsp?id="+id;
	 	      				//}
	 	      				
	 	      				return ; 
	 	      			}else {  
	 	      				alert("您已经提交修改申请"); 
	 	      			} 
	      		}else if(statues ==0 ){
	      			
	      			  window.location.href="order.jsp?id="+id;
	      			  return ;
	      		   } 
	           },  
	          error: function (XMLHttpRequest, textStatus, errorThrown) { 
	         // alert(textStatus+"return ;") ;
	            alert("系统忙，请您稍后重试"); 
	          return ;
	          } 
	    });
	   }else { 
		   alert("已送货不能修改");
	   }
    }else { 
    	alert("对不起，不是您的订单，您不能修改");
    }

}
  
function updateOeders(type){ 
	var message = "退货";
	if(type == "huanhuo"){
		message = "换货";
	}
	var question = confirm("你确认要"+message+"吗？");
	if(question != "0"){
		if(1 == canupdate){
			   $.ajax({  
			        type:"post",  
			         url:"server.jsp", 
			         //data:"method=list_pic&page="+pageCount,
			         data:"method=printlnStatues&oid="+id,
			         dataType: "", 
			         success: function (data) {  
			        	 statues = data ;
			        	 if(statues == 1){ 
			      			if(orpstatues == 2 || huanhuo == 2){ 
			      				alert("您的订单已"+message); 
			      				//window.location.href="server.jsp?method=tuihuoed&oid="+id;
			      				return ;   
			      			}else if(orpstatues == -1 || huanhuo == -1){
			      				if(type == "huanhuo"){
			      					window.location.href="OrderServlet?id="+id+"&method="+type;
			      				}else {
			      					 window.location.href="updatedingmayPrintl.jsp?id="+id+"&method="+type;
			      				}
			      				
			      			    return ;  
			      			}if(orpstatues == 4 || huanhuo == 4){
			      				var question = confirm("您的申请被拒绝，确定要重新提交申请吗？");
			      				if(question != "0"){
			      					window.location.href="updatedingmayPrintl.jsp?id="+id+"&method="+type;
			      				}
			      				return ; 
			      			}else {       
			      				alert("您已经提交退货申请"); 
			      			} 
			      		}else if(statues ==0 ){
			      			if(type == "huanhuo"){
			      				alert("商品未送货，不能换货");
			      			}else {
			      				window.location.href="server.jsp?method=tuihuo&oid="+id;
			      			}
			      			
			      			  return ;
			      		   } 
			           }, 
			          error: function (XMLHttpRequest, textStatus, errorThrown) { 
			         // alert(textStatus+"return ;") ;
			          alert("系统忙，请您稍后重试");
			          return ;
			          } 
			    });
		    }else { 
		    	alert("对不起，不是您的订单，您不能退货");
		    }
	}
	
	}

function addImage(src){
	$("#image").html("");
	if(imageflag%2==1){
		$("#image").append("<img src='"+src+"'/>"  );
	}
	imageflag ++;
}

function winfirm(){   

	var fileforload = $("#fileforload").val();

	if(fileforload == null || fileforload == ""){
		alert("请选择文件");  
		return false ;
	}

}

function viewmypic(mypic,imgfile) {        
	if (imgfile.value){        
	mypic.src=imgfile.value;        
	mypic.style.display="";        
	mypic.border=1;        
	}        
	} 
	

function getmap(){
  window.location.href="../map.jsp?id=<%=id%>";
}	
</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
  <table> 
     <tr> 
          <td></td>
          <td><span class="qiangdan"><a href="chaxun_sale.jsp">订单查询</a></span></td>
           <% if(returns != 0 && returns != 2 && huanhuo != 0 && huanhuo != 2 && modify != 0 && or.getDeliveryStatues() != 1 && or.getDeliveryStatues() != 2  ){ %>
          <td><span class="qiangdan"><a href="javascript:void(0)" onclick="updateOeder()">修改</a></span></td>
               <%}  
               if(modify != 0 && modify != 2 && huanhuo != 0 && huanhuo != 2 && returns != 0 ){
            	   if(!((or.getDeliveryStatues() == 1 || or.getDeliveryStatues() == 2) && or.getOderStatus().equals(20+"")) && or.getReturnstatuse() == 0 ){
             %>     
          <td><span class="qiangdan"><a href="javascript:void(0)" onclick="updateOeders('tuihuo')">退货</a></span></td>
              <%    } 
            	}
              if(modify != 0 && modify != 2 && returns != 0 && returns != 2 && ( or.getDeliveryStatues() == 1 ||  or.getDeliveryStatues() == 2) && huanhuo != 0){ 
                   %> 
               <td><span class="qiangdan"><a href="javascript:void(0)" onclick="updateOeders('huanhuo')">换货</a></span></td>
                <% }
                %>
         <td><span class="qiangdan"><a href="serch_list.jsp">返回</a></span></td>
     </tr>
  
  </table>


</div>
 
<!--  头 单种类  -->
 
<div class="s_main_box"> 


<table width="100%" class="s_main_table">

<tr>	
		<td align="left" class="s_list_m">修改状态</td>
		 <td align="left" class="s_list_m">
		 <%
		 String col = "";
		 if(-1 != modify){  
          String sm = "";
          if(0 == modify){
        	  sm = "待确认";
          }else if(1== modify){
        	  sm = "确认中";
          }else if(2== modify){ 
        	  sm = "已同意";
        	  col= "red";
          }else if(4 == modify){
        	  sm = "已拒绝"; 
        	  col= "red";
          }
		 %>
		 <FONT color=<%=col %>><%=sm %></FONT> 
		 <%
		 }else {
		
		%>
		无状态
		
		<% 
		 }
		%>
		</td>
		</tr>
		
		 <tr>	
		<td align="left" class="s_list_m">退货状态</td>
		 <td align="left" class="s_list_m">
		 <%
		 if(-1 != returns){  
          String sm = "";
          if(0 == returns){
        	  sm = "待确认";
          }else if(1== returns){
        	  sm = "确认中";
          }else if(2== returns){
        	  sm = "已同意";
          }else if(4== returns){
        	  sm = "已拒绝";  
          }
		 %> 
		 <%=sm %>
		 <%
		 }else {
		
		%>
		无状态
		<% 
		 }
		%>
		</td>
		</tr>
		
  <tr>
    <td width="25%" class="s_list_m">单号</td> 
    <td class="s_list_m"><%=or.getPrintlnid() == null?"":or.getPrintlnid()%></td>
   
  </tr>
  <%
  List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, or.getId());
  for(int g = 0 ;g<lists.size();g++){
 	 OrderProduct op = lists.get(g);
      if(op.getStatues() == 1 ){
    	  
      String coll = "style='background-color:orange'";
  
  %>
   <tr <%=coll %>>
    <td width="25%" class="s_list_m">票面类别</td>
    <td class="s_list_m"> 

	  <%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName()%>
    </td>
   
  </tr >
  <tr <%=col %>>
    <td width="25%" class="s_list_m">票面型号</td>
    <td class="s_list_m">

		    	 <%=ProductService.getIDmap().get(Integer.valueOf(op.getSaleType())).getType()%> 
 
    </td>
   
  </tr>
  <tr <%=coll %>>
    <td width="25%" class="s_list_m">票面数量</td>
    <td class="s_list_m">

		    	 <%=op.getCount()%>

    </td>
   
  </tr>
  <%
     }else {
    	 String coll = "";
    	  if(g%2==0){
    		
    		  col = "style='background-color:yellow'";
    	  }
    	 %>
    	  
    	 <tr <%=coll %>>
         <td width="55%" class="s_list_m">送货类别</td>
     
         <td class="s_list_m">

    		  <%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName()%> 
         </td>
         </tr>
    	 
     <tr <%=col %>>
     <td width="55%" class="s_list_m">送货型号</td>
     <td class="s_list_m">
		     <%=ProductService.getIDmap().get(Integer.valueOf(op.getSendType())).getType()%>
      </td>
      </tr>
    	 
    	<tr <%=coll %>>
      
      <td width="55%" class="s_list_m">送货数量</td>
     
      <td class="s_list_m">
		    	<%=op.getCount()%>  
	
	</td>
  </tr>  
    	 
    <%   
      }
   }
  %>
   
    <%
    List<Gift> glists = gMap.get(or.getId()); 
    
    if(null != glists){

    for(int g = 0 ;g<glists.size();g++){
   	 
	   	 Gift op = glists.get(g);
	   	 if(null !=op && 0==op.getStatues()){
	   		 
	   		 %>
	    <tr >
      
      <td width="55%" class="s_list_m">赠品</td>
     
      <td class="s_list_m"> 
		    	<%=op.getName()%>  
	
	</td>
  </tr>  
	 <tr >
      
      <td width="55%" class="s_list_m">赠品数量</td>
     
      <td class="s_list_m"> 
		    	<%=op.getCount()%>  
	
	</td>
  </tr>    		 
	   		 
	   		 <%
	   	 }
   	 }
    }
  %>
  <tr>
    <td class="s_list_m">顾客姓名</td> 
    <td class="s_list_m"><%=or.getUsername() %></td>
  </tr>
   <tr>
  
  <tr >
    <td class="s_list_m">顾客电话</td> 
    
    <td><a href="tel:<%=or.getPhone1() %>"><%=or.getPhone1() %></a></td>
    <!--  一键拨号  -->
  </tr>
   
    <tr >
    <td class="s_list_m">送货时间</td>
    <td align="left" class="s_list_m"><%=or.getOdate() %></td>
    
  </tr> 
  <tr>
    <td class="s_list_m">送货地区</td>
    <td class="s_list_m"><%=or.getLocate() %></td>
  </tr>
  
  <tr>
    <td class="s_list_m">送货地点</td>
    <td class="s_list_m"><%=or.getLocateDetail() %></td>
  </tr>
  <tr>
    <td class="s_list_m">安装单位</td> 
  <% if(or.getDealsendId() != 0 ){ 
		%>
		 <td class="s_list_m"align="left"><%=usermap.get(Integer.valueOf(or.getDealsendId())).getUsername() %> 
		</td>
		<% 
		}else {

		%>
		<td align="left" class="s_list_m"></td>    
		<%
		}
		%> 
      </tr>	
       <tr>
    <td class="s_list_m">安装单位电话</td>
     <% if(or.getDealsendId() != 0 ){ 
		%>
		<td><a href="tel:<%=usermap.get(Integer.valueOf(or.getDealsendId())).getPhone() %>"><%=usermap.get(Integer.valueOf(or.getDealsendId())).getPhone() %></a></td>
		<%
		}else { 

		%>
		<td align="left" class="s_list_m"></td>    
		<%
		}
		%> 
      </tr>	
  <tr>
    <td class="s_list_m">送货员</td>
  <% if(or.getSendId() != 0 ){
		%>
		 <td class="s_list_m"align="left"><%=usermap.get(Integer.valueOf(or.getSendId())).getUsername() %> 
		</td>
		<% 
		}else {

		%>
		<td align="left" class="s_list_m"></td>    
		<%
		}
		%> 
      </tr>	
       <tr>
    <td class="s_list_m">送货员电话</td>
     <% if(or.getSendId() != 0 ){
		%>
		<td><a href="tel:<%=usermap.get(Integer.valueOf(or.getSendId())).getPhone() %>"><%=usermap.get(Integer.valueOf(or.getSendId())).getPhone() %></a></td>
		<%
		}else {

		%>
		<td align="left" class="s_list_m"></td>    
		<%
		}
		%> 
      </tr>	
      
 
    
  <tr>
    <td class="s_list_m">送货状态</td>
    <td align="left" class="s_list_m">
    <%
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == or.getDeliveryStatues()){
		%>
		 未发货
		 
		<%
          }else if(1 == or.getDeliveryStatues()){

		%>
		已送货
		
		<% 
          }else if(2 == or.getDeliveryStatues()){
		%>
	 已安装   
		<%
          }
		%>
    
    </td>
  
  </tr> 
      
   <tr >
    <td class="s_list_m">备注</td>
    <td align="left" class="s_list_m"><%=or.getRemark() %></td>
    
  </tr> 
  
  
  <tr></tr>
  <tr></tr>
  <tr>

  </tr>
</table>

  <br>
  <br>
  
<span class="qiangdan"><a href="javascript:void(0);" onclick="getmap()">进入地图</a></span>
 
<!--  zong end  -->
</div>

</body>
</html>