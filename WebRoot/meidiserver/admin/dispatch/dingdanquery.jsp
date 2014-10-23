<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
 
<%@ include file="../searchdynamic.jsp"%>
  
<%  

request.setCharacterEncoding("utf-8");

List<Order> list = OrderManager.getOrderlist(user,Group.sencondDealsend,Order.orderquery,num,Page,sort,sear);  
session.setAttribute("exportList", list); 
count =  OrderManager.getOrderlistcount(user,Group.sencondDealsend,Order.orderquery,num,Page,sort,sear);  

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单管理</title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />
<style type="text/css">
.fixedHead { 
position:fixed;
}  
.tabled tr td{ 
width:50px
}  
*{
    margin:0;
    padding:0;
}
#table{  
    width:2500px;
     table-layout:fixed ;
}
#th{
    background-color:white;
    position:absolute;
    width:2500px; 
    height:30px;
    top:0;
    left:0; 
}
#wrap{
 
    position:relative;
    padding-top:30px;
    overflow:auto;
    height:400px;
}

</style>
</head>

<body>

<script type="text/javascript" src="../../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var id = "";
var pages = "" ;
var num = "";
var oid ="<%=id%>";
var pgroup = "<%=pgroup%>"; 
var opstatues = "<%=opstatues%>";

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

function func(str){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
}
function funcc(str,str2){
    $(id).css("display","none");
	$("#"+str).css("display","block");
	id = "#"+str ;
	$.ajax({  
        type: "post", 
         url: "../server.jsp", 
         data:"method=dingdan&id="+str2,
         dataType: "", 
         success: function (data) {
            // window.location.href="senddingdan.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            }
           });
}
 
function changes(str1){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=dingdaned&id="+str1,
         dataType: "", 
         success: function (data) {
           window.location.href="dingdanpeidan2.jsp";
           }, 
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}

function change(str1,str2,type){
	var statues = $("#"+str1).val(); 
	//alert(uid);
	$.ajax({   
        type: "post",   
         url: "../../user/server.jsp", 
         data:"method=songhuo&id="+str2+"&statues="+statues+"&type="+type,
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
  	        	   window.location.href="dingdanquery.jsp";
        	 }
           
           
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           });
}



//function winconfirm(statues,uid,oid){
	//$.ajax({   
  //      type: "post",   
  //       url: "../server.jsp", 
  //       data:"method=dealshifang&statues="+statues+"&oid="+oid+"&uid="+uid,
  //       dataType: "",   
  //       success: function (date) { 
        	
    //    	 window.location.href="dingdanquery.jsp";
           
    //       },  
    //     error: function (XMLHttpRequest, textStatus, errorThrown) { 
    // 
    //        } 
    //       });
//}
 
function winconfirm(statues,uid,oid,opstatues){
	if(opstatues == 0 ){
		alert("您已经提交释放申请");
		return ;
	}else if(opstatues == 4){
		question = confirm("您的申请被拒绝，是否继续提交？");
	}else { 
		question = confirm("你确认要释放吗？");
	}
		
		if (question != "0"){
			
			//alert(attract.toString());
			$.ajax({    
		        type:"post", 
		         url:"../../user/server.jsp",   
		         //data:"method=list_pic&page="+pageCount,
		        data:"method=shifang&oid="+oid+"&pGroupId="+uid+"&opstatues="+statues,
		         dataType: "",   
		         success: function (data) {  
		          alert("释放申请已提交成功");    
		          window.location.href="dingdanquery.jsp?id="+id;
		           }, 
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		          alert("释放申请失败");
		            } 
		           }); 
		 }
	}
	
function adddetail(src){ 
	//window.location.href=src ;
	winPar=window.open(src, 'abc', 'resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:0px;dialogLeft:center;scroll:no');

	if(winPar == "refresh"){
	       window.location.reload();
    }

}

function orderPrint(id,statues){
	$.ajax({ 
        type: "post", 
         url: "../server.jsp",
         data:"method=print2&id="+id+"&statues="+statues,
         dataType: "",  
         success: function (data) {   
           window.location.href="../printPaigong.jsp?id="+id;
           },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
        // alert(errorThrown); 
            } 
           }); 
}

</script>

<div style="position:fixed;width:100%;height:20%;">
  <jsp:include flush="true" page="../head.jsp">
  <jsp:param name="" value="" />
  </jsp:include>   
      
<jsp:include flush="true" page="../page.jsp">
    <jsp:param name="sear" value="<%=sear %>" /> 
	<jsp:param name="page" value="<%=Page %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
    <jsp:param name="type" value="<%=Order.orderquery%>"/> 
</jsp:include> 

<div id="headremind">
<jsp:include page="headremind.jsp"/>
</div>

<jsp:include page="../search.jsp">
 <jsp:param name="page" value="<%=pageNum %>" />
	<jsp:param name="numb" value="<%=numb %>" />
	<jsp:param name="sort" value="<%=sort %>" />  
	<jsp:param name="count" value="<%=count %>"/> 
</jsp:include> 

</div > 
<div style=" height:170px;">
</div>
 
<br/> 

<div id="wrap">
<table  cellspacing="1" id="table">
		<tr id="th">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  
			
			<td align="center"><input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input> </td>-->
			<td align="center">单号</td>
			<td align="center">送货员</td>
			<td align="center">送货时间</td>
			<td align="center">安装员</td>
			<td align="center">安装时间</td> 
			<td align="center">释放</td>
			<td align="center">操作</td>
			<td align="center">退货</td> 
			<td align="center">送货状态</td> 
			
			<td align="center">门店</td>
			<td align="center">验证码(联保单)</td>
			<td align="center">销售员</td>
			<td align="center">顾客信息</td>
			
			<td align="center">送货名称</td>
			
			<td align="center">送货型号</td>
			<td align="center">送货数量</td>
			<td align="center">赠品</td>
			<td align="center">赠品数量</td>
			<td align="center">赠品状态</td>
			
            <td align="center">预约日期</td> 
           <td align="center">文员配单日期</td> 
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
			<td align="center">打印状态</td>
			
			<td align="center">打印</td> 
			
			<td align="center">备注</td> 
			
			<td align="center">送货是否已结款</td>
			
			<td align="center">先送货后安装</td>
			<td align="center">是否已回访</td>
            <td align="center">安装是否已结款</td>
		</tr>
	 
<tbody> 
  <% 
   if(null != list){
    for(int i = 0;i<list.size();i++){
    	Order o = list.get(i);
    	
    	String col = "";
    	if(i%2 == 0){
    		col = "style='background-color:yellow'";
    	}
  %>
   <tr id="<%=o.getId()+"ss" %>"  class="asc"  onclick="updateClass(this)"> 
		<!--  <td align="center"><input type="checkbox" value="1" name="userid[]"/></td> 
		
		<td align="center" width="20"><input type="checkbox" value="" id="check_box" name = "<%=o.getId() %>"></input></td>-->
		
		<td align="center"><a href="javascript:void(0)" onclick="adddetail('../dingdanDetail.jsp?id=<%=o.getId()%>')" > <%=o.getPrintlnid() == null?"":o.getPrintlnid()%></a></td>
		<td align="center"> 
		<% if(o.getSendId() != 0){
			if(usermap.get(Integer.valueOf(o.getSendId())) != null){
		 %>
		 <%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
		 <%
		  }
		}
		 %>
		
		</td>
		<td align="center"> 
		<%=o.getSendtime()==null?"":o.getSendtime()
		 %>
		
		</td>
		<td align="center"> 
		<% if(o.getInstallid() != 0 ){
			 if(usermap.get(o.getInstallid()) != null){
		 %> 
		 <%=usermap.get(o.getInstallid()).getUsername() %>
		 <%
			 } 
			}else if(o.getInstallid() == 0 && o.getDeliveryStatues() == 2){ 
				if(usermap.get(o.getSendId()) != null){
		 %>
		    <%=usermap.get(o.getSendId()).getUsername() %>
		 <%
				}
		  } 
		 %>
		</td>
		 <td align="center"> 
		
		<%=o.getInstalltime()==null?"":o.getInstalltime()
		 %> 
		 </td>
		 
		<td align="center">

     <%
     int statues = OrderManager.getShifangStatues(o);
     int shifang = -1 ;  
	if(statues != -1){
		
		if(opmap.get(statues) != null){
			OrderPrintln orp = opmap.get(statues).get(o.getId());    
			if(orp != null){ 
				shifang = orp.getStatues();
				if(shifang == 4 ){
				%>
				   您的申请被拒绝
				 <input type="submit" class="button" name="dosubmit" value="驳回" onclick="winconfirm('<%=statues%>','<%=user.getUsertype() %>','<%=o.getId() %>','<%=shifang%>')"></input>	
				<%
				}else if(shifang == 0){
				%> 
				您已提交
				<%
				}
			} 
		}else {
			%>
			
		<input type="submit" class="button" name="dosubmit" value="驳回" onclick="winconfirm('<%=statues%>','<%=user.getUsertype() %>','<%=o.getId() %>','<%=shifang%>')"></input>	
			<%
		}
	}
     %> 
   </td> 
		 
		<td align="center">
    <%
   int releasedispatch = OrderPrintlnManager.getstatues(opmap, OrderPrintln.releasedispatch, o.getId()) ; 
    if(o.getReturnid() == 0 && releasedispatch != 0  && shifang != 0){ 
        if(o.getDeliveryStatues() == 0 || 9 == o.getDeliveryStatues()){
 
    %>
     <select class = "category" name="category"  id="songh<%=o.getId() %>" >
     
     <%
       
     %>
     <option value="" ></option> 
     
     <option value="2" >送货+安装 </option>   
     <option value="1" >只送货 </option>
     
      </select>  
     <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>','')"  value="确定"/>

   
   <%
    }else if(1 == o.getDeliveryStatues()  || 10 == o.getDeliveryStatues()){
        	   %>
    		   <select class = "category" name="category"  id="songh<%=o.getId() %>" >
                    <option value="4" >只安装 </option>  
               </select>   
              <input type="button" onclick="change('songh<%=o.getId()%>','<%=o.getId()%>','')"  value="确定"/>
    		<%  
          }
    }
    	%>
     
    </td>
		 <% 
   
    if(o.getReturnstatuse() == 0 && o.getReturnid() != 0 ){

    %>
    <td class="s_list_m"> 
     <select class = "category" name="category"  id="return<%=o.getId() %>" >
     <%
       
     %> 
     <option value="1" >确认 </option> 
      </select>  
     <input type="button" onclick="change('return<%=o.getId()%>','<%=o.getId()%>','<%=Order.returns %>')"  value="确定"/>
       </td>
   <%
    }else if(o.getReturnstatuse() == 1){   
		%>  
    <td class="s_list_m">商品已退</td> 
   <%
   }else {
	   
  %>
  <td class="s_list_m"></td> 
   <%
   }
   %>
		<td align="center">
		<%=OrderManager.getDeliveryStatues(o.getDeliveryStatues()) %>
		</td>
		
		<td align="center"><%=o.getbranchName(o.getBranch())%></td>

		<td align="center" ><%=o.getCheck() %></td>
		<td align="center"> 		  
		<%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		</td> 
		<% 
		String tdcol = " bgcolor=\"red\"" ;
		if(o.getPhoneRemark()!=1){
			tdcol = "";
		}
		  %>   
		<td align="center"><%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		%>
		
		</td>  
		     <td align="center"><%= o.getCategory(0,"</p>")%></td>  
		  <td align="center" ><%=o.getSendType(0,"</p>")%></td>  
		  <td align="center" ><%= o.getSendCount(0,"</p>")%></td>   
		<td align="center" ><%= o.getGifttype("</p>")%></td>  
		<td align="center" ><%= o.getGifcount("</p>")%></td>  
		<td align="center" ><%= o.getGifStatues("</p>")%></td>
		
		
		<td align="center"><%=o.getOdate() %></td>
		<td align="center"><%=o.getDealSendTime() %></td>
		<td align="center"><%=o.getLocate()%></td>
		<td align="center"><%=o.getLocateDetail() %></td>
		
		<td align="center">
		  
		<%
		//打印状态     0  未打印   1 打印
		if(0 == o.getPrintSatuesP()){
		%>
		 未打印  
		<%
         }else if(1 == o.getPrintSatuesP()){
		%>
		已打印
		<%
         }
		%> 
	 	       
		</td> 
		   
		  <td align="center"> 
		   <%
		      if(o.getPrintSatuesP() == 1){

		   %>
		    <a href="javascript:void(0);" onclick="orderPrint('<%=o.getId()%>',1)">[打印]</a>
		   <%
	    	  
	      }
		   %>
		</td>
		
		
        <td align="center">   
		    <%=o.getRemark() %>
		</td>


		 <td align="center"> 
		<%=o.getStatuesPaigong() == 1 ?"是":"否"
		 %> 
		 </td>
		
	
		<td align="center"> 
		<%=o.getDeliverytype() == 2 ?"是":"否"
		 %> 
		 </td>
		<td align="center">    
		    <%=o.getStatuescallback()==0?"否":"是" %> 
		</td> 
		<td align="center">  
		    <%
		    String message = "";
		    if(o.getStatuesinstall()==0){
		    	message = "否";
		    }else if(o.getStatuesinstall()==1){
		    	message = "是";
		    }else if(o.getStatuesinstall()==2){
		    	message = "已忽略";
		    }
		       
		    %>  
		    <%=message %>  
		</td>
    </tr>
    <%}
    
    }%>
</tbody>
</table>


     </div>


</body>
</html>
