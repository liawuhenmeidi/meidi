<%@ page language="java"
	import="java.util.*,httpClient.*,ordersgoods.*,product.*,branch.*,org.apache.commons.logging.*,utill.*,goodsreceipt.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user");  
  
String branch = request.getParameter("branch");   
String type = request.getParameter("type");

List<OrderReceipt> list = OrderReceitManager.getListAll();  
//System.out.println(list.size());
// 门店  型号  订单   
Map<String,Map<String, List<OrderReceipt>>> map = new LinkedHashMap<String, Map<String,List<OrderReceipt>>>();

if (null != list) { 
	for (int i = 0; i < list.size(); i++) {
		OrderReceipt or = list.get(i); 
		if(StringUtill.isNull(branch) || !StringUtill.isNull(branch) && branch.equals(or.getBranchName())){
	      // System.out.println("or.getBranchName()"+or.getBranchName()); 
	Map<String, List<OrderReceipt>> mapb = map.get(or.getBranchName());
            if(null == mapb){  
            	mapb = new HashMap<String, List<OrderReceipt>>();
            	map.put(or.getBranchName(), mapb);
            } 
           // System.out.println("or.getGoodsName()"+or.getGoodsName()); 
              
	//System.out.println(or.getStatuesName());
	if(!"取消".equals(or.getStatuesName())){
		 
		List<OrderReceipt> lis = mapb.get(or.getGoodsnum()); 
	if(null == lis){   
	        	lis = new ArrayList<OrderReceipt>(); 
	        	mapb.put(or.getGoodsnum(), lis);
	        } 
	         //count ++;
	lis.add(or);
        
	}  
		} 
	}  
		}
//System.out.println(count);



	//System.out.println(StringUtill.GetJson(set));
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta name="apple-mobile-web-app-capable" content="yes" />

<title>订单审核</title>

<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />
<script type="text/javascript" src="../../js/calendar.js"></script>
<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	function detail(src) {
		window.location.href = src;
	}

	function check() {
		var flag = false;

		$("input[type='checkbox'][id='check_box']").each(function() {
			if ($(this).attr("checked")) {
				var str = this.value;

				if (str != null && str != "") {
					// attract[i] = str; 
					//  i++;
					flag = true;
				}
			}
		});

		return flag;
	}
</script>
</head>
<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：退货订单</div>

		<!--  头 单种类  -->

		<table width="100%" border="0" cellspacing="1" id="Ntable">

			<tr class="dsc">
				<td align="center">门店</td>
				<td align="center">编码</td>
				<td align="center">商品编码</td>
				<td align="center">商品名称</td>
				<td align="center">退货订单号</td>
				<td align="center">订单日期</td>
				<td align="center">有效日期</td>
			</tr>
			<%
				if (null != map) {
					// System.out.println(map.size());
					Set<Map.Entry<String, Map<String, List<OrderReceipt>>>> set = map
							.entrySet();
					Iterator<Map.Entry<String, Map<String, List<OrderReceipt>>>> it = set
							.iterator();
					int allcount = 0; 
					int bcount = 0; 
					while (it.hasNext()) {
						Map.Entry<String, Map<String, List<OrderReceipt>>> mapent = it
								.next();
						//String buyid = mapent.getKey();
						String bname = mapent.getKey();
						//System.out.println(bname);
						
						Map<String, List<OrderReceipt>> or = mapent.getValue();

						if (!or.isEmpty()) {
							int bsize = or.size();
							//System.out.println("bsize"+bsize);
							Set<Map.Entry<String, List<OrderReceipt>>> setb = or
									.entrySet();
							Iterator<Map.Entry<String, List<OrderReceipt>>> itb = setb
									.iterator();
							while (itb.hasNext()) {
								
								Map.Entry<String, List<OrderReceipt>> mapb = itb
										.next();
								String typename = mapb.getKey();
								//System.out.println("typename" + typename);

								List<OrderReceipt> oas = mapb.getValue();
								//System.out.println(" oas.size()" + oas.size());					
								//	int licount = 0 ;
								//System.out.println("oas.isEmpty()" + oas.isEmpty());
								if (!oas.isEmpty()) {
									Iterator<OrderReceipt> itl = oas.iterator();
									String buyid = "";
									String time = "";
									String Recevetime = "";
									String goodname = "";
									String cl = "class=\"asc\"";

									if (oas.size() > 1) {
										cl = "class=\"bsc\"";
									}
									boolean flag = false;
									while (itl.hasNext()) {
 
										OrderReceipt oa = itl.next();

										if (oa.getRefusenum() != 0) {
											flag = true;
										}   
										allcount++;
										goodname = oa.getGoodsName();
 
										buyid += "</p>" + oa.getBuyid();
										time += "</p>" + oa.getReceveTime(); 
										Recevetime += "</p>" + oa.getActiveordertiem();
									} 
									if (flag) {
										bcount++;
										//if (bcount == 1) {
			%>
			<tr <%=cl%>
				ondblclick="detail('receiveordertypedetail.jsp?branchname=<%=bname%>&typenum=<%=typename%>')">
				<td align="center" ><%=bname%></td>
				<td align="center"><%=bcount%></td>
				<td align="center"><%=typename%></td>
				<td align="center"><%=goodname%></td>
				<td align="center"><%=buyid%></td>
				<td align="center"><%=time%></td>
				<td align="center"><%=Recevetime%></td>
			</tr>
			<%
				
			%> 

			<% 
				//} else {
			%>
			<!--  
			<tr <%=cl%>
				ondblclick="detail('receiveordertypedetail.jsp?branchname=<%=bname%>&typenum=<%=typename%>')">
				<td align="center"><%=bcount%></td>
				<td align="center"><%=typename%></td>
				<td align="center"><%=goodname%></td>
				<td align="center"><%=buyid%></td>
			</tr>
 
-->
 
			<%
				}
									}
			%>



			<%
				}

							}

						}
			%>


			<% 
			//	} 

			//		System.out.println(allcount);
				}
			%>

		</table>
	</div>

</body>
</html>