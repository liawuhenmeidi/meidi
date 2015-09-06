<%@ page language="java" import="java.util.*,message.*,utill.*,category.*,product.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%      
request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user");

String id = request.getParameter("id");
 
Order o = OrderManager.getOrderID(user,Integer.valueOf(id));
   
HashMap<Integer,User> usermap = UserManager.getMap();

List<OrderProduct> list = o.getOrderproduct();         
//获取二次配单元（工队）
List<User> listS = UserService.getsencondDealsend(user); 
  
List<Category> listc = CategoryManager.getCategory(user,Category.sale); 

Map<Integer,Map<Integer,OrderPrintln>> opmap = OrderPrintlnManager.getOrderStatuesMap(user);
HashMap<String,ArrayList<String>> listt = ProductManager.getProductName(); 
 
String plist = StringUtill.GetJson(listt);
Message message = MessageManager.getMessagebyoid(id); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>

<link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css" />

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


#table{  
   
    table-layout:fixed ;
} 

</style>
</head>

<body style="scoll:no">
 
<!--   头部开始   --> 

<script type="text/javascript">

</script>

<br/>  
 <a href="javascript:history.go(-1);"><font style="color:blue;font-size:20px;" >返回</font></a>   
<div id="wrap">  
<form  action="server.jsp"  method ="post"  id="form"   onsubmit="return checkedd()"  >
  
<input type="hidden" name="method" value="updateorder"/>
<input type="hidden" name="oid" value="<%=id%>"/>  
<table  cellspacing="1"  id="table"> 
       <%  
		String tdcol = " bgcolor=\"red\"" ; 
		  %>
		  
		<tr  class="asc">  
			<!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
			<td align="center">单号</td> 
			<td align="center"><%=o.getPrintlnid() == null?"":o.getPrintlnid()%></td>  
			<td align="center">门店</td>
			<td align="center"><%=o.getbranchName(o.getBranch())%></td> 
			<td align="center">销售员</td>
			<td align="center">       		  
		     <%=usermap.get(o.getSaleID()).getUsername()+"</p>"+usermap.get(o.getSaleID()).getPhone() %>
		    </td> 
		</tr> 
		<tr class="asc">	 
			<td align="center">pos(厂送)单号</td>
			<td align="center" <%=o.getPosremark()==1?tdcol:"" %>>
			     <%=o.getPos() %>
			</td>
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
		<td align="center">票面名称</td>
			<td align="center">
				
			     <%= o.getCategory(1,"")%>
			</td>
		
		    <td align="center">票面型号</td> 
			<td align="center">
			     <%=o.getSendType(1,"")%>

			</td> 
			<td align="center">票面数量</td>
			 
			<td align="center">
			     <%= o.getSendCount(1,"")%>

			</td> 
		
		</tr>
	 <% 
	 for(int i = 0 ; i<list.size();i++){
		 OrderProduct op = list.get(i);
		 if(op.getStatues() == 0){
	       
	   %>
		  
		<tr  class="asc">  
			<td align="center">送货名称</p>送货型号</td>  
			<td align="center"><%=op.getCategoryName()+"</p>"+ProductService.getIDmap().get(Integer.valueOf(op.getSendType())).getType()%></td>  
			<td align="center">上报状态</td>    
			<td align="center"><%=op.getSalestatues(op.getSalestatues())%></td> 
			<td align="center">送货数量</td>
			
			<td align="center"><%= op.getCount()%></td> 
			</tr>
      <%  }
      }%>			
		<tr class="asc">
			<td align="center">赠品</td>
			<td align="center"><%= o.getGifttype("</p>")%></td>
			<td align="center">赠品状态</td>
			<td align="center"><%= o.getGifStatues("</p>")%></td> 
			<td align="center">赠品数量</td>
			<td align="center"><%= o.getGifcount("</p>")%></td>
			</tr>
		<tr  class="asc">
			<td align="center">顾客信息</td>
			<td align="center">
			     <%=o.getUsername()  +"</p>"+
				"<p><font color=\""+tdcol+"\"> "+  
		                      o.getPhone1()
		               %>
 
		</td>
            <td align="center">开票日期</td> 
            <td align="center">
          
			     <%=o.getSaleTime() %>
            </td>  
            <td align="center">预约日期</td>
            <td align="center">  
			     <%=o.getOdate() %>
            
            </td>
           
            </tr>
		<tr  class="asc">
		    <td align="center">送货地区</td>
            <td align="center"><%=o.getLocate()%></td>
            <td align="center">送货地址</td>
            <td align="center">
			     <%=o.getLocateDetail() %> 
            </td>
           <td align="center">送货状态</td>
          <td align="center">
		<%=OrderManager.getDeliveryStatues(o) %>
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
		   if(o.getDealsendId() != 0){
			   %>
			<%=usermap.get(o.getDealsendId()).getUsername() %>
			   <% 
		   }
		 %>
		</td> 
		<td align="center">查看位置</td> 
			  <td align="center"> 
		    <a href="javascript:void(0);"  onclick="searchlocate('<%=o.getId() %>')">[查看位置]</a> 
		</td>
			</tr>
</table> 


</form>
     </div>

</body>
</html>
