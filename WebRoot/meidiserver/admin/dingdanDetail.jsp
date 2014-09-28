<%@ page language="java" import="java.util.*,message.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String id = request.getParameter("id");

Order o = OrderManager.getOrderID(user,Integer.valueOf(id));
   
HashMap<Integer,User> usermap = UserManager.getMap();
        
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend);   
  

Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

Message message = MessageManager.getMessagebyoid(id); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
 <script src="../js/mobiscroll.core-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.core-2.6.2-zh.js" type="text/javascript"></script>
<link href="../css/mobiscroll.core-2.6.2.css" rel="stylesheet" type="text/css" />
<script src="../js/mobiscroll.datetime-2.6.2.js" type="text/javascript"></script>
<script src="../js/mobiscroll.android-ics-2.6.2.js" type="text/javascript"></script>
 <link href="../css/mobiscroll.android-ics-2.6.2.css" rel="stylesheet" type="text/css" /> 
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript" src="../js/common.js"></script>

  
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"/>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<style type="text/css">
.fixedHead { 
position:fixed;
}  
.tabled tr td{ 
width:50px
}  


td { 
    width:200px;
    line-height:30px;
}

body{BACKGROUND-IMAGE: white;background-repeat:no-repeat";  } 
*{
    margin:0;
    padding:0;
}
#table{  
   
    table-layout:fixed ;
}

</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var id = "";

$(function () {
	var opt = { };
    opt.date = {preset : 'date'};
	$('#serviceDate').scroller('destroy').scroller($.extend(opt['date'], 
	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
	var opt2 = { };   
    opt2.date = {preset : 'date'};
	$('#serviceDate2').scroller('destroy').scroller($.extend(opt2['date'], 
	{ theme: 'android-ics light', mode: 'scroller', display: 'modal',lang: 'zh' ,startYear:'1980',endYear:'2020'}));
   
}); 
     

function addImage(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
}

function changepeidan(str1,oid,deliveryStatues){
	var uid = $("#"+str1).val();
	if(deliveryStatues != 0 ){
		if(uid == null || uid == ""){
			alert("请选择安装公司");
			return ;
		}
		question = confirm("您确定要配单并打印吗？");
		if (question != "0"){
			
			$.ajax({ 
		        type: "post", 
		         url: "server.jsp",
		         data:"method=peidan&id="+oid+"&uid="+uid,
		         dataType: "", 
		         success: function (data) { 
		            if(data == 8){
		            	alert("导购修改中。稍后重试"); 
		            }else{ 
		            	 window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues;  
		            }
		           },  
		         error: function (XMLHttpRequest, textStatus, errorThrown) { 
		            } 
		           });
		}else {
			return ;
		}
	}else { 
		$.ajax({ 
	        type: "post", 
	         url: "server.jsp",
	         data:"method=peidan&id="+oid+"&uid="+uid,
	         dataType: "", 
	         success: function (data) { 
	            if(data == 8){
	            	alert("导购修改中。稍后重试"); 
	            }else{ 
	            	window.location.href="print.jsp?id="+oid+"&deliveryStatues="+deliveryStatues;  
	            }
	           },  
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
		
		
		
		
		
		
	}
	
}
 
function changes(str1,str2,str3,str4,str5,str6,type){
	  
	if( 2 == str3 ){       
		if(0 != str4){
			if(str6 != 2 ){    
			question = confirm("商品已送货，您不能直接同意，是否联系安装公司驳回");
			if (question != "0"){
				
				if(str5 == 0){   
					alert("您已经提交"); 
				}else {
				$.ajax({     
			        type:"post",  
			         url:"../user/server.jsp",  
			         //data:"method=list_pic&page="+pageCount,       
			         data:"method=shifang&oid="+str2+"&pGroupId="+pgroup+"&opstatues="+type,
			         dataType: "",  
			         success: function (data) {    
			          alert("驳回申请已提交成功"); 
			          window.location.href="dingdan.jsp";
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			          alert("驳回申请失败");
			            } 
			           });
			}
			}
			return ;
			} 
		}
	
	if(<%=OrderPrintln.salereleaseanzhuang%> == type || <%=OrderPrintln.salereleasesonghuo%> == type || <%=OrderPrintln.release%> == type || <%=OrderPrintln.releasedispatch%> == type && 2 == str6){
	question = confirm("请先打印");
	
	if (question != "0"){
		var type = "<%=Order.deliveryStatuesTuihuo%>"; 
		window.location.href="print.jsp?id="+str2+"&type="+type ;
	}else {
		return ;
	} 
	}	 
	} 
		$.ajax({  
	        type: "post", 
	         url: "server.jsp",   
	         data:"method=dingdaned&id="+str1+"&oid="+str2+"&statues="+str3,  
	         dataType: "",  
	         success: function (data) {
	          // window.location.href="dingdan.jsp";
	           }, 
	         error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            } 
	           });
	
}  

function searchlocate(id){
	window.open("../adminmap.jsp?id="+id, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 // window.open.href="../adminmap.jsp?id="+id;
}

function checkedd(){
	//parent.location.reload(); 
	window.returnValue='refresh'; 
	//window.close();
	//window.oper.reload();
}

</script>
<div style="position:fixed;width:100%;height:100px;">
  
  <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>
      






</div >



<div style=" height:50px;">
</div>


 
<br/>  

<div id="wrap"> 
<form  action="server.jsp"  method ="post"  id="form"   onsubmit="return checkedd()"  >

<input type="hidden" name="method" value="updateorder"/>
<input type="hidden" name="oid" value="<%=id%>"/>  
<table  cellspacing="1"  id="table" style="background-color:black" > 
       <%  
		String tdcol = " bgcolor=\"red\"" ; 
		  %>
		  
		<tr  class="asc">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td> 
			<td align="center"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>  
			<td align="center">门店</td>
			<td align="center"><%=o.getBranch()%></td> 
		</tr>
		<tr class="asc">	
			<td align="center">销售员</td>
			<td align="center">       		  
		     <%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		    </td> 
		    
			<td align="center">pos(厂送)单号</td>
			<td align="center" <%=o.getPosremark()==1?tdcol:"" %>>
	          <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
			    <input type="text"  name="pos" id="pos" value="<%=o.getPos() %>"  />
			   <% }else {    
			   %>  
			     <%=o.getPos() %>
			   <%
			   }
			   %>
			</td>
			</tr>
			
		<tr  class="asc">
			<td align="center">OMS订单号</td>
			<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>>
			
			 <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
			    <input type="text"  name="sailid" id="sailid" value="<%=o.getSailId() %>"  />
			   <% }else {    
			   %>   
			     <%=o.getSailId() %>
			   <%
			   }
			   %>
			</td>
			<td align="center">验证码(联保单)</td>
			 <td align="center" <%=o.getReckedremark()==1?tdcol:"" %>>
			 
			 <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %> 
			    <input type="text"  name="check" id="chek" value="<%=o.getCheck() %>"  />
			   <% }else {    
			   %>   
			     <%=o.getCheck() %>
			   <%
			   } 
			   %>
			 
			 </td>
			  
			</tr> 
			<%if(o.getPhoneRemark()!=1){ 
			tdcol = ""; 
		} %>
		<tr class="asc">
			<td align="center">顾客信息</td>
			<td align="center">
			
			 <% if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sencondDealsend)){
	        	  %>
	        	   
	        	  <%=o.getUsername()  +"</p>" %>  
			    <input type="text"  color="<%=tdcol%>" id="phone1"  name="phone1" value="<%= o.getPhone1() %>"  />
			   <% }else {    
			   %>   
			     <%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		               %>
			   <%
			   } 
			   %>
 
		</td>
			<td align="center">送货名称</td>
			<td align="center"><%= o.getCategory(0,"</p>")%></td>
			</tr>
		<tr class="asc">
		    <td align="center">票面型号</td> 
			<td align="center">
			<% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
	        	 <input type="text" name="dingmatype" id ="dingmatype"  value="<%=o.getSendType(1,"</p>")%>"  style="width:90% "></input>
			   
			   <% }else {      
			   %>    
			     <%=o.getSendType(1,"</p>")%>
			   <%
			   } 
			   %>
			
			</td> 
			<td align="center">票面数量</td>
			 
			<td align="center"><%= o.getSendCount(1,"</p>")%></td> 
		
		</tr>	
		<tr  class="asc">
			<td align="center">送货型号</td> 
			<td align="center"><%=o.getSendType(0,"</p>")%></td> 
			<td align="center">送货数量</td>
			
			<td align="center"><%= o.getSendCount(0,"</p>")%></td> 
			</tr>
		<tr class="asc">
			<td align="center">赠品</td>
			<td align="center"><%= o.getGifttype("</p>")%></td>
			
			<td align="center">赠品数量</td>
			<td align="center"><%= o.getGifcount("</p>")%></td>
			</tr>
		<tr  class="asc">
			<td align="center">赠品状态</td>
			<td align="center"><%= o.getGifStatues("</p>")%></td> 
            <td align="center">开票日期</td> 
            <td align="center">
            <% if(UserManager.checkPermissions(user, Group.dealSend) ){
	        	  %> 
	        	 <input class="date2" type="text" name="saledate" id ="serviceDate2"  value="<%=o.getSaleTime() %>"   style="width:90% "></input>
			   
			   <% }else {      
			   %>    
			     <%=o.getSaleTime() %>
			   <%
			   } 
			   %>
            
            </td>  
            </tr>
		<tr class="asc">
            <td align="center">安装日期</td>
            <td align="center">
             <% if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sencondDealsend) ||  UserManager.checkPermissions(user, Group.sencondDealsend)){
	        	  %> 
	        	 <input class="date2" type="text" name="andate" id ="serviceDate"  value="<%=o.getOdate() %>"   style="width:90% "></input>
			   
			   <% }else {     
			   %>    
			     <%=o.getOdate() %>
			   <%
			   } 
			   %>
            
            </td>
            <td align="center">送货地区</td>
            <td align="center"><%=o.getLocate()%></td>
            </tr>
		<tr  class="asc">
            <td align="center">送货地址</td>
            <td align="center">
            
            <% if(UserManager.checkPermissions(user, Group.dealSend)){
	        	  %>
	        	 <textarea  id="locations"  name="locations" ><%=o.getLocateDetail() %></textarea> 
			   
			   <% }else {     
			   %>   
			     <%=o.getLocateDetail() %> 
			   <%
			   } 
			   %>
            </td>
           <td align="center">送货状态</td>
           <td align="center">
		<%
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == o.getDeliveryStatues()){
		%>
		 未发货
		<%
          }else if(1 == o.getDeliveryStatues()){

		%>
		已送货
		<%
          }else if(2 == o.getDeliveryStatues()){
		%>
	      已安装
		<%
          }else if(3 == o.getDeliveryStatues()){
		%>
		
		 已退货
		<%
          }
		%>
		</td>
           </tr> 
		  <tr class="asc"> 
			<td align="center">备注</td>
			  <td align="center"> 
			  <% if(UserManager.checkPermissions(user, Group.dealSend) || UserManager.checkPermissions(user, Group.sencondDealsend)){
	        	  %>  
	        	<textarea  id="remark" name="remark" ><%=o.getRemark() %></textarea>   
			  
			   <% }else {    
			   %>   
			     <%=o.getRemark() %>
			   <%
			   } 
			   %>
			  
			  
		       
		      </td>
             <td align="center">配单</td>
             <td align="center">
		 <%   
		    int flag = -1 ;
		    int flag2 = -1 ;
		    if(opmap.get(OrderPrintln.modify) != null){
				OrderPrintln op = opmap.get(OrderPrintln.modify).get(o.getId());
				
				 if(op != null){  
					 flag = op.getStatues() ;
					 }
				 }
		    
		    if(opmap.get(OrderPrintln.returns) != null){
				OrderPrintln op = opmap.get(OrderPrintln.returns).get(o.getId());
				
				 if(op != null){  
					 flag2 = op.getStatues() ;
					 }
				 }
					  
		  
		   if(o.getDealsendId() == 0){
			   if(flag != 2 && flag != 0 && flag2 != 2 && flag2 != 0){
			   %>
				<select class = "category" name="category"  id="songh<%=o.getId() %>" >
				 <option value=""></option>
				<%     if(listS != null ){
					     
			             
		               for(int j=0;j< listS.size();j++){
		            	   User u = listS.get(j);
		            	   String str = "";  
		            	   if(u.getId() == o.getDealsendId()){ 
		            		   str = "selected=selected" ;
		            	   }     
		            	   %> 
		            	    <option value=<%=u.getId() %>  <%= str%>> <%=u.getUsername() %></option>
		            	   <% 
		                    }
				     }
			            %>
		         </select> 
		      
		         <input type="button" onclick="changepeidan('songh<%=o.getId()%>','<%=o.getId()%>')"  value="确定"/> 
		         
			<% 	
		     }
		   }else {
			   %>
			    
			<%=usermap.get(o.getDealsendId()).getUsername() %>
			   
			   
			   <% 
		   }
		 %>
		</td> 
			</tr>
		<tr class="asc">
			<td align="center">查看位置</td> 
			  <td align="center"> 
		    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
		</td>
             <td align="center">安装单位驳回</td> 
             <td align="center"> 
		<% 
		int shifangstatues = -1 ;
		if(opmap.get(OrderPrintln.release) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.release).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 if(orp.getStatues() == 2){ 
			     	 
		    	 %> 
		    	  <%=orp.getMessage() %>
		    	   <p>驳回申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>驳回申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 
			 
			 
			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.release %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.release %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		 
		<%  
		if(opmap.get(OrderPrintln.salereleasesonghuo) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.salereleasesonghuo).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 if(orp.getStatues() == 2){ 
			     	
		    	 %> 
		    	   <%=orp.getMessage() %>
		    	   <p>驳回申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>驳回申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {

			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.salereleasesonghuo%>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.salereleasesonghuo %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		
		<%  
		
		if(opmap.get(OrderPrintln.salereleaseanzhuang) != null){
		OrderPrintln orp = opmap.get(OrderPrintln.salereleaseanzhuang).get(o.getId()); 
		
		 if(orp != null){
			 shifangstatues = orp.getStatues();
			 System.out.println(shifangstatues);
			 if(orp.getStatues() == 2){ 
				 
		    	 %> 
		    	   <%=orp.getMessage() %>
		    	   <p>驳回申请已同意</p>
		    	   
		    	 <%
		    	 
		    	  }else if(orp.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>驳回申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
			 
			 
			 
			 %>
		  
		 <%=orp.getMessage() %> 
		    	      
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','0','','','<%=OrderPrintln.salereleaseanzhuang %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=orp.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','0','','','<%=OrderPrintln.salereleaseanzhuang %>')"  value="不同意"/>
	
		
		<%
		   }   
		 }
		}
		%>
		</td> 
			</tr>
			<tr class="asc">
			<td align="center">导购修改申请</td>
			  <td align="center"> 
		<%
		 if(opmap.get(OrderPrintln.modify) != null){
		OrderPrintln op = opmap.get(OrderPrintln.modify).get(o.getId());
		
		 if(op != null){  
			
			 if(op.getStatues() == 2){ 
			     	
		    	 %> 
		    	
		    	   <p>导购修改中</p>  
		    	   
		    	 <%
		    	 
		    	  }else if(op.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>修改申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else {
				    		  int statues = -1 ; 		  
							    if(opmap.get(OrderPrintln.releasemodfy) != null){
									OrderPrintln oppp = opmap.get(OrderPrintln.releasemodfy).get(o.getId());
									if(oppp != null ){
										statues = oppp.getStatues() ;
									} 
							    }
			 %>
		  
		 <%=op.getMessage() %>  
		 <% if(statues == 0 || shifangstatues == 0){
			 
		  %> 
		   安装公司处理中 
		 <%}else if(statues == 2 || shifangstatues == 2){  
		 %>
		 <input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="同意"/>
	     
		<%}else if(statues == -1){ %> 
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="同意"/>
		<input type="button" onclick="changes('<%=op.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=statues %>','','<%=OrderPrintln.releasemodfy %>')"  value="不同意"/>
		<% }%>
		 
		</td>
		<%
		     
				    	  
				    	} 
		 } 
		 }
		%>
             <td align="center">导购退货申请</td> 
             
            <td align="center"> 
		<% 
		if(opmap.get(OrderPrintln.returns) != null){
		OrderPrintln op1 = opmap.get(OrderPrintln.returns).get(o.getId());
		 if(op1 != null){
			 if(op1.getStatues() == 2){ 
			     	 
		    	 %> 
		    	
		    	   <p>退货申请已同意</p>
		    	   
		    	 <% 
		    	 
		    	  }else if(op1.getStatues() == 4){ 
				     	
				    	 %> 
				    	 
				    	   <p>退货申请已拒绝</p>
				    	   
				    	 <%
				    	 
				    	  }else{
				    		  
				    int statues = -1 ;		  
				    if(opmap.get(OrderPrintln.releasedispatch) != null){
						OrderPrintln oppp = opmap.get(OrderPrintln.releasedispatch).get(o.getId());
						if(oppp != null){ 
							statues = oppp.getStatues() ;
						}

				    }
			 %>
		 <%=op1.getMessage() %>
		  <%if(statues == 0 || shifangstatues == 0){
		   %>    
		   安装公司处理中 
		  <% } else if(statues == 2 || shifangstatues == 2){%>
		  
		  <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意退货"/>
		   
		  <%}else { %>	         
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.comited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="同意"/>
		 <input type="button" onclick="changes('<%=op1.getId()%>','<%=o.getId() %>','<%=OrderPrintln.uncomited%>','<%=o.getDealsendId() %>','<%=statues %>','<%=o.getReturnstatuse() %>','<%=OrderPrintln.releasedispatch %>')"  value="不同意"/> 
		<% }%> 
		</td>

		<%
		 }
		 }
		}
		%>
			</tr>
			

</table> 

<table cellspacing="1"  id="table" style="background-color:black">  
    <tr id="th">   
    <td align="center">留言</td>
    
       <td align="center">
                <div> 
                <% 
                  if(message != null){
                %>  
                <textarea  id="remark" name="remark" ><%=message.getMessage() %></textarea> 
                <br/>
                
                <%} %> 
 <input  type="text" name="message"   style="width:90% "></input>
  <br/>
</div>
       
       </td>
          <td></td>
    <td width="100%" class="center"><input type="submit"  style="background-color:red;font-size:25px;"  value="确认修改" /></td>
   </tr> 
   </table> 

</form>
     </div>

</body>
</html>
