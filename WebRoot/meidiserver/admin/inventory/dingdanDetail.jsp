<%@ page language="java" import="java.util.*,message.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      

request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String id = request.getParameter("id");

Order o = OrderManager.getOrderID(user,Integer.valueOf(id));
  
HashMap<Integer,User> usermap = UserManager.getMap();
        
//获取二次配单元（工队）
List<User> listS = UserManager.getUsers(user ,Group.sencondDealsend);   
  
HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
 
Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
Map<Integer,List<Gift>> gMap = GiftManager.getOrderStatuesM(user);


Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);

Message message = MessageManager.getMessagebyoid(id); 

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文员派工页</title>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

<script type="text/javascript" src="../../js/common.js"></script>

</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">
var id = "";

$(function () {
	
}); 
     

function addImage(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
}


 
 

function checkedd(){
	//parent.location.reload(); 
	window.returnValue='refresh'; 
	//window.close();
	//window.oper.reload();
}

</script>

<br/>   

 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>       
 
<table  cellspacing="1"  id="table" style="width:100%;height:100px;"> 
       <%    
		String tdcol = " bgcolor=\"red\"" ; 
       
	     String pcategory = "";
	     String scategory = "";
	     String ptype = "";
	     String stype = "";
	     String pcountt = "";
	     String scountt = "";
	     List<OrderProduct> lists = OrPMap.get(o.getId());
	     if(lists != null ){
		     for(int g = 0 ;g<lists.size();g++){
		    	 OrderProduct op = lists.get(g);
		    	 if(op.getStatues() == 1 ){
		    		 pcategory =  categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
			         pcountt += op.getCount() +"</p>";
			         ptype += op.getSaleType()==null ||op.getSaleType() == "null" ? "":op.getSaleType() +"</p>";
		    	 }else {
		    		 scategory += categorymap.get(Integer.valueOf(op.getCategoryId())).getName()+"</p>";
			         scountt += op.getCount() +"</p>";
			         stype += op.getSendType()==null ||op.getSendType() == "null" ? "":op.getSendType() +"</p>"; 
		    	 }  
		     }
	     }
	     
	     
	     String gstatues = "";
	     String gtype = "";
	     String gcountt = ""; 
	     
	     List<Gift> glists = gMap.get(o.getId());
	     
	     if(null != glists){
	
	     for(int g = 0 ;g<glists.size();g++){
	    	 
	    	 Gift op = glists.get(g);
	    	 if(null !=op ){
	    		 gtype += op.getName()+"</p>";
		         gcountt += op.getCount()+"</p>";
		         String statues = "";
		         if(0==op.getStatues()){
		        	 statues = "需配送";
		         }else if(1 == op.getStatues()) {
		        	 statues = "已自提";
		         }else if(9 == op.getStatues()) { 
		        	 statues = "只安装(门店提货)";
		         }else if(10 == op.getStatues()) {
		        	 statues = "只安装(顾客已提)";
		         } 
		         gstatues += statues +"</p>";
	    	 }
	     }
	     }
	     
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
	        
			     <%=o.getPos() %>
			  
			</td>
			</tr>
			
		<tr  class="asc">
			<td align="center">OMS订单号</td>
			<td align="center" <%=o.getSailidrecked()==1?tdcol:"" %>>
			
		  
			     <%=o.getSailId() %>
			   
			</td>
			<td align="center">验证码(联保单)</td>
			 <td align="center" <%=o.getReckedremark()==1?tdcol:"" %>>
			 
			 
			     <%=o.getCheck() %>
			 
			 
			 </td>
			  
			</tr> 
			<%if(o.getPhoneRemark()!=1){ 
			tdcol = ""; 
		} %>
		<tr class="asc">
			<td align="center">顾客信息</td>
			<td align="center">
			
			
			     <%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		               %>
			 
 
		</td>
			<td align="center">送货名称</td>
			<td align="center"><%=scategory%></td>
			</tr>
		<tr class="asc">
		    <td align="center">票面型号</td> 
			<td align="center">
			  
			     <%=ptype%>
			  
			
			</td> 
			<td align="center">票面数量</td>
			 
			<td align="center"><%=pcountt%></td> 
		
		</tr>	
		<tr  class="asc">
			<td align="center">送货型号</td> 
			<td align="center"><%=stype%></td> 
			<td align="center">送货数量</td>
			
			<td align="center"><%=scountt%></td> 
			</tr>
		<tr class="asc">
			<td align="center">赠品</td>
			<td align="center"><%=gtype%></td>
			
			<td align="center">赠品数量</td>
			<td align="center"><%=gcountt%></td>
			</tr>
		<tr  class="asc">
			<td align="center">赠品状态</td>
			<td align="center"><%=gstatues%></td> 
            <td align="center">开票日期</td> 
            <td align="center">
           
			     <%=o.getSaleTime() %>
			 
            
            </td>  
            </tr>
		<tr class="asc">
            <td align="center">安装日期</td>
            <td align="center">
              
			     <%=o.getOdate() %>
			 
            
            </td>
            <td align="center">送货地区</td>
            <td align="center"><%=o.getLocate()%></td>
            </tr>
		<tr  class="asc">
            <td align="center">送货地址</td>
            <td align="center">
         
			     <%=o.getLocateDetail() %> 
			
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
			 
			     <%=o.getRemark() %>
			 
			  
			  
		       
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
		 
		<%}else if(statues == -1){ %> 
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
		<% }%> 
		</td>

		<%
		 }
		 }
		} 
		%>
			</tr>
			

</table> 

</body>
</html>
