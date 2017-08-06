<%@ page language="java"
         import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%

    request.setCharacterEncoding("utf-8");
    User user = (User) session.getAttribute("user");
    int id = user.getId();
    String serchProperty = "";
    String pageNum = request.getParameter("page");
    String numb = request.getParameter("numb");
    String sort = request.getParameter("sort");
    if (StringUtill.isNull(pageNum)) {
        pageNum = "1";
    }
    if (StringUtill.isNull(numb)) {
        numb = "100";
    }
    if (StringUtill.isNull(sort)) {
        sort = "id";
    }
    int count = 0;
    int Page = Integer.valueOf(pageNum);


    int num = Integer.valueOf(numb);
    if (Page <= 0) {
        Page = 1;
    }
    List<Order> list = null;
//String sear = (String)session.getAttribute("sear");
//if(StringUtill.isNull(sear)){ 
//	sear = ""; 
//}
    String sear = "";
    String searched = request.getParameter("searched");
    if ("searched".equals(searched)) {

        String[] search = request.getParameterValues("search");
        if (search != null) {
            for (int i = 0; i < search.length; i++) {
                String str = search[i];

                boolean fflag = false;
                if ("saledate".equals(str) || "andate".equals(str)) {
                    String start = request.getParameter(str + "start");
                    String end = request.getParameter(str + "end");
                    boolean flag = false;
                    if (start != null && start != "" && start != "null") {
                        sear += " and " + str + "  BETWEEN '" + start + "'  and  ";
                        flag = true;
                    }
                    if (end != null && end != "" && end != "null") {
                        sear += " '" + end + "'";
                    } else if (flag) {
                        sear += "now()";
                    }
                } else if ("categoryname".equals(str) || "sendtype".equals(str) || "saletype".equals(str)) {
                    String strr = request.getParameter(str);
                    if (strr != "" && strr != null) {
                        sear += " and id in (select orderid  from mdorderproduct where " + str + " like '%" + strr + "%')";
                    }  // giftName
                } else if ("giftName".equals(str) || "statues".equals(str)) {
                    String strr = request.getParameter(str);
                    if (strr != "" && strr != null) {
                        sear += " and id in (select orderid  from mdordergift where " + str + " like '%" + strr + "%')";
                    }  // giftName
                } else if ("dealSendid".equals(str) || "saleID".equals(str) || "sendId".equals(str)) {
                    String strr = request.getParameter(str);
                    if (strr != "" && strr != null) {
                        sear += " and " + str + " in (select id from mduser  where username like '%" + strr + "%')";
                    }
                } else {
                    String strr = request.getParameter(str);
                    if (strr != "" && strr != null) {
                        sear += " and " + str + " like '%" + strr + "%'";
                    }
                }
            }
        } else {
            sear = "";
        }

        //session.setAttribute("sear", sear);

    }


//list = OrderManager.getOrderlistPrintlnSend(user,Group.sencondDealsend,Integer.valueOf(num),Page,sort);  
    list = OrderManager.getOrderlist(user, Group.sencondDealsend, Order.porderPrint, num, Page, sort, "");
    count = OrderManager.getOrderlistcount(user, Group.sencondDealsend, Order.porderPrint, num, Page, sort, "");
    Map<Integer, List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
    HashMap<Integer, User> usermap = UserManager.getMap();
//获取送货员    
    Map<Integer, List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
    List<User> listS = UserManager.getUsers(user, Group.send);

    HashMap<Integer, Category> categorymap = CategoryManager.getCategoryMap();
    Map<Integer, OrderPrintln> opMap = OrderPrintlnManager.getOrderStatues(user, 3);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>订单管理</title>

    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css"/>
    <style type="text/css">
        .fixedHead {
            position: fixed;
        }

        .tabled tr td {
            width: 50px
        }

        * {
            margin: 0;
            padding: 0;
        }

        td {
            width: 100px;
            line-height: 30px;
        }

        #table {
            width: 1400px;
            table-layout: fixed;
        }

        #th {
            background-color: #888888;
            position: absolute;
            width: 1400px;
            height: 30px;
            top: 0;
            left: 0;
        }

        #wrap {

            position: relative;
            padding-top: 30px;
            overflow: auto;
            height: 450px;
        }

    </style>
</head>

<body>

<script type="text/javascript" src="../js/common.js"></script>
<!--   头部开始   -->
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
    var id = "";
    var pages = "<%=Page%>";
    var num = "<%=num%>";

    $(function () {
        $("#wrap").bind("scroll", function () {

            if (pre_scrollTop != ($("#wrap").scrollTop() || document.body.scrollTop)) {
                //滚动了竖直滚动条
                pre_scrollTop = ($("#wrap").scrollTop() || document.body.scrollTop);

                if (obj_th) {
                    obj_th.style.top = ($("#wrap").scrollTop() || document.body.scrollTop) + "px";
                }
            }
            else if (pre_scrollLeft != (document.documentElement.scrollLeft || document.body.scrollLeft)) {
                //滚动了水平滚动条
                pre_scrollLeft = (document.documentElement.scrollLeft || document.body.scrollLeft);
            }
        });
        $("select[id='numb'] option[value='" + num + "']").attr("selected", "selected");

        $("#page").blur(function () {
            pages = $("#page").val();
            window.location.href = "dingdanprint2.jsp?pages=" + pages + "&numb=" + num;
        });

        // $("#search").blur(function(){

        // var search = $("#search").val();
        // var serchProperty = $("#serchProperty").val();

        // window.location.href="dingdan.jsp?search="+search+"&serchProperty="+serchProperty;
        // });
        $("#numb").change(function () {
            num = ($("#numb").children('option:selected').val());
            // alert(num);
            window.location.href = "dingdanprint2.jsp?pages=" + pages + "&numb=" + num;
        });

        $("#sort").change(function () {
            sort = ($("#sort").children('option:selected').val());
            // alert(num);
            window.location.href = "dingdanprintl2.jsp?page=" + pages + "&numb=" + num + "&sort=" + sort;
        });
    });
    function func(str) {
        $(id).css("display", "none");
        $("#" + str).css("display", "block");
        id = "#" + str;
    }
    function funcc(str, str2) {
        $(id).css("display", "none");
        $("#" + str).css("display", "block");
        id = "#" + str;
        $.ajax({
            type: "post",
            url: "server.jsp",
            data: "method=dingdan&id=" + str2,
            dataType: "",
            success: function (data) {
                // window.location.href="senddingdan.jsp";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // alert(errorThrown);
            }
        });
    }

    function changes(oid, id) {
        $.ajax({
            type: "post",
            url: "server.jsp",
            data: "method=dingdaned&oid=" + oid + "&id=" + id,
            dataType: "",
            success: function (data) {
                window.location.href = "dingdanpeidan2.jsp";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // alert(errorThrown);
            }
        });
    }
    function change(str1, str2) {
        var uid = $("#" + str1).val();
        $.ajax({
            type: "post",
            url: "server.jsp",
            data: "method=peidan&id=" + str2 + "&uid=" + uid,
            dataType: "",
            success: function (data) {
                alert("设置成功");
                window.location.href = "dingdanpeidan2.jsp";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // alert(errorThrown);
            }
        });

    }

    function orderPrint(id, statues, type) {
        $.ajax({
            type: "post",
            url: "server.jsp",
            data: "method=print2&id=" + id + "&statues=" + statues,
            dataType: "",
            success: function (data) {
                window.location.href = "dingdanprint2anzhuang.jsp";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                // alert(errorThrown);
            }
        });
    }


</script>

<div style="position:fixed;width:100%;height:200px;">

    <jsp:include flush="true" page="head.jsp">
        <jsp:param name="dmsn" value=""/>
    </jsp:include>

    <jsp:include flush="true" page="page.jsp">
        <jsp:param name="page" value="<%=pageNum %>"/>
        <jsp:param name="numb" value="<%=numb %>"/>
        <jsp:param name="sort" value="<%=sort %>"/>
        <jsp:param name="count" value="<%=count %>"/>
        <jsp:param name="type" value="<%=Order.pinstallprintln%>"/>
    </jsp:include>

    <jsp:include page="search.jsp"/>
</div>

<div style=" height:150px;">
</div>


<br/>

<div id="wrap">
    <table cellspacing="1" id="table">
        <tr id="th">
            <!--  <td align="center" width=""><input type="checkbox" value="" id="check_box" onclick="selectall('userid[]');"/></td>  -->
            <td align="center">单号</td>
            <td align="center">门店</td>
            <td align="center">顾客姓名</td>
            <td align="center">电话</td>
            <td align="center">送货名称</td>


            <td align="center">安装日期</td>
            <td align="center">送货地区</td>
            <td align="center">送货地址</td>
            <td align="center">送货状态</td>
            <td align="center">打印状态</td>

            <td align="center">送货人员</td>
            <td align="center">安装人员</td>
            <td align="center">备注</td>
            <td align="center">安装单打印</td>


            <!--  <td align="center">修改申请</td> -->
        </tr>

        <tbody>
        <%
            if (null != list) {
                for (int i = 0; i < list.size(); i++) {
                    Order o = list.get(i);

                    String col = "";
                    if (i % 2 == 0) {
                        col = "style='background-color:yellow'";
                    }
        %>
        <tr id="<%=o.getId()+"ss" %>" class="asc" onclick="updateClass(this)">
            <!--  <td align="center"><input type="checkbox" value="1" name="userid[]"/></td> -->
            <td align="center"><%=o.getPrintlnid() == null ? "" : o.getPrintlnid()%>
            </td>
            <td align="center"><%=o.getBranch()%>
            </td>

            <td align="center"><%=o.getUsername() %>
            </td>
            <td align="center"><%=o.getPhone1()%>
            </td>
            <%
                String pcategory = "";
                String scategory = "";
                String ptype = "";
                String stype = "";
                String pcountt = "";
                String scountt = "";

                List<OrderProduct> lists = OrPMap.get(o.getId());
                if (lists != null) {
                    for (int g = 0; g < lists.size(); g++) {
                        OrderProduct op = lists.get(g);
                        if (op.getStatues() == 1) {
                            pcategory = categorymap.get(Integer.valueOf(op.getCategoryId())).getName() + "</p>";
                            pcountt += op.getCount() + "</p>";
                            ptype += op.getSaleType() == null || op.getSaleType() == "null" ? "" : op.getSaleType() + "</p>";
                        } else {
                            scategory = categorymap.get(Integer.valueOf(op.getCategoryId())).getName() + "</p>";
                            scountt += op.getCount() + "</p>";
                            stype += op.getSendType() == null || op.getSendType() == "null" ? "" : op.getSendType() + "</p>";
                        }
                    }
                }
            %>


            <td align="center"><%=scategory%>
            </td>

            <td align="center"><%=o.getOdate() %>
            </td>
            <td align="center"><%=o.getLocate()%>
            </td>
            <td align="center"><%=o.getLocateDetail() %>
            </td>
            <td align="center">
                <%
                    // 0 表示未送货  1 表示正在送  2 送货成功
                    if (0 == o.getDeliveryStatues()) {
                %>
                未发货
                <%
                } else if (1 == o.getDeliveryStatues()) {

                %>
                已送货
                <%
                } else if (2 == o.getDeliveryStatues()) {
                %>
                已安装
                <%
                } else if (3 == o.getDeliveryStatues()) {
                %>

                已退货
                <%
                    }
                %>
            </td>
            <td align="center">

                <%
                    //打印状态     0  未打印   1 打印
                    if (0 == o.getPrintSatuesP()) {
                %>
                未打印
                <%
                } else if (1 == o.getPrintSatuesP()) {

                %>
                已打印
                <%
                    }
                %>


            </td>

            <td align="center"><%=usermap.get(Integer.valueOf(o.getSendId())).getUsername() %>
            </td>
            <td align="center">
                <% if (o.getInstallid() != 0) {
                %>
                <%=usermap.get(Integer.valueOf(o.getInstallid())).getUsername() %>
                <%
                    }
                %>

            </td>

            <td align="center">
                <%=o.getRemark() %>
            </td>

            <td align="center">
                <%
                    if (o.getInstallid() != 0) {
                %>
                <a href="javascript:void(0);"
                   onclick="orderPrint('<%=o.getId()%>',1,'<%= Order.orderinstall %>')">[打印]</a>
                <%
                    }%>
            </td>


            <!--
		<%
		OrderPrintln opp = opMap.get(o.getId());
		if(opp!= null){

		%>
		<td align="center"><span style="cursor:hand" onclick="funcc('ddiv<%=o.getId() %>',<%=o.getId() %>)"></span>
		 <div id="ddiv<%=o.getId()%>"  style= "" >
		  
		    
		    	 <%
		    	  if(opp.getStatues() == 2){
		    	
		    	 %> 
		    	
		    	   <p>修改申请已处理</p>
		    	   
		    	 <%
		    	 
		    	  }else {
		    	      %>
		    	     
		    	     <%=opp.getMessage() %>
		    	    
		    	     <input type="button" onclick="changes('<%=o.getId()%>','<%=opp.getId() %>')"  value="确定"/>
		    	     <%
		    	  }
		    	     %>
		   
		   
		 </div>
		</td>
		
		<%
		}else {
			
			%>	
		
		<td align="center"></td>
		
		<%
		}
		%>
		-->

        </tr>
        <%
                }

            }
        %>
        </tbody>
    </table>


</div>


</body>
</html>
