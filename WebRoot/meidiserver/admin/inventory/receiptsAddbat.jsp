<%@ page language="java"
         import="java.util.*,utill.*,product.*,inventory.*,orderproduct.*,branch.*,branchtype.*,grouptype.*,category.*,group.*,user.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%
    request.setCharacterEncoding("utf-8");
    User user = (User) session.getAttribute("user");

    List<Category> categorylist = CategoryManager.getCategory(user, Category.sale);

    List<BranchType> listb = BranchTypeManager.getLocate();

    Map<String, List<Branch>> map = BranchManager.getLocateMapBranch();

    String mapjosn = StringUtill.GetJson(map);

    HashMap<String, ArrayList<String>> listt = ProductManager.getProductName();

    String plist = StringUtill.GetJson(listt);

    String action = request.getParameter("action");
    if ("add".equals(action)) {
        Inventory inventory = new Inventory();
        String time = TimeUtill.gettime();
        int uid = user.getId();
        String branchid = request.getParameter("branch");
        inventory.setBranchid(Integer.valueOf(branchid));
        String[] producs = request.getParameterValues("product");
        for (int i = 0; i < producs.length; i++) {


        }


        String productName = request.getParameter("name");
        if (!StringUtill.isNull(productName)) {
            //response.sendRedirect("product.jsp");
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <style type="text/css">

        td {
            width: 100px;
            line-height: 30px;
        }


    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>产品管理</title>
    <script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css"/>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

    <script type="text/javascript">

        var jsonmap = '<%=mapjosn%>';
        var availableTags = '<%=plist%>';
        var jsons = $.parseJSON(availableTags);
        var row = 1;
        var rows = new Array();

        $(function () {
            $("#branchtype").change(function () {
                $("#branch").html("");
                var num = ($("#branchtype").children('option:selected').val());
                var jsons = $.parseJSON(jsonmap);
                var json = jsons[num];
                //alert(json);
                var options = '<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
                for (var i = 0; i < json.length; i++) {
                    options += "<option value='" + json[i].id + "'>" + json[i].locateName + "</option>";
                }
                $("#branch").html(options);
            });

            $("#branch").change(function () {
                var str = $("#branch").find("option:selected").text();
                $("#branchmessage").html(str + "入库单");
            });

            initproductSerch("#ordercategory0", "#ordertype0");
        });


        function initproductSerch(str, str2) {
            cid = $(str).val();

            $(str2).autocomplete({
                source: jsons[cid]
            });
            $(str).change(function () {
                $(str2).val("");
                cid = $(str).val();
                $(str2).autocomplete({
                    source: jsons[cid]
                });
            });
        }

        function add() {
            rows.push(row);
            var count = 1;
            var categoryid = $("#ordercategory0").val();
            var categoryname = $("#ordercategory0").find("option:selected").text();
            var ctype = $("#ordertype0").val();
            var str = '';
            str += '<tr id="record' + row + '">' +
                    ' <td>' + row + '</td> ' +
                    ' <td>' + categoryname + '</td> ' +
                    ' <td>' + ctype + '</td> ' +
                    ' <td><input type="text"  id="orderproductNum' + row + '" name="orderproductNum' + row + '" value="' + count + '" style="width:50%" /></td> ' +
                    ' <td><input type="button" value="删除" onclick="delet(record' + row + ',' + row + ')"/></td> ' +
                    ' <td><input type="hidden" name="product" value="' + row + '"/></td> ' +
                    ' </tr>';
            $("#table").append(str);
            row++;
        }

        function delet(str, str2) {
            rows.splice($.inArray(str2, rows), 1);
            $(str).remove();
        }

        function check() {
            var branch = $("#branch").val();
            if (branch == "") {
                alert("请选择仓库");
                return false;
            }


            if (rows.length < 1) {
                alert("没有记录可以提交");
                return false;
            }

            for (var i = 0; i < rows.length; i++) {
                var count = $("#orderproductNum" + rows[i]).val();
                if (count <= 0) {
                    alert("产品数量不能小于1");
                    return false;
                }
            }

            $("#submit").css("display", "none");

            return true;
        }

    </script>
</head>

<body>
<!--   头部开始   -->
<jsp:include flush="true" page="../head.jsp">
    <jsp:param name="dmsn" value=""/>
</jsp:include>

<!--   头部结束   -->

<div class="main">
    <div class="weizhi_head">现在位置：</div>
    <div class="main_r_tianjia">
        <ul>
            <li><a href="receipts.jsp">返回</a></li>
        </ul>
    </div>
    <div>
        <form action="receiptsAdd.jsp" method="post" onsubmit="return check()">
            <input type="hidden" name="action" value="add"/>
            出库单位：

            <select class="quyu" name="branchtype" id="branchtype">
                <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                <%
                    for (int i = 0; i < listb.size(); i++) {
                        BranchType lo = listb.get(i);
                %>
                <option value="<%=lo.getId()%>"><%=lo.getName()%>
                </option>
                <%
                    }
                %>
            </select>
            <select id="branch" name="branch">
                <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
            </select>
            <br/>
            入库单位：
            <div style="background-color:yellow;width:60%">
                <center>
                    <div id="branchmessage">入库单</div>
                </center>
                <br/>
                增加型号:
                <select class="category" name="ordercategory0" id="ordercategory0">
                    <%
                        for (int i = 0; i < categorylist.size(); i++) {
                            Category cate = categorylist.get(i);
                    %>
                    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getName()%>
                    </option>
                    <%
                        }
                    %>
                </select>

                <input type="text" name="ordertype0" id="ordertype0" class="cba"/>
                <input type="button" name="" value="+" onclick="add()"/>


                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="table">
                    <tr>

                        <td>序号</td>
                        <td>产品</td>
                        <td>产品型号</td>
                        <td>数量</td>
                        <td>删除</td>
                        <td></td>
                    </tr>

                </table>


                <input type="submit" id="submit" value="确认提 交"/>&nbsp;&nbsp;&nbsp;&nbsp;合计

            </div>


        </form>
    </div>

</div>


</body>
</html>
