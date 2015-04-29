<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ include file="../../common.jsp"%>
<% 
	request.setCharacterEncoding("utf-8"); 
List<BranchType> listgt = BranchTypeManager.getLocate();  
String branchtype = request.getParameter("branchtype"); 
Map<String,OrderGoodsAll> map  = OrderGoodsAllManager.getsendmap(user,OrderMessage.unexamine); 
 // System.out.println(StringUtill.GetJson(map)); 
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

<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
 

$(function () {  
	//$("#"+type).css("color","red");
}); 
 
function detail(src){
	window.location.href=src;
}
  
function search(statues){
	window.location.href="maintain.jsp?statues="+statues;
}

function check(){ 
	var flag = false; 
	$("#submit").css("display","none"); 
	$("input[type='checkbox'][id='check_box']").each(function(){          
   		if($(this).attr("checked")){
   				var str = this.value; 
   				
   				if(str != null  &&  str != ""){
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
	</div>
	<div class="weizhi_head">现在位置：开单发货</div>
	<form action="ordergoodssend.jsp" method="post">
		<table>
			<tr>
				<td>销售系统： <select id="branchtype" name="branchtype">
						<option></option>
						<%
							if (null != listgt) {
								for (int i = 0; i < listgt.size(); i++) {
									BranchType bt = listgt.get(i);
									if (bt.getTypestatues() == 1) {
						%>
						<option value="<%=bt.getId()%>"><%=bt.getName()%></option>
						<%
							}
								}
							}
						%>
				</select>
				</td>
				<td><input type="submit" id="submit" value="查询" />
				</td>

			</tr>
		</table>
	</form>
	<!--  头 单种类  -->
	<form action="ordergoodsenddetail.jsp" method="post"
		onsubmit="return check()">
		<input type="hidden" name="statues" value="0"> <input
			type="hidden" name="token" value="<%=token%>" />

		<table width="100%" border="0" cellspacing="1" id="Ntable">
			<tr class="dsc">
				<td width="10%" class="s_list_m" align="center"><input
					type="checkbox" value="" id="allselect"
					onclick="seletall(allselect)"></input>
				</td>
				<td width="30%" class="s_list_m" align="center">门店</td>
				<td width="20%" class="s_list_m" align="center">导购</td>
				<td width="20%" class="s_list_m" align="center">订单时间</td>
				<td width="20%" class="s_list_m" align="center">订单截止日期</td>
			</tr>
			<%
				if (null != map) {
					Set<Map.Entry<String, OrderGoodsAll>> mapent = map.entrySet();
					Iterator<Map.Entry<String, OrderGoodsAll>> itmap = mapent
							.iterator();
					int i = 0;
					while (itmap.hasNext()) {
						Map.Entry<String, OrderGoodsAll> en = itmap.next();
						OrderGoodsAll o = en.getValue();
						String endtime = o.getList().get(0).getEffectiveendtime();
						String key = en.getKey();
						if (StringUtill.isNull(branchtype)
								|| o.getOm().getBranch().getPid() == Integer
										.valueOf(branchtype)) {
							;
			%>
			<tr class="asc"
				ondblclick="detail('ordergoodsdetail.jsp?id=<%=key%>&type=<%=OrderMessage.billing%>&statues=<%=OrderMessage.unexamine%>')">
				<td align="center"><input type="checkbox"
					value="<%=o.getOm().getId()%>" name="omid" id="check_box"></input>
				</td>
				<td align="center"><%=o.getOm().getBranchname()%></td>
				<td align="center"><%=o.getOm().getUser().getUsername()%></td>
				<td align="center"><%=o.getOm().getSubmittime()%></td>
				<td align="center"><%=StringUtill.getNotNUll(endtime)%></td>

			</tr>

			<%
				}
					}
				}
			%>

			<tr class="asc">
				<td align="center" colspan=5><input type="submit" id="submit"
					value="开单发货" />
				</td>
			</tr>
		</table>
	</form>


</body>
</html>