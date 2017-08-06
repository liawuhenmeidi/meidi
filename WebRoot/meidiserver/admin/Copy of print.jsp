<%@ page language="java"
         import="java.util.*,utill.*,java.text.SimpleDateFormat,category.*,orderPrint.*,gift.*,order.*,user.*,orderproduct.*,group.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%
    request.setCharacterEncoding("utf-8");
    User user = (User) session.getAttribute("user");
    String id = request.getParameter("id");

    Order order = OrderManager.getOrderID(user, Integer.valueOf(id));


    HashMap<Integer, User> usermap = UserManager.getMap();

    User send = usermap.get(order.getDealsendId());

    HashMap<Integer, Category> categorymap = CategoryManager.getCategoryMap();

    Map<Integer, List<Gift>> gMap = GiftManager.getOrderStatuesM(user);
    Map<Integer, List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);

    SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    Date date = new Date();
    String time = df2.format(date);
    String pid = df.format(date);
    System.out.println(pid);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>打印页面</title>
    <style media=print type="text/css">
        .noprint {
            visibility: hidden
        }
    </style>
    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../css/bass.css"/>
    <link rel="stylesheet" href="../css/songhuo.css"/>
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>

    <script type="text/javascript">
        function println() {

            window.print();


        }


    </script>
</head>

<body>

<table width="1000">
    <tr>
        <td width="300">&nbsp;</td>
        <td width="384" rowspan="2" align="center" style="font-size:24px; font-family:楷体;">送货安装单</td>
        <td width="300">门店：<%=order.getBranch() %>
        </td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;单号：<%=pid + "#" %><%=order.getId() %>
        </td>
        <td><%=time %>
        </td>
    </tr>

</table>


<table width="1000" border="0" cellpadding="0" cellspacing="0" id="t">
    <tr>
        <td width="99" height="35" align="center" valign="middle" id="d">顾客名称</td>
        <td width="191" height="35" align="center" valign="middle" id="d"><%=order.getUsername() %>
        </td>
        <td width="145" height="35" align="center" valign="middle" id="d">电话</td>
        <td width="229" height="35" align="center" valign="middle" id="d"><%=order.getPhone1() %>&nbsp;</td>
        <td width="111" height="35" align="center" valign="middle" id="d">电话2</td>
        <td width="223" height="35" id="e">&nbsp;</td>
    </tr>


    <tr>
        <td width="99" height="35" align="center" valign="middle" bgcolor="#FFFFFF" id="d">地&nbsp;&nbsp;&nbsp;&nbsp; 址
        </td>
        <td height="35" colspan="3" align="center" valign="middle" bgcolor="#FFFFFF"
            id="d"><%=order.getLocateDetail() %>
        </td>
        <td width="111" height="35" align="center" valign="middle" bgcolor="#FFFFFF" id="d">预约日期</td>
        <td width="223" height="35" bgcolor="#FFFFFF" id="e">&nbsp;<%=order.getOdate() %>
        </td>
    </tr>


    <tr>
        <td height="35" colspan="6" align="center" valign="middle" bgcolor="#FFFFFF">

            <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <%
                    List<OrderProduct> listor = OrPMap.get(order.getId());
                    if (null != listor) {
                        for (int g = 0; g < listor.size(); g++) {
                            OrderProduct op = listor.get(g);
                            if (0 == op.getStatues()) {

                %>
                <tr>
                    <td width="10%" height="27" align="center" valign="middle" id="d">产品类别</td>
                    <td width="20%" height="27" align="center" id="d">
                        &nbsp;<%=categorymap.get(Integer.valueOf(op.getCategoryId())).getName() %>
                    </td>
                    <td width="10%" height="27" align="center" valign="middle" id="d">产品型号</td>
                    <td width="20%" height="27" align="center" id="d">&nbsp;<%=op.getSendType() %>
                    </td>
                    <td width="10%" height="27" align="center" valign="middle" id="d">数量</td>
                    <td width="10%" height="27" align="center" valign="middle" id="d"><%=op.getCount() %>
                    </td>
                </tr>

                <%

                            }
                        }
                    }
                %>

                <%

                    List<Gift> glists = gMap.get(Integer.valueOf(id));

                    if (null != glists) {
                        String statues = "需配送";
                        for (int g = 0; g < glists.size(); g++) {
                            Gift op = glists.get(g);
                            if (null != op && 0 == op.getStatues()) {
                %>
                <tr>
                    <td width="10%" height="27" align="center" valign="middle" id="d">赠品型号</td>
                    <td width="20%" height="27" align="center" valign="middle" id="d"><%=op.getName() %>
                    </td>
                    <td width="10%" height="27" align="center" valign="middle" id="d">状态</td>
                    <td width="10%" height="27" align="center" valign="middle" id="d"><%=statues %>
                    </td>
                    <td width="10%" height="27" align="center" valign="middle" id="d">数量</td>
                    <td width="10%" height="27" align="center" valign="middle" id="d"><%=op.getCount() %>
                    </td>

                </tr>

                <%
                            }

                        }
                    }
                %>


            </table>


        </td>
    </tr>

    <tr>
        <td width="99" height="35" align="center" valign="middle" bgcolor="#FFFFFF" id="d">派单员</td>
        <td width="191" height="35" align="center" valign="middle" bgcolor="#FFFFFF"
            id="d"><%=usermap.get(Integer.valueOf(order.getDealsendId())).getUsername() %>
        </td>
        <td width="145" height="35" align="center" valign="middle" bgcolor="#FFFFFF" id="d">送货安装员（签字）</td>
        <td height="35" colspan="3" align="center" valign="middle" bgcolor="#FFFFFF" id="d">&nbsp;</td>
    </tr>

    <tr>
        <td width="99" height="80" align="center" valign="middle" bgcolor="#FFFFFF" id="f">备注</td>
        <td height="80" colspan="5" align="right" valign="bottom" bgcolor="#FFFFFF"><%= order.getRemark()%>
            <blockquote>
                <blockquote>
                    <blockquote>
                        <blockquote>
                            <p>顾客签字：</p>
                        </blockquote>
                    </blockquote>
                </blockquote>
            </blockquote>
        </td>
    </tr>
</table>


<center><input class="noprint" type=button name='button_export' title='打印1' onclick="println()" value="打印"></input>
</center>
<p class="noprint">打印之后请修改打印状态</p>
</body>
</html>


