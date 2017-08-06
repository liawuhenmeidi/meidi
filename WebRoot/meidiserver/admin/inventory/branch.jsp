<%@ page language="java" import="java.util.*,category.*,utill.*,branch.*,branchtype.*,group.*,user.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%
    request.setCharacterEncoding("utf-8");
    User user = (User) session.getAttribute("user");

    String id = request.getParameter("id");

    BranchType branch = BranchTypeManager.getLocate(Integer.valueOf(id));

    List<Branch> list = BranchManager.getLocate(id);

    System.out.println("list.size()" + list.size());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>产品管理</title>
    <script type="text/javascript" src="../../js/jquery-1.7.2.min.js"></script>
    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css"/>

    <script type="text/javascript">

    </script>
</head>

<body>
<!--   头部开始   -->
<jsp:include flush="true" page="../head.jsp">
    <jsp:param name="dmsn" value=""/>
</jsp:include>

<!--   头部结束   -->

<!--       -->

<div class="weizhi_head">现在位置：<%=branch.getName()%>
</div>
<div class="main_r_tianjia">
    <ul>
        <li><a href="inventoryBranch.jsp?">返回</a></li>
    </ul>

</div>

<div class="table-list">
    <table width="100%" cellspacing="0">
        <thead>
        <tr>
            <th align="left" width="20">
                <input type="checkbox" value="" id="allselect" onclick="seletall(allselect)"></input></th>
            <th align="left">门店序列号</th>
            <th align="left">门店</th>
            <th align="left">查看库存</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Branch category = list.get(i);
                    //category.getId()
        %>
        <tr>
            <td align="left"><input type="checkbox" value="1" name="<%=category.getId() %>"></input></td>
            <td align="left"><%=i + 1 %>
            </td>
            <td align="left"><%=category.getLocateName() %>
            </td>
            <td align="left"><a href="inventory.jsp?id=<%=category.getId() %>">[查看]</a></td>
        </tr>
        <% }
        }%>
        </tbody>
    </table>

    <div class="btn">

        <!--  <input type="button" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>  -->

    </div>
    <div class="btn">
        <!--
            门店名称： <input type="text"  id="locate" name="locate" />
        <input type="button" onclick="changes()"  value="增加"/> </br>
         -->
        <input type="submit" class="button" name="dosubmit" value="删除" onclick="winconfirm()"></input>

    </div>
</div>


</body>
</html>
