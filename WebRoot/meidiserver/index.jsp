<%@ page language="java"
         import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>

    <style type="text/css">
        body {
            background-color: silver;
        }

        .page_center {
            width: 600px;
            margin: 20px auto;
        }

        #nav {
            background-color: yellow;
            height: 120px;
        }

        #left {
            width: 120px;
            background-color: pink;
            position: absolute;

        }

        #middle {
            width: 360px;
            background-color: gray;
            margin-left: 120px;

        }

        #right {
            width: 120px;
            background-color: green;
            position: absolute;
            top: 0;
            right: 0;
        }

        #foot {
            background-color: blue;
        }

        #main {
            position: relative;
            background-color: green;
            background-image: url(5.gif);
            background-repeat: repeat-y;
            background-position: left;
        }
    </style>
    <script type="text/javascript">


    </script>
</head>

<body>
<div id="nav" class="page_center">
    000
</div>
<div id="main" class="page_center">
    <div id="left">
        111<br/><br/><br/><br/><br/><br/>
    </div>
    <div id="middle">
        222<br/><br/>主<br/><br/>
        <br/><br/>要<br/><br/>内<br/><br/><br/><br/><br/>容<br/><br/><br/><br/><br/>主<br/><br/><br/><br/>要<br/><br/>
    </div>
    <div id="right">
        333<br/><br/><br/><br/><br/><br/><br/>
    </div>
</div>
<div id="foot" class="page_center">
    444<br/><br/><br/><br/><br/><br/>
</div>
</body>
</html> 