<%@page import="java.net.URLEncoder"%>

<%@ page language="java" import="java.util.*,wilson.upload.*,net.sf.json.JSONObject,uploadtotalgroup.*,utill.*,wilson.matchOrder.*,uploadtotal.*,user.*,wilson.salaryCalc.*;" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<% 
	request.setCharacterEncoding("utf-8");
	User user = (User)session.getAttribute("user");
	
	String id = request.getParameter("said");
	String branch = request.getParameter("branch");
	String type = request.getParameter("type");
	String checkedStatus = request.getParameter("checkedStatus");
	String totaltype =request.getParameter("totaltype"); 
	
	Map<String,Map<String,List<UploadTotal>>> mapt =UploadManager.getTotalOrdersGroup(id,Integer.valueOf(totaltype),checkedStatus); 
	 
	Map<String,UploadSalaryModel> mapus = UploadManager.getSalaryModelsAll();
    //System.out.println(id+"**"+branch+"**"+type);
    List<UploadOrder> list = UploadManager.getTotalUploadOrders(id);
	System.out.println(list.size());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上传管理页面</title>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>  
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />


</head> 

<body>

 

<table border="1px" align="left" width="100%">
       <tr>
		        <td align="center">序号</td>
				<td align="center">销售门店</td>
				<td align="center">销售日期</td>
				<td align="center">品类</td> 
				<td align="center">票面型号</td> 
				<td align="center">单价</td>
				<td align="center">票面数量</td> 
				<td align="center">供价</td>
				<td align="center">扣点</td>
				<td align="center">扣点后单价</td>
				<td align="center">扣点后价格</td>
		</tr>

 <%
    int count = 0;
	double moneycount = 0 ;
	double bpmoneycount = 0 ;
	  
   if(null != list){   
	 for(int i = 0 ; i < list.size() ; i ++){ 
		UploadOrder sain  = list.get(i);
		if((branch.equals(sain.getShop()) || StringUtill.isNull(branch) ) && type.equals(sain.getType())){
		String tpe = ""; 
		if(null != mapus){
			UploadSalaryModel up = mapus.get(StringUtill.getStringNocn(sain.getType()));
			if(null != up){
				tpe = up.getCatergory(); 
			}
		}
		 count += sain.getNum();
		 moneycount += sain.getSalePrice();
		 bpmoneycount += sain.getSalePrice()*(1-sain.getBackPoint()/100);
		
	%>
	<tr class="asc"  onclick="updateClass(this)"> 
					<td align="center"><%=i+1 %></td>
					<td align="center"><%=sain.getShop() %></td>
					<td align="center"><%=sain.getSaleTime() %></td>
					<td align="center"><%=tpe %></td> 
					<td align="center"><%=sain.getType() %></td>  
					<td align="center"><%=DoubleUtill.getdoubleTwo(sain.getSalePrice()/sain.getNum())  %></td>
					<td align="center"><%=sain.getNum() %></td> 
					<td align="center"><%=sain.getSalePrice() %></td>
					<td align="center"><%=sain.getBackPoint() %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(sain.getSalePrice()*(1-sain.getBackPoint()/100)/sain.getNum()) %></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(sain.getSalePrice()*(1-sain.getBackPoint()/100)) %></td>
	</tr>
	<%
	} 
   }
   }
	%>
 
 <tr class="asc"  style="background:#ff7575"  onclick="updateClass(this)"> 
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"><%=count %></td> 
					<td align="center"><%=DoubleUtill.getdoubleTwo(moneycount)%></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"><%=DoubleUtill.getdoubleTwo(bpmoneycount)%></td>
	</tr>
 

</table>




</body>
</html>
