<%@ page language="java"
	import="java.util.*,ordersgoods.*,product.*,branchtype.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8"); 
User user = (User)session.getAttribute("user"); 
List<BranchType> listgt = BranchTypeManager.getLocate(); 
//System.out.println(StringUtill.GetJson(listgt ));  
Map<String,OrderGoodsAll> map  = OrderGoodsAllManager.getmap(user,OrderMessage.examine); 
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
	$(function() {
		//$("#"+type).css("color","red");
	}); 
 
	function check(num) { 
		// alert(num); 
		$("#type").val(num);
		var flags = false;
		var flagid = false;
		var attract = new Array();
		var attracts = new Array();
		var i = 0; 
		var j= 0 ; 
        if(2 != num){
        	var branchtype = $("#branchtype").val();
    		if (branchtype == "" || branchtype == null) {
    			alert("请选择销售系统");
    			return false;
    		}
        }
		

		$("input[type='checkbox'][name='statues']").each(function() {
			if ($(this).attr("checked")) {
				var str = this.value;

				if (str != null && str != "") {
					attracts[i] = str;
					i++;  
					flags = true;
				}
			}
		});

		if (!flags) {
			alert("请选择产品状态");
			return false;
		}

		$("input[type='checkbox'][id='check_box']").each(function() {
			if ($(this).attr("checked")) {
				var str = this.value;

				if (str != null && str != "") {
					 attract[j] = str; 
					  j++; 
					flagid = true;
				}
			}
		});
 
		if (!flagid) {
			alert("请选择订单");
			return false;
		}
         
		if(2 == num){
			var id = attract.toString();
			 var statues = attracts.toString();
			//alert(id);  
			//alert(statues);
			 
			//return ; 
			 $.ajax({   
			        type: "post",  
			         url: "../../Print",  
			         data:"method=billing&ids="+id+"&statues="+statues+"&typestatues=2", 
			         dataType: "",   
			         success: function (data) { 
			        	 window.location.href="ordergoodexamine.jsp";  
			           },  
			         error: function (XMLHttpRequest, textStatus, errorThrown) { 
			        // alert(errorThrown); 
			            } 
			           });
			
		}else {
			if (flags && flagid) {
				//alert(1); 
				$("#post").submit();
			}
		} 
		//alert(flags && flagid);
		

	}

	function detail(src) {
		window.location.href = src;
	}
</script>
</head>

<body>
	<div class="s_main">
		<jsp:include flush="true" page="../head.jsp">
			<jsp:param name="dmsn" value="" />
		</jsp:include>
		<div class="weizhi_head">现在位置：订单生成</div>
		<!--  头 单种类  -->
		<form action="ordergoodSN.jsp" method="post" id="post">
			<input type="hidden" name="type" id="type" >
			<table width="100%" border="0" cellspacing="1" id="Ntable">
				<tr class="asc">
					<td colspan=5>
						<table width="100%">
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
								</select></td>
								<td>产品状态：
								<table>  
							<tr> 
								<td id="statues1" style="display:none"><input type="checkbox" name="statues"  value="1"
								>常规机</td>
								<td id="statues2" style="display:none"><input type="checkbox" name="statues"  value="2"
								>特价</td>
								<td id="statues3" style="display:none"><input type="checkbox" name="statues"  value="3"
								>样机</td>
								<td id="statues4" style="display:none"><input type="checkbox" name="statues"  value="4"
								>换货</td>
								<td id="statues5" style="display:none"><input type="checkbox" name="statues"  value="5"
								>赠品</td> 
								<td id="statues6" style="display:none"><input type="checkbox" name="statues"  value="6"
								>店外退货</td>
								<td id="statues7" style="display:none"><input type="checkbox" name="statues" value="7"
								>已入库常规退货</td>
								<td id="statues8" style="display:none"><input type="checkbox" name="statues"  value="8"
								>已入库特价退货</td>
								<td id="statues9" style="display:none"><input type="checkbox" name="statues"  value="9"
								>已入库样机退货</td>
							</tr>
							
							</table>
							 
							
							</td>
							</tr>
						</table></td>

				</tr>
 
				<tr class="dsc">
					<td width="5%" class="s_list_m" align="center"><input
						type="checkbox" value="" id="allselect"
						onclick="seletall(allselect)"></input>
					</td>
					<td width="20%" class="s_list_m" align="center">订单号</td>
					<td width="25%" class="s_list_m" align="center">订单时间</td>
					<td width="25%" class="s_list_m" align="center">门店</td>
					<td width="25%" class="s_list_m" align="center">导购</td>
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
							String key = en.getKey();
							List<OrderGoods> list = o.getList();
							if(null != list){
								for(int m=0;m<list.size();m++){
									OrderGoods og = list.get(m);
									
									%>
									<script type="text/javascript"> 
									$("#statues<%=og.getStatues()%>").css('display','block');
									</script>
									
									<%
								}
							}
				%>
				<tr class="asc"
					ondblclick="detail('ordergoodsdetail.jsp?id=<%=key%>&type=<%=OrderMessage.examine%>&statues=<%=OrderMessage.unexamine%>')">
					<td align="center"><input type="checkbox"
						value="<%=o.getOm().getId()%>" name="omid" id="check_box"></input>
					</td>
					<td align="center"><%=o.getOm().getOid()%></td>
					<td align="center"><%=o.getOm().getSubmittime()%></td>
					<td align="center"><%=o.getOm().getBranchname()%></td>
					<td align="center"><%=o.getOm().getUser().getUsername()%></td>
				</tr>

				<%
					}
					}
				%>

				<tr class="asc">
					<td align="center" colspan=5><input type="button" value="生成订单"
						onclick="check(0)" /> <input type="button" value="生成退货单"
						onclick="check(1)" /> <input type="button" value="忽略"
						onclick="check(2)" />
					</td>
				</tr>
			</table>
		</form>

	</div>

</body>
</html>