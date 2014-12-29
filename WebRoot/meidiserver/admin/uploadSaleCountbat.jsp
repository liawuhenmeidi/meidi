<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,utill.*,wilson.matchOrder.*,uploadtotal.*,user.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	List<String> orderNames = UploadManager.getUnTotalUploadOrdersNames();
	Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
	String type = request.getParameter("type");
	String id = request.getParameter("said");

	boolean check = false ;
	boolean total = false ;
	Map<String, HashMap<String, UploadTotal>> map = null;
	HashMap<String, UploadTotal> maptypeinit = null;
	List<UploadOrder> list = null ;  
	if("check".equals(type)){
		 list = UploadManager.getTotalUploadOrders(id); 
		 check = true ;
	}else if("total".equals(type)){
		total = true ;
		map = UploadManager.getTotalOrders(id);
	}else if("typetotal".equals(type)){
		total = true ;  
		maptypeinit = UploadManager.getTotalOrders(id,"type");
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />

<script type="text/javascript">

function amortization(src){
	window.open(src, 'abc', 'resizable:yes;dialogWidth:1200px;dialogHeight:1000px;dialogTop:0px;dialogLeft:center;scroll:no');
} 

</script>
</head>

<body>
   <form action="" method="post" id="post" onsubmit="return checkedd()">
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名称:
   <input type="hidden" name="type" id="type" value="check"/>
   <select name="said" id="said">
	<option ></option>
	<%
	for(int i = 0 ; i < orderNames.size() ; i ++){ 
		String sain  = orderNames.get(i);
		if(sain.equals(id)){
			%>
			<option value="<%=sain %>" selected="selected"><%=sain %></option>
			<%
		}else { 
			%>
			<option value="<%=sain %>" ><%=sain %></option>
			<%
		}
	} 
	%>
</select>
<input type="submit" value="查看"/>

<% if(check){ %>
<input type="button" class="button" value="设置标准" onclick="amortization('saleCountGroup.jsp')" ></input>
<input type="submit" value="型号门店统计" onclick="$('#type').val('total')"/>
<input type="submit" value="型号统计" onclick="$('#type').val('typetotal')"/>
<%} 
if(total){ %>
<input type="hidden" name="method" id="type" value="totalExport"/>
<input type="submit" value="导出" onclick="$('#type').val('totalExport');$('#post').attr('action','../Print')"/>
<%} 
%>

</form>
 
<% if(check){ %>
<table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">销售门店</td>
				<td align="center">销售日期</td>
				<td align="center">品类</td> 
				<td align="center">票面型号</td> 
				<td align="center">票面数量</td> 
				<td align="center">供价</td>
				<td align="center">扣点</td>
		</tr>

 <%
   if(null != list){
	for(int i = 0 ; i < list.size() ; i ++){ 
		UploadOrder sain  = list.get(i);
		String tpe = "";
		if(null != mapus){
			UploadSalaryModel up = mapus.get(StringUtill.getStringNocn(sain.getType()));
			if(null != up){
				tpe = up.getCatergory(); 
			}
		}
		 
	%>
	<tr class="asc" ondblclick="unconfire('<%=sain.getId()%>')" onclick="updateClass(this)"> 
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=sain.getShop() %></td>
					<td align="center"><%=sain.getSaleTime() %></td>
					<td align="center"><%=tpe %></td>
					<td align="center"><%=sain.getType() %></td> 
					<td align="center"><%=sain.getNum() %></td> 
					<td align="center"><%=sain.getSalePrice() %></td>
					<td align="center"><%=sain.getBackPoint() %></td>
	</tr>
	<%
	} 
   }
	%>


</table>
<%} %>


<% if(total){ %>
<table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">门店</td>
				<td align="center">品类</td> 
				<td align="center">型号</td> 
				<td align="center">数量</td> 
				<td align="center">销售总价</td>
				<td align="center">扣点后总价</td>
		</tr>

 <% 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   int idcount = 0 ;
   double AllTotalcount = 0 ;
   int AllCount = 0 ;
   double AllTatalbreakcount = 0 ;
   if(null != map){
   //Map<String, HashMap<String, UploadTotal>> 
   Set<Map.Entry<String, HashMap<String, UploadTotal>>> setmap = map.entrySet();
   Iterator<Map.Entry<String, HashMap<String, UploadTotal>>> itmap = setmap.iterator();
   while(itmap.hasNext()){
	   Map.Entry<String, HashMap<String, UploadTotal>> enmap = itmap.next();
	   HashMap<String, UploadTotal> maptype = enmap.getValue();
	   Set<Map.Entry<String, UploadTotal>> setmaptype =  maptype.entrySet();
	   Iterator<Map.Entry<String, UploadTotal>> itmaptype = setmaptype.iterator();
	   double Totalcount = 0 ;
	   int Count = 0 ;
	   double Tatalbreakcount = 0 ;
	   String branchname = "";
	   while(itmaptype.hasNext()){
		   Map.Entry<String, UploadTotal> enmaptype = itmaptype.next();
		   UploadTotal up = enmaptype.getValue();
		   branchname = up.getBranchname();
		   Totalcount += up.getTotalcount();
		   Count += up.getCount();
		   Tatalbreakcount += up.getTatalbreakcount();
		   AllTotalcount += up.getTotalcount();
		   AllCount += up.getCount();
		   AllTatalbreakcount += up.getTatalbreakcount();
		   idcount ++;
		   
		   String tpe = "";
			if(null != mapus){
				UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
				if(null != ups){
					tpe = ups.getCatergory(); 
				}
			}
		   
		   
		   
		   
		  %>  
		  
		  <tr class="asc"  ondblclick="unconfire('<%=up.getId()%>')" onclick="updateClass(this)"> 
					<td align="center"><%=idcount %></td>
					<td align="center"><%=up.getBranchname() %></td>
					<td align="center"><%=tpe%></td>
					<td align="center"><%=up.getType()%></td>
					<td align="center"><%=up.getCount() %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(up.getTotalcount()) %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(up.getTatalbreakcount()) %></td> 
	</tr>
		   
		 <%  
		   
	   }
	   
	   %>
	   <tr class="asc" style="color:red" ondblclick="unconfire('<%=branchname%>')" onclick="updateClass(this)"> 
					<td align="center"></td>
					<td align="center"><%=branchname %></td>
					<td align="center">总计</td>
					<td align="center"><%=Count %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(Totalcount) %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(Tatalbreakcount) %></td> 
	    </tr>
	   
	   
	   <%
      } 
  }
   if(null != maptypeinit){
	   Set<Map.Entry<String, UploadTotal>> setmaptype =  maptypeinit.entrySet();
	   Iterator<Map.Entry<String, UploadTotal>> itmaptype = setmaptype.iterator();
	   double Totalcount = 0 ;
	   int Count = 0 ;
	   double Tatalbreakcount = 0 ;
	   String branchname = "";
	   while(itmaptype.hasNext()){
		   Map.Entry<String, UploadTotal> enmaptype = itmaptype.next();
		   UploadTotal up = enmaptype.getValue();
		  // branchname = up.getBranchname();
		   Totalcount += up.getTotalcount();
		   Count += up.getCount();
		   Tatalbreakcount += up.getTatalbreakcount();
		   AllTotalcount += up.getTotalcount();
		   AllCount += up.getCount();
		   AllTatalbreakcount += up.getTatalbreakcount();
		   idcount ++; 
		   
		   String tpe = "";
			if(null != mapus){
				UploadSalaryModel ups = mapus.get(StringUtill.getStringNocn(up.getType()));
				if(null != ups){
					tpe = ups.getCatergory(); 
				}
			}
		  %>  	  
		  <tr class="asc"  ondblclick="unconfire('<%=up.getId()%>')" onclick="updateClass(this)"> 
					<td align="center"><%=idcount %></td>
					<td align="center"></td>
					<td align="center"><%=tpe%></td>
					<td align="center"><%=up.getType()%></td>
					<td align="center"><%=up.getCount() %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(up.getTotalcount()) %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(up.getTatalbreakcount()) %></td> 
	</tr>   
		 <%  
		   
	   }

   }
	%>
	 <tr class="asc" style="color:red"  onclick="updateClass(this)"> 
					<td align="center"></td>
					<td align="center">总计</td>
					<td align="center">总计</td>
					<td align="center"><%=AllCount %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(AllTotalcount) %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(AllTatalbreakcount) %></td> 
	    </tr>
</table>
<%} %>


</body>
</html>
