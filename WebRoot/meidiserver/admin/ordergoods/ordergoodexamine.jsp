<%@ page language="java"
	import="java.util.*,ordersgoods.*,product.*,branchtype.*,enums.*,exportModel.*,org.apache.commons.logging.*,utill.*,category.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*,aftersale.*;"
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
        	var branchtype = $("#exportmodel").val();
    		if (branchtype == "" || branchtype == null) {
    			alert("请选择订单模型");
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
			 $("input[type='button'][name='button']").each(function() {
				 $(this).css("display","none");
			 });  
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
					<td colspan=6>
						<table width="100%">
							<tr>
								<%--<td>销售系统： <select id="branchtype" name="branchtype">
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
								--%>
					<td align=center>订单模型</td>
					<td align=center> 
					<select id="exportmodel" name="exportmodel">
						<option></option>
						<%  
							ExportModel.Model[] models = ExportModel.Model.values();
							int num = models.length;
							for (int i = 0; i < num; i++) {
								ExportModel.Model model = models[i];
							 	
						%> 
						
						<option value="<%=model.getValue()%>"><%=model.name()%></option>
						<%
							}
						%>
						</select>
					</td>
					<td></td>
					<td></td>
					<td></td>
								<td>产品状态：
								<table>  
							<tr>  
							<% 
							ProductSaleModel.Model[] model = ProductSaleModel.Model.values();
							for(int i=0;i<model.length;i++){
								ProductSaleModel.Model pm = model[i];
								
								%>
								<td id="statues<%=pm.getValue() %>" style="display:none"><input type="checkbox" name="statues"  value="<%=pm.getValue() %>"
								><%=pm %></td>
								  
								<%
							}  
							 
							%>				
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
					<td width="25%" class="s_list_m" align="center">备注</td>
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
									var falg = false ;
									$("#statues<%=og.getStatues()%>").css('display','block');
									
									if('<%=og.getStatues()%>' == 7 || '<%=og.getStatues()%>' == 8 || '<%=og.getStatues()%>' == 9 || '<%=og.getStatues()%>' == 10){
										flag = true ;
										
									} 
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
					<td align="center"><%=o.getOm().getRemark()%></td>
				</tr>
   
				<%
					}
					} 
				%>  
  
				<tr class="asc"> 
					<td align="center" colspan=6><input type="button" name="button" value="生成订单"
						onclick="check(0)" /> 
						<input type="button" name="button" id="buttontui" value="生成退货单" onclick="check(1)" style="display:none"/> 
						<input type="button" name="button" value="忽略"
						onclick="check(2)" />
					</td> 
				</tr>
				 
				
									
			</table>
			
			<script type="text/javascript">  
										if(flag){
											$("#buttontui").css('display','block'); 
										}
										
								
									</script>
									
		</form>

	</div>

</body>
</html>