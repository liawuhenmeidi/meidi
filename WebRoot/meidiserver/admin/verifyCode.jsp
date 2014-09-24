<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*,wilson.verifyCode.*,utill.*" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setCharacterEncoding("utf-8");
	 
	String msg = "";
	Boolean confirm = false;
	String requestType = "";
	String[] searchResult = {};
	MainClient mc = new MainClient();
	VerifyCodeManager vcm = new VerifyCodeManager();
	String resultString = vcm.getDoingVerifyCode();
	
	String userName = "";
	String password = ""; 
	String saleOrderNo = "";		
	
	requestType = request.getParameter("requestType");

	confirm = (request.getParameter("confirm") != null && request.getParameter("confirm").equals("confirm"))?true:false;

	
	if(confirm){
		userName = request.getParameter("userName");
		password = request.getParameter("password");
		userName = "1005949101"; 
		password = "26556199sn"; 
		saleOrderNo = request.getParameter("saleOrderNo");	
		msg = "正在尝试中，请15分钟后在苏宁系统中刷新";

		mc.setUserName(userName);
		mc.setPassword(password);
		mc.setSaleOrderNo(saleOrderNo);
		mc.start();

		response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));

		return;
	}


	if(requestType != null && requestType.equals("search")){
		msg = "";
		userName = request.getParameter("userName");
		password = request.getParameter("password"); 
        
        if(StringUtill.isNull(userName) && StringUtill.isNull(password)){
        	userName = (String)session.getAttribute("snuserName");
            password = (String)session.getAttribute("snpassword"); 
        }
		    
		if(!StringUtill.isNull(userName) && !StringUtill.isNull(password)){
			session.setAttribute("snuserName",userName);
			session.setAttribute("snpassword", password);
		}else {
			out.write("请输入用户名密码");
		}
		
		
		userName = "1005949101"; 
		password = "26556199sn"; 
		
		saleOrderNo = request.getParameter("saleOrderNo");	
		
		if(saleOrderNo == null || saleOrderNo.equals("")){
			msg = "销售订单号不能为空";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		
		if(!mc.login(userName, password)){
			msg = "登录失败，请核对用户名，密码";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		if(!mc.select(saleOrderNo)){
			msg = "查询失败，请核对订单号";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		searchResult = mc.getSdi().getSearchResult().split(",");
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>强制消单页</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
  <form action="" method="post">
    <table align="center">
    	
    	<input type="hidden" name="requestType" value="search"/>
    	
    	<%
    	if(!msg.equals("")){
    		
    	%>
    	<tr>
    		<td align="center" colspan="15" style="color:red"><%=msg%></td>
    	</tr>
    	<%
    	}
    	%>
    	<tr align="center">
    		<td>用户名</td>
    		<td><input type="text" name="userName" value="<%=userName.equals("")?"":userName%>"/></td>
    	</tr>
    	
    	<tr align="center">
    		<td>密码</td>
    		<td><input type="password" name="password" value="<%=password.equals("")?"":password%>"/></td>
    	</tr>
    	
    	<tr>   		
    		<td>销售订单编号</td>
    		<td><input type="text" name="saleOrderNo" value="<%=saleOrderNo.equals("")?"":saleOrderNo%>"/></td>
    	</tr>   
    		
    	<br/>
    	
    	<%
    	if(requestType == null || requestType.equals("")){ 
    	%>
    	<tr align="center">
    		<td align="center"><input type="submit" value="查询"/>
    	</tr>
    	<%
    	}
    	%>
    	
    </table>
    
    <%
    if(requestType != null && requestType.equals("search")){
    %>
    <table align="center" border="2px">   
    	<input type="hidden" name="confirm" value="confirm"/>
    	<tr align="center">
    		<td>销售订单编号</td>
    		<td>销售订单行号</td>
    		<td>苏宁商品编码</td>
    		<td>商品名称</td>
    		<td>销售数量</td>
    		<td>销售订单时间</td>
    		<td>顾客要求到货时间</td>
    		<td>顾客姓名</td>
    		<td>顾客联系方式</td>
    		<td>送货地址</td>
    		<td>订单状态</td>
    		<td>发货作业</td>
    		<td>快递链接</td>
    		<td>顾客收货时间</td>
    		<td>销售渠道</td>
    	</tr>
    	
    	<tr align="center">
    		<%
    		for(int i = 0 ; i < searchResult.length ; i ++){
    		%>
    		<td>
    		<%=searchResult[i] %>
    		</td>
    		<%
    		}
    		%>
    	</tr>     		
    	<br/>
    	<tr>
    		<td colspan="15" align="center"><input type="submit" value="确认消单"/></td>
    	</tr>
    	</form>
    </table>
    <%
    }  
    %>
      
    <br>
  </form>
  <table>
  
  	<tr>
  		<td>正在消单中的单据有:</td>
  	</tr>
  	
  	<tr>
  		<td><%=resultString %></td>
  	</tr>
  	
  </table>
  </body>
</html>
