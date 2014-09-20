<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*,wilson.verifyCode.*" pageEncoding="GBK"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	
	String msg = "";
	Boolean confirm = false;
	String requestType = "";
	String[] searchResult = {};
	HttpClientTest hct = new HttpClientTest();;
	
	String userName = "";
	String password = "";
	String saleOrderNo = "";		
	
	requestType = request.getParameter("requestType");
	if(null != request.getParameter("msg")){
		msg = new String(request.getParameter("msg").getBytes("ISO-8859-1"),"GBK");
	}
	
	confirm = (request.getParameter("confirm") != null && request.getParameter("confirm").equals("confirm"))?true:false;
	
	if(confirm){
		userName = request.getParameter("userName");
		password = request.getParameter("password");
		saleOrderNo = request.getParameter("saleOrderNo");	
		msg = "���ڳ����У���15���Ӻ�������ϵͳ��ˢ��";
		response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
		hct.startThis(userName, password, saleOrderNo);
		return;
	}


	
	if(requestType != null && requestType.equals("search")){
		msg = "";

		userName = request.getParameter("userName");
		password = request.getParameter("password");
		saleOrderNo = request.getParameter("saleOrderNo");	
		
		if(saleOrderNo == null || saleOrderNo.equals("")){
			msg = "���۶����Ų���Ϊ��";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		
		if(!hct.login(userName, password)){
			msg = "��¼ʧ�ܣ���˶��û���������";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		if(!hct.select(saleOrderNo)){
			msg = "��ѯʧ�ܣ���˶Զ�����";
			response.sendRedirect(request.getRequestURI() + "?msg=" + URLEncoder.encode(msg));
			return;
		}
		searchResult = hct.getSdi().getSearchResult().split(",");
	}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>ǿ������ҳ</title>
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
    		<td>�û���</td>
    		<td><input type="text" name="userName" value="<%=userName.equals("")?"":userName%>"/></td>
    	</tr>
    	
    	<tr align="center">
    		<td>����</td>
    		<td><input type="password" name="password" value="<%=password.equals("")?"":password%>"/></td>
    	</tr>
    	
    	<tr>   		
    		<td>���۶������</td>
    		<td><input type="text" name="saleOrderNo" value="<%=saleOrderNo.equals("")?"":saleOrderNo%>"/></td>
    	</tr>   
    		
    	<br/>
    	
    	<%
    	if(requestType == null){ 
    	%>
    	<tr align="center">
    		<td align="center"><input type="submit" value="��ѯ"/>
    	</tr>
    	<%
    	}
    	%>
    	
    </table>
    
    <%
    if(requestType != null){
    %>
    <table align="center" border="2px">   
    	<input type="hidden" name="confirm" value="confirm"/>
    	<tr align="center">
    		<td>���۶������</td>
    		<td>���۶����к�</td>
    		<td>������Ʒ����</td>
    		<td>��Ʒ����</td>
    		<td>��������</td>
    		<td>���۶���ʱ��</td>
    		<td>�˿�Ҫ�󵽻�ʱ��</td>
    		<td>�˿�����</td>
    		<td>�˿���ϵ��ʽ</td>
    		<td>�ͻ���ַ</td>
    		<td>����״̬</td>
    		<td>������ҵ</td>
    		<td>�������</td>
    		<td>�˿��ջ�ʱ��</td>
    		<td>��������</td>
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
    		<td colspan="15" align="center"><input type="submit" value="ȷ������"/></td>
    	</tr>
    	</form>
    </table>
    <%
    }
    %>
    
    <br>
  </form>
  </body>
</html>
