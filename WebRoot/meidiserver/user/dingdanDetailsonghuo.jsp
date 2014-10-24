<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ include file="detaildynamic.jsp"%> 
<% 
int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user); 
 
request.setAttribute("order", or);

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
var canupdate = "<%=canupdate%>";
var pgroup = "<%=pgroup%>";
var opstatues = "<%=opstatues%>";

function updateOeder(){ 
  
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
	      			if(oppstatues == 2 ){
	      				window.location.href="updatedingmay.jsp?id="+id;
	      				return ; 
	      			}else if(oppstatues == -1){
	      				window.location.href="updatedingmayPrintl.jsp?id="+id;
	      			    return ;
	      			}else {  
	      				alert("您已经提交修改申请"); 
	      			}
	      		}else if(statues ==0 ){
	      			
	      			  window.location.href="updatedingmay.jsp?id="+id;
	      			  return ;
	      		   } 
	           }, 
	          error: function (XMLHttpRequest, textStatus, errorThrown) { 
	         // alert(textStatus+"return ;") ;
	          alert("您不能修改，请与管理员联系");
	          return ;
	          } 
	    });
    }else { 
    	alert("对不起，不是您的订单，您不能修改");
    }

}


function updateOeders(){
  window.location.href="updatedingmayPrintl.jsp?id="+id+"&method=tuihuo";
	}
	
function getmap(){
  window.location.href="../map.jsp?id=<%=id%>";
}	


function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}
 
function winconfirm(str){
	if(str == 0 ){
		alert("您已经提交释放申请");
		return ;
	}else if(str == 4){
		question = confirm("您的申请被拒绝，是否继续提交？");
	}else { 
		question = confirm("你确认要释放吗？");
	}
		
		if (question != "0"){
			
			//alert(attract.toString());
			$.ajax({   
		        type:"post", 
		         url:"server.jsp",
		         //data:"method=list_pic&page="+pageCount,
		        data:"method=shifang&oid="+id+"&pGroupId="+pgroup+"&opstatues="+opstatues,
		         dataType: "", 
		         success: function (data) { 
		          alert("释放申请已提交成功");    
		          window.location.href="dingdanDetailsonghuo.jsp?id="+id;
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		          alert("释放申请失败");
		            } 
		           }); 
		 }
	}

function change(str1,str2,type){
		var uid = $("#"+str1).val(); 
		if(uid == -1){
			return false;
		}
		$.ajax({  
	        type: "post",   
	         url: "server.jsp", 
	         data:"method=songhuo&id="+str2+"&statues="+uid+"&type="+type,
	         dataType: "",  
	         success: function (date) {
	        	
	        	 if(date == 0){
	        		 alert("导购提交修改申请，不能配工");
	        		 return ; 
	        	 }else if(date == 20){  
	        		 alert("导购提交退货申请，不能配工");
	        		 return ;
	        	 } else{
	        		 alert("设置成功"); 
	  	           if(type == 21){ 
	  	        	   window.location.href="tuihuo.jsp";
	  	           }else {
	  	        	   window.location.href="songhuo.jsp";
	  	           } 
	        	 }
	           
	           
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	        // alert(errorThrown); 
	            } 
	           });
	}

</script>
</head>

<body>
<div class="s_main">
<jsp:include flush="true" page="../head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
  
<span class="qiangdan"><a href="welcom.jsp">返回</a></span><span class="qiangdan"><a href="server.jsp?method=quit">退出</a></span>
</div>

<!--  头 单种类  -->

<!--  订单详情  -->
<div class="s_main_box">
<table width="100%" class="s_main_table">
 <tr>
   
     <tr>
    <td class="s_list_m">释放状态
     

    </td>

    
    <td class="s_list_m">
    <%    OrderPrintln orp = OrderPrintlnManager.getOrderStatues(user, or.getId(),opstatues); 
          boolean query = false ;
			 if(flag){
				 if(orp != null ){
					  int sta = orp.getStatues();
			          String sm = "";
			          if(0 == sta){ 
			        	  sm = "待确认";
			          }else if(1== sta){
			        	  sm = "确认中";
			          }else if(2== sta){
			        	  sm = "您的申请被同意";
			          }else if(4== sta){
			        	  sm = "您的申请被拒绝";
			        	  query = true ;
			        	  %>
			        	  <input type="submit" class="button" name="dosubmit" value="释放" onclick="winconfirm('<%=opstatues%>')"></input>
			        	  <%
			          }  
	      %> 
	     <%=sm %>
	     
	     <%
	          }else {
	        	  query = true ;
	     %>
	              无
	              <input type="submit" class="button" name="dosubmit" value="释放" onclick="winconfirm('<%=opstatues%>')"></input>
	     <%
		   }
		}
     %> 
   </td> 

  <tr>  
	
	
   
    <% 
   
    if(or.getReturnstatuse() == 0 && or.getReturnid() == user.getId() && query){

    %>
    <tr>
    <td class="s_list_m">退货</td>  
    <td class="s_list_m"> 
     <select class = "category" name="category"  id="return<%=or.getId() %>" >
     <%
       
     %> 
     <option value="1" >确认 </option> 
      </select>  
     <input type="button" onclick="change('return<%=or.getId()%>','<%=or.getId()%>','<%=Order.returns %>')"  value="确定"/>
       </td>
  <tr>
   <%
    }else if(or.getReturnstatuse() == 1){   
		%> 
		  <tr>  
    <td class="s_list_m">商品已退</td> 
    <td class="s_list_m">
                  已退货
              </td>
  <tr>          
     <%
       } 
     %>
  		
  <tr>
    <td width="25%" class="s_list_m">单号</td>
    <td class="s_list_m"><%=or.getPrintlnid() == null?"":or.getPrintlnid()%></td>
   
  </tr>  
  <%
  List<OrderProduct> lists = OrderProductManager.getOrderStatues(user, or.getId());
  for(int g = 0 ;g<lists.size();g++){
 	 OrderProduct op = lists.get(g);
      if(op.getStatues() == 0 ){

    	 String col = "";
   	      if(g%2==0){
   		
   		  col = "style='background-color:yellow'";
   	    }
   	 
    	 %>
    	  
    	 <tr <%=col %>>
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
    	 
    	<tr <%=col %>>
      
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
    <td class="s_list_m">电话</td> 
    
    <td><a href="tel:<%=or.getPhone1() %>"><%=or.getPhone1() %></a></td>
    <!--  一键拨号  -->
  </tr>
  
    <tr >
    <td class="s_list_m">预约日期</td>
    <td><%=or.getOdate() %></td>
    
  </tr>
  <tr>
    <td class="s_list_m">送货区域</td>
    <td class="s_list_m"><%=or.getLocate() %></td>
  </tr>
  
  <tr>
    <td class="s_list_m">送货地点</td>
    <td class="s_list_m"><%=or.getLocateDetail() %></td>
  </tr>
  
   <tr>
    <td class="s_list_m">送货状态</td>
    <td class="s_list_m">
    <%
   
    if(or.getReturnid() == 0 ){ 
    if(orp != null && orp.getStatues() == 0){
			
		}else { 
    if(or.getDeliveryStatues() == 0 || 9 == or.getDeliveryStatues()){
 
    %>
     <select class = "category" name="category"  id="songh<%=or.getId() %>" >
     
     <%
       
     %>
      <option value="-1" >&nbsp;&nbsp;&nbsp;&nbsp;</option> 
     <option value="2" >送货+安装 </option>  
     <option value="1" >只送货 </option>
     
      </select>  
     <input type="button" onclick="change('songh<%=or.getId()%>','<%=or.getId()%>','')"  value="确定"/>

   
   <%
    }else if(1 == or.getDeliveryStatues()  || 10 == or.getDeliveryStatues()){
           if(or.getInstallid() == user.getId()) {
        	   %>
    		   <select class = "category" name="category"  id="songh<%=or.getId() %>" >
    		        <option value="-1" >&nbsp;&nbsp;&nbsp;&nbsp;</option> 
                    <option value="4" >只安装 </option>  
               </select>  
              <input type="button" onclick="change('songh<%=or.getId()%>','<%=or.getId()%>','')"  value="确定"/>
    		<%    
           }else {    
        	   %>
    		   已送货
    		<% 
           }   
          }else if(2 == or.getDeliveryStatues()){
		%>
	    已安装 
		<%
          }
			  }
    }else { 
    	%>
    	申请退货订单
    	<%
    }
		%>
     
    </td>
 
   <tr >
    <td class="s_list_m">备注</td>
    <td align="left" class="s_list_m"><%=or.getRemark() %></td>
    
  </tr> 
  
</table>

  <br>
  <br> 
  
<span class="qiangdan"><a href="javascript:void(0);" onclick="getmap()">进入地图</a></span>
 
<!--  zong end  -->
</div>

</body>
</html>