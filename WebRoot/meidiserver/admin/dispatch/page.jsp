<%@ page language="java" import="java.util.*,utill.*,order.*;" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8" %>
<%
    request.setCharacterEncoding("utf-8");
    int type = Integer.valueOf(request.getParameter("type"));
    String message = "";
    String href = request.getParameter("href");
    int count = 0;

    if (request.getParameter("count") != null && !"".equals(request.getParameter("count"))) {
        count = Integer.valueOf(request.getParameter("count"));
    }

    String pageNum = request.getParameter("page");
    String numb = request.getParameter("numb");
    String sort = request.getParameter("sort");
    String sear = request.getParameter("sear");

    int Page = Integer.valueOf(pageNum);
    int num = Integer.valueOf(numb);


    if (Order.orderDispatching == type) {
        message = "文员派工页";
    } else if (Order.charge == type) {
        message = "厂送票未结款";
    } else if (Order.come == type) {
        message = "厂送票未回";
    } else if (Order.go == type) {
        message = "厂送票未消";
    } else if (Order.dingma == type) {
        message = "调账确认页";
    } else if (Order.over == type) {
        message = "安装单位结款页";
    } else if (Order.orderPrint == type) {
        message = "文员打印页";
    } else if (Order.serach == type) {
        message = "查看订单页";
    } else if (Order.porderDispatching == type) {
        message = "网点派工";
    } else if (Order.pinstall == type) {
        message = "安装派工";
    } else if (Order.pinstallprintln == type) {
        message = "安装打印";
    } else if (Order.pserach == type) {
        message = "网点查询";
    } else if (Order.porderPrint == type) {
        message = "网点打印";
    } else if (Order.callback == type) {
        message = "客户回访";
    } else if (Order.pcharge == type) {
        message = "安装结款";
    } else if (Order.pchargepaisong == type) {
        message = "送货结款";
    } else if (Order.deliveryStatuesTuihuo == type) {
        message = "退货订单页";
    } else if (Order.chargeall == type) {
        message = "送货安装结款";
    } else if (Order.orderquery == type) {
        message = "送货确认页";
    }


    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/meidiserver/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <style type="text/css">
        td {
            align: center
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../style/css/bass.css"/>
    <script type="text/javascript">

        var num = "<%=num%>";
        var sort = "<%=sort%>";
        var href = "<%=href%>";
        var page = "<%=pageNum%>";
        var sear = "<%=sear%>";

        $(function () {

            $("select[id='numb'] option[value='" + num + "']").attr("selected", "selected");
            $("select[id='sort'] option[value='" + sort + "']").attr("selected", "selected");

            $("#page").blur(function () {
                page = $("#page").val();

                window.location.href = href + "?page=" + page + "&numb=" + num + "&sort=" + sort + "&sear=" + sear;
            });

            $("#numb").change(function () {
                num = ($("#numb").children('option:selected').val());
                // alert(num);
                window.location.href = href + "?page=" + page + "&numb=" + num + "&sort=" + sort + "&sear=" + sear;
            });

            $("#sort").change(function () {
                sort = ($("#sort").children('option:selected').val());
                window.location.href = href + "?page=" + page + "&numb=" + num + "&sort=" + sort + "&sear=" + sear;
            });
        });


    </script>
</head>
<body>
<div style="text-align:center">
    <table cellspacing="1" style="width: 95%;margin:auto">
        <tr>
            <td>
                现在位置：<%=message %>
                &nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td>
                行数
                <select class="category" name="category" id="numb">
                    <option value="100">100</option>
                    <option value="200">200</option>
                    <option value="500">500</option>
                    <option value="1000">1000</option>
                    <option value="-1">所有</option>
                </select>
            </td>
            <td>
                <a href="<%=href %>?page=1&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">首页</a>
                <a href="<%=href %>?page=<%=Page-1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">上一页</a>
                <a href="<%=href %>?page=<%=Page+1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">下一页</a>
                <a href="<%=href %>?page=<%=count/num+1%>&numb=<%=num %>&sort=<%=sort%>&sear=<%=sear%>">尾页</a>
            </td>
            <td>
                第
                <input type="text" size="5" name="username" value="<%=Page%>" id="page"/>
                页
            </td>
            <td>
                共
                <%=count %>

                条记录
            </td>
            <td>
                按
                <select class="" name="" id="sort">
                    <option value="andate asc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    <option value="andate asc">预约日期</option>
                    <option value="saledate asc">开票日期</option>
                    <option value="submittime desc">上报时间</option>
                </select>
                排序

            </td>
            <td>
                <a href="<%=basePath %>Print"><font style="color:red;font-size:20px;">导出数据</font> </a>
            </td>
        </tr>
    </table>
</div>

<hr>
</hr>
</body>
</html>
