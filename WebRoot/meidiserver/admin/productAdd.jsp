<%@ page language="java"  pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%@ include file="../common.jsp"%>

<%

String categoryID = request.getParameter("categoryID");

List<String> lists = ProductService.getlist(Integer.valueOf(categoryID));
String list = StringUtill.GetJson(lists);

String method = request.getParameter("method");

String action = request.getParameter("action");
String productID = request.getParameter("productid");
String ptype = request.getParameter("ptype");
 
Product p = new Product(); 
List<Product> listp = ProductService.getlistMatain() ;
int  saletype = -1; 
if("update".equals(method)){
	p = ProductService.getIDmap().get(Integer.valueOf(productID));
	 saletype = p.getSaleType();
} 

if("add".equals(action)){  
	String productName = request.getParameter("name");
	String sizes = request.getParameter("size");
	String stockprice = request.getParameter("stockprice");
	String mataintime = request.getParameter("mataintime");
	String matainids = "";
	String matainid[] = request.getParameterValues("matainids");
	String encoded = request.getParameter("encoded");
	String saleType = request.getParameter("saleType");
	if(null != matainid && matainid.length > 0){
		for(int i=0;i<matainid.length;i++){
			matainids += matainid[i]+","; 
		}
		matainids = matainids.substring(0,matainids.length()-1);
		//System.out.println(matainids);
	}
	if(!StringUtill.isNull(productName)){   
		ProductManager.save(productName,categoryID,Double.valueOf(sizes),Double.valueOf(stockprice),mataintime,matainids,encoded,saleType);
		response.sendRedirect("product.jsp?categoryID="+categoryID);
	}    
}else if("update".equals(action)){   
	String productName = request.getParameter("name"); 
	String sizes = request.getParameter("size");
	String stockprice = request.getParameter("stockprice");
	String mataintime = request.getParameter("mataintime");
	String matainids = ""; 
	String matainid[] = request.getParameterValues("matainids");
	String encoded = request.getParameter("encoded");
	String saleType = request.getParameter("saleType");
	if(null != matainid && matainid.length > 0){
		for(int i=0;i<matainid.length;i++){
			matainids += matainid[i]+",";
		} 
		matainids = matainids.substring(0,matainids.length()-1);
		//System.out.println(matainids);
	}
	if(!StringUtill.isNull(productName)){    
		ProductManager.update(productName,productID,Double.valueOf(sizes),Double.valueOf(stockprice),mataintime,matainids,encoded,saleType);
		response.sendRedirect("product.jsp?categoryID="+categoryID);
	}
}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title> 
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css" />
<script type="text/javascript">
 
 var list = <%=list%>;
 var  saleType = "<%= saletype%>";
 $(function () {  
	$("#saleType"+saleType).attr("checked","checked");
});
 
 function check(){ 
	 
	 var productid = $("#productid").val();
	 var name = $("#name").val();
	 var size = $("#size").val(); 
	 var stockprice = $("#stockprice").val();
	 //alert(name); 
	 var count = $.inArray(name, list);
	 //alert(count);
	 if(name == "" || name == null || name == "null"){
		 alert("产品型号不能为空"); 
		 return false;
	 }else {
		 if(productid == "" || null ==productid ){
			 if(count != -1){ 
				 alert("您输入的产品已存在");
				 return false ;
			 }
		 }
		
	 } 
	 
	 if(isNaN(size)){
		 alert("体积输入不正确");
		 return false;
	 } 
	 if(isNaN(stockprice)){
		 alert("售价输入不正确");
		 return false;
	 }
	 
	 return true ;
 }
 
</script>
</head>

<body>
<!--   头部开始   -->
 <jsp:include flush="true" page="head.jsp">
  <jsp:param name="dmsn" value="" />
  </jsp:include>

<!--   头部结束   -->

     
 <!--       -->     
   <div class="weizhi_head">现在位置：类别:<%=p.getCname() %></div>  
    <div class="main_r_tianjia">
    
   <ul>  
      <li><a href="product.jsp?categoryID=<%=categoryID %>">返回</a></li>
     </ul>
   </div>  
    
     <div >   
     <form action="productAdd.jsp"  method = "post"  onsubmit="return check()">
     <input type="hidden" name="action" value="<%=method%>"/>  
      <input type="hidden" name="categoryID" value="<%=categoryID%>"/>
      <input type="hidden" id="productid" name="productid" value="<%=productID%>"/>
      <input type="hidden" id="token" name="token" value="<%=token%>"/> 
      
     <table width="80%" id="table">  
       <tr class="asc"><td align="center"> 产品型号:</td>
       <td align="center"><input type="text"  id="name" name="name"  value="<%=p.getType()%>"/> <br /> </td>
       </tr> 
     <tr class="asc"><td align="center">  产品体积:</td>
       <td align="center">  <input type="text"  id="size" name="size"  value="<%=p.getSize()%>"/>  <br />   <br /> </td>
       </tr>
       <tr class="asc"><td align="center">  最低售价:</td>
       <td align="center"> <input type="text"  id="stockprice" name="stockprice"  value="<%=p.getStockprice()%>"/>  <br />  <br /> </td>
       </tr>
       <tr class="asc"><td align="center"> 产品编码:</td> 
       <td align="center"> <input type="text"  id="encoded" name="encoded"  value="<%=p.getEncoded()%>"/>  <br />  <br /> </td>
       </tr> 
        
        <tr class="asc"><td align="center"> 销售类型:</td> 
       <td align="center">    
        常规机<input type="radio" name="saleType" id="saleType1" value="1" />
        特价机   <input type="radio" name="saleType" id="saleType2"  value="2" />
        </td>
       </tr>  
       <% if("0".equals(ptype)){ 
    	   String matainids = p.getMatainids();
    	   String[] ids= null;
    	   if(!StringUtill.isNull(matainids)){
    		   ids = matainids.split(",");
    	   }
    	      
        %> 
        <tr class="asc" >
         <td align="center">需保养配件</td>
         <td align="center">   
         <table width="100%">
        		   
         <%  if(null != listp) { 
        	// System.out.println(StringUtill.GetJson(listp));
        	  for(int i=0;i<listp.size();i++){
        		  String checked = "";
        		  Product mp = listp.get(i);
        		  if(null != ids){
        			  for(int j=0;j<ids.length;j++){
        				  if(Integer.valueOf(ids[j]) == mp.getId()){
        					  checked = "checked=\"checked\"";
        				  }
        			  }
        		  }
        		// System.out.println(StringUtill.GetJson(mp));
        		  %> 
        		  <tr> 
        		   <td ><input type="checkbox" <%=checked %> name="matainids" value ="<%=mp.getId()%>"/><%= mp.getType()%></td>
        		   </tr>
        		  
        		  <%
        	  }
         }%>
         
        		  </table>
         </td>
        </tr>
        
       <% }else if("1".equals(ptype)){
    	   
       %>  
       <tr class="asc">
          <td align="center"> 配件更新周期(天)</td>
          <td align="center"><input type="text"  id="mataintime" name="mataintime"  value="<%=p.getMataintime()%>"/></td>
      </tr>
        
       <%} %> 
     </table>

     <div >     
        <%   if(UserManager.checkPermissions(user, Group.addprodoct,"w")){ %>
    <input type="submit" value="提  交" />
     <%} %> 
     </div>
 </form>

     </div>

     </div>

</body>
</html>
