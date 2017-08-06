<%@ page language="java" import="java.util.*,category.*,group.*,user.*,utill.*,product.*;" pageEncoding="UTF-8"
         contentType="text/html;charset=utf-8" %>
<%
    request.setCharacterEncoding("utf-8");
    User user = (User) session.getAttribute("user");

    List<Category> list = CategoryManager.getCategory(user, Category.sale);

    String action = request.getParameter("action");
    if ("add".equals(action)) {
        String productName = request.getParameter("name");
        if (!StringUtill.isNull(productName)) {
            //response.sendRedirect("product.jsp");
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>产品管理</title>

    <link rel="stylesheet" type="text/css" rev="stylesheet" href="../../style/css/bass.css"/>
    <script type="text/javascript">

        function check() {

            var name = $("#name").val();
            if (name == "" || name == null || name == "null") {
                alert("产品型号不能为空");
                return false;
            }
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
    <!--       -->
    <div class="">
        <div class="weizhi_head">现在位置：</div>
        <div class="main_r_tianjia">
        </div>
        <div>
            <form action="productAdd.jsp" method="post" onsubmit="return check()">
                <input type="hidden" name="method" value="add"/>


                选择仓库：
                <select class="category" name="ordercategory0" id="ordercategory0" style="width:95% ">

                    <%
                        for (int i = 0; i < list.size(); i++) {
                            Category cate = list.get(i);
                    %>
                    <option value="<%= cate.getId()%>" id="<%= cate.getId()%>"><%=cate.getName()%>
                    </option>
                    <%
                        }
                    %>

                </select>


                <input type="submit" value="提  交"/>


            </form>


        </div>


    </div>


</body>
</html>
