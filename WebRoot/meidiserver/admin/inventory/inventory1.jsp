<%@ page language="java"
	import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
	pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
	request.setCharacterEncoding("utf-8");
User user = (User)session.getAttribute("user"); 
String userbranch = user.getBranch(); 
String category = request.getParameter("category");
String branchid = request.getParameter("branchid");  
String branchtypeid = request.getParameter("branchtypeid");

 
//boolean flag = UserManager.checkPermissions(user, Group.ordergoods, "f"); 
Category c = CategoryManager.getCategory(category);
 
List<String> listp = ProductService.getlist(Integer.valueOf(category));

String allp = StringUtill.GetJson(listp); 

List<String> listbranchp = BranchService.getListStr(); 
String listall = StringUtill.GetJson(listbranchp);  
  
 
List<Branch> listbranch = BranchManager.getLocate(); 
List<Category> categorylist = CategoryManager.getCategory(user,Category.sale); 
  
List<BranchType> listb = BranchTypeManager.getLocate();
  

HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();

Map<String,Category> newcategorymap = new HashMap<String,Category>();
if(categorymap != null){
	Set<Integer> key = categorymap.keySet();
    for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
        int s =  it.next();
        newcategorymap.put(s+"", categorymap.get(s));
    }
	
} 

String Categorystr = StringUtill.GetJson(newcategorymap); 

 
Map<String,List<Branch>> map = BranchManager.getLocateMapBranch();  
    
String mapjosn = StringUtill.GetJson(map);
 
HashMap<String,ArrayList<String>> listt = ProductManager.getProductName();
 
String plist = StringUtill.GetJson(listt);
 
List<String> listallp = ProductManager.getProductlist();
String listallpp = StringUtill.GetJson(listallp);   
   
Map<Integer,Branch> branchmap = BranchManager.getIdMap();

Map<String,Branch> newbranchmap = new HashMap<String,Branch>();
if(branchmap != null){
	Set<Integer> key = branchmap.keySet();
    for (Iterator<Integer> it = key.iterator(); it.hasNext();) {
        int s =  it.next();
        newbranchmap.put(s+"", branchmap.get(s));
    }
	
}


String branchstr = StringUtill.GetJson(newbranchmap); 
//System.out.println(branchstr);

String inventoryid = request.getParameter("id");
Inventory inventory = null ;
String invent = ""; 
String outbranch = "";
String inbranch = "";
String remark = "";
String inittime = "";
String isdisabel = " "; 

if(!StringUtill.isNull(inventoryid)){
	isdisabel = " disabled=\"disabled\" ";
	inventory = InventoryManager.getInventoryID(user, Integer.valueOf(inventoryid));  
	
	if(branchmap != null){
		   Branch branch = branchmap.get(inventory.getOutbranchid());
	       if(branch != null){
	    	   outbranch = branch.getLocateName(); 
	       } 
	       branch = branchmap.get(inventory.getInbranchid());
	       if(branch != null){ 
	    	   inbranch = branch.getLocateName();  
	       } 
	   }
	
	remark = inventory.getRemark();
	inittime = inventory.getIntime(); 
	List<InventoryMessage> list = inventory.getInventory();
	invent = StringUtill.GetJson(list);  
    if(inventory.getInstatues() == 0 && inventory.getOutstatues() == 0){
    	isdisabel = ""; 
    }
}
 
boolean ordergoods_e= UserManager.checkPermissions(user, Group.ordergoods,"e");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
td {
	width: 100px;
	line-height: 30px;
}

#head td {
	white-space: nowrap;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>产品管理</title>



<script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>

<link rel="stylesheet" type="text/css" rev="stylesheet"
	href="../../style/css/bass.css" />
<link rel="stylesheet" href="../../css/jquery-ui.css" />
<script type="text/javascript" src="../../js/jquery-ui.js"></script>

<script type="text/javascript">
var disable = '<%=isdisabel%>';
var jsonall = <%=listall%>;
var allp = <%=allp%>;

 var jsonmap = '<%=mapjosn%>';  

 var jsons =  <%=plist%>;

 var jsonallp = <%=listallpp%>; 
 
 var row = 1; 
 var rows = new Array();
 var branchstr = <%=branchstr%>; 
 var Categorystr = <%=Categorystr%>;
 var inventoyr = '<%=invent%>';
 var winPar = null;
 var jsoninvent =  $.parseJSON(inventoyr);
  var typeid = ""; 
  var branch = "<%=branchid%>"; 
  var userbranch = "<%=userbranch%>";
    
 $(function () {  
	 $("#branch").autocomplete({ 
		 source: jsonall
	    });
	 $("#product").autocomplete({ 
		 source: allp
	    });

	 add();
     
 }); 
  
 function startRequest(ctype,branchid){ 
	 var time = $("#time").val();
	 if("fresh" == time){
		 var starttime = $("#starttime").val(); 
		 var endtime = $("#endtime").val(); 
		 window.location.href='inventoryDetail.jsp?ctype='+ctype+'&branchid='+branchid+'&starttime='+starttime+'&endtime='+endtime; 
		 $("#time").val("");
		 return ;
	 }
	
 }
 
 
 function inventory(inventory){
	 //window.open('inventorysearch.jsp?id='+inventory, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
	 window.location.href='inventorysearch.jsp?id='+inventory;
 }
  
 function search(ctype,branchid){ 
	 $("#time").val("");
	 $("#starttime").val(""); 
	 $("#endtime").val(""); 
	 winPar = window.open("time.jsp","time","resizable=yes,modal=yes,scroll=no,width=500px,top="+(screen.height-300)/2+",left="+(screen.width-400)/2+",height=400px,dialogTop:0px,scroll=no");  	
	
	 setInterval("startRequest('"+ctype+"','"+branchid+"')",500);  
 } 
   

function distri(){
 if(typeid == null || typeid == ""){
	 alert("请选择产品型号"); 
 }else { 
	 window.location.href='distribution1.jsp?category='+categoryid+'&type='+typeid;
     //window.open('distribution1.jsp?category='+categoryid+'&type='+typeid, 'abc', 'resizable:yes;dialogWidth:400px;dialogHeight:500px;dialogTop:0px;dialogLeft:center;scroll:no');
   }
 }
  
function serchclick(category,type,branchid,obj){
	 categoryid = category;
	// alert(type);
	 typeid = type ; 
	 updateClass(obj);  
 } 
 
function pandian(type,branchid){
	$.ajax({   
         type: "post", 
         url: "../server.jsp",    
         data:"method=pandian&branchid="+branchid+"&type="+type,
         dataType: "",   
         success: function (data) { 
        	 add();

            },  
         error: function (XMLHttpRequest, textStatus, errorThrown) { 
            } 
           });
}
 
 
 
 function add(){   
	 var canpandian = false ;
	 
	 $("#table tr").remove();    
	 
	 var category = "<%=category%>";  
		var b = $("#branch").val();
		var product = $("#product").val();
		//alert(branch);
		//alert(b); 
		
		//alert(b!= null);
		var counttype = $("#counttyepe").val();
		
		var chk_value =[];    
        $('input[name="typestatues"]:checked').each(function(){    
                 chk_value.push($(this).val());    
            }); 
        
		var typestatues = chk_value.toString();

		if ((branch == null || branch == "") && b != null && b != "") {
			branch = b;
		}

		if (b != null && b != "") {
			branch = b;
		}
		//alert(branch);
		// alert(b);
		$("#branch").val(branch);
		//$("#branch option[value='"+branch+"']").attr("selected","selected"); 
		//alert(branch);
		if (userbranch == branch) {
			canpandian = true;
		}

		$
				.ajax({
					type : "post",
					url : "../server.jsp",
					data : "method=inventoryall&branch=" + branch
							+ "&category=" + category + "&product=" + product
							+ "&typestatues=" + typestatues,
					dataType : "",
					success : function(data) {
						var addstr = '<tr class="dsc">'
								+ '<td align="left">产品类别</td>'
								+ '<td align="left">产品型号</td>'
								+ '<td align="left">'
								+ '<table width="100%"><tr><td align="left">安装网点：账面库存数量</td></tr><tr><td align="left">销售门店：未入库数量</td></tr></table>'
								+ '</td>' + '<td align="left">实际库存数量</td>';
						//if(flag){
						//	addstr += '<td align="left">卖场入库</td>'; 
						//}

						addstr += '<td align="left">盘点</td>' + '</tr>';
						var json = $.parseJSON(data);
						
						json.sort(     
			                     function(a, b)  
			                     {  
			                         if(a.type< b.type) return -1;  
			                         if(a.type > b.type) return 1;  
			                         return 0;   
			                     }  
			                 );
						
						for ( var i = 0; i < json.length; i++) {
							var str = json[i];
							var pandian = "是";

							if (str.isquery == false || str.isquery == "false") {
								if (canpandian) {
									pandian = '<input type="button" name="" value="盘点确认" onclick="pandian(\''
											+ str.typeid
											+ '\',\''
											+ userbranch
											+ '\')"/>';
								} else {
									pandian = "否";
								}
							} else {
								if (canpandian) {
									pandian = str.time;
								}
							}

							if (counttype != 0 || counttype == 0
									&& str.papercount != 0) {
								addstr += '<tr id="record' + row
										+ '" class="asc" ondblclick="search(\''
										+ str.typeid + '\',\'' + branch
										+ '\')"  onclick="serchclick(\''
										+ str.categoryid + '\',\'' + str.typeid
										+ '\',\'' + branch + '\',this)">' +

										' <td>' + str.cateoryName + '</td> '
										+ ' <td>' + str.type + '</td> '
										+ ' <td>' + str.papercount + '</td> ' +
										// inventoryid  
										' <td>' + str.realcount + '</td> ' +
										//  ' <td><label onclick="addlInstorage('+str.branchid+','+str.typeid+')">[点击增加卖场入库信息]</td></label> ' + 
										' <td>' + pandian + '</td> ' + ' </tr>';
								row++;
							}
						}

						$("#table").append(addstr);
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
					}
				});
 
	}
    
	function addlInstorage(branchid) {    
		//window.location.href = '../ordergoods/addlInstorage.jsp?branchid='
		//		+ branchid;      
		var branch = $("#branch").val();
		//alert(branch+<%=branchtypeid%>);    
		window.open('../ordergoods/addlInstorageFromInventory.jsp?branch='
				+ branch+'&branchtypeid=<%=branchtypeid%>'); 
	}
	 
	
	
</script>
</head>

<body> 
	<!--   头部开始   -->
	<jsp:include flush="true" page="../head.jsp">
		<jsp:param name="dmsn" value="" />
	</jsp:include>

	<!--   头部结束   -->
 
	<input type="hidden" id="time" value="" />
	<input type="hidden" id="starttime" value="" />
	<input type="hidden" id="endtime" value="" />

	<table width="100%" id="head">
		<tr> 
			<td>现在位置：库存查询</td>

			<td><a href="javascript:distri();"> 查看分布</a></td>
			<td>仓库:<input type="text" name="branch" id="branch" value="" />
               <% 
                if(ordergoods_e){  
                    %>     
                     <a href="javascript:void(0);"  onclick="addlInstorage()" target="_blank"><span style="font-size:20px;color:red;">卖场入库</span></a>  
                     
                    <% 
                    }  
                %>
               
               
               
			</td>
			<td>产品型号:<input type="text" name="product" id="product" value="" />
 
			</td> 
			<td>产品类别： 
				常规<input type="checkbox" name="typestatues" value="1" checked="checked"/> 
				特价<input
				type="checkbox" name="typestatues" value="2" checked="checked"/> 
				样机<input
				type="checkbox" name="typestatues" value="3" checked="checked"/></td>
			<td><select id="counttyepe" name="counttyepe">
					<option value="-1">全部显示</option>
					<option value=0>只显示库存不为0</option>
			</select></td>

			<td><input type="button" name="" value="查询" onclick="add()" /></td>

			<td><a href="javascript:history.go(-1);"><font
					style="color:blue;font-size:20px;">返回</font>
			</a></td>

		</tr>
        




	</table>

	<div class="table-list">

		<table width="100%" cellspacing="1" id="table">

			<thead>
				<tr>
					<th align="left">产品类别</th>
					<th align="left">产品型号</th>
					<th align="left">账面库存数量</th>
					<th align="left">实际库存数量</th>

					<th align="left">盘点</th>
				</tr>
			</thead>

		</table>


	</div>


	<br />

	<div id="serach"></div>
</body>
</html>
