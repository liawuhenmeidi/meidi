<%@ page language="java"
         import="java.util.*,utill.*,category.*,gift.*,orderPrint.*,order.*,user.*,orderproduct.*,group.*;"
         pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<!--html 代码-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>模拟excel表格</title>
    <style type="text/css">
        <!--
        .form th {
            background-color: #efebde;
            border-right-width: 1px;
            border-bottom-width: 1px;
            border-right-style: solid;
            border-bottom-style: solid;
            border-right-color: #7e7d68;
            border-bottom-color: #7e7d68;
            font-weight: normal;
            padding-top: 5px;
            cursor: url(Cursors/SizeE.cur);
        }

        .form td {
            border-right-width: 1px;
            border-bottom-width: 1px;
            border-top-style: none;
            border-right-style: solid;
            border-bottom-style: solid;
            border-left-style: none;
            border-right-color: #c0c0c0;
            border-bottom-color: #c0c0c0;
            cursor: url(Cursors/ColouredAero_ew.cur);
        }

        .cell {
            cursor: url(Cursors/ColouredAero_prec.cur);
        }

        .form table {
            border: 1px solid #7e7d68;
        }

        .form input {
            border-top-width: 0px;
            border-right-width: 0px;
            border-bottom-width: 0px;
            border-left-width: 0px;
            border-top-style: none;
            border-right-style: none;
            border-bottom-style: none;
            border-left-style: none;
            background-color: transparent;
        }

        -->
    </style>
</head>
<body>
<form action="" method="post" name="form1" class="form" id="form1">
    <table width="80%" border="0" cellpadding="0" cellspacing="0">
        <tbody>
        <tr>
            <th width="10%" height="10%" scope="col">&nbsp;</th>
            <th width="10%" scope="col">A</th>
            <th width="10%" scope="col">B</th>
            <th width="10%" scope="col">C</th>
            <th width="10%" scope="col">D</th>
            <th width="10%" scope="col">E</th>
            <th width="10%" scope="col">F</th>
            <th width="10%" scope="col">G</th>
            <th width="10%" scope="col">H</th>
            <th width="10%" scope="col">I</th>
            <th width="10%" scope="col">J</th>
        </tr>
        <tr>
            <th height="10%" scope="row">1</th>
            <td><input name="textfield112" type="text" class="cell" size="10"></td>
            <td><input name="textfield11" type="text" class="cell" size="10"></td>
            <td><input name="textfield52" type="text" class="cell" size="10"></td>
            <td><input name="textfield54" type="text" class="cell" size="10"></td>
            <td><input name="textfield56" type="text" class="cell" size="10"></td>
            <td><input name="textfield61" type="text" class="cell" size="10"></td>
            <td><input name="textfield69" type="text" class="cell" size="10"></td>
            <td><input name="textfield77" type="text" class="cell" size="10"></td>
            <td><input name="textfield85" type="text" class="cell" size="10"></td>
            <td><input name="textfield93" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">2</th>
            <td><input name="textfield2" type="text" class="cell" size="10"></td>
            <td><input name="textfield12" type="text" class="cell" size="10"></td>
            <td><input name="textfield53" type="text" class="cell" size="10"></td>
            <td><input name="textfield55" type="text" class="cell" size="10"></td>
            <td><input name="textfield57" type="text" class="cell" size="10"></td>
            <td><input name="textfield62" type="text" class="cell" size="10"></td>
            <td><input name="textfield70" type="text" class="cell" size="10"></td>
            <td><input name="textfield78" type="text" class="cell" size="10"></td>
            <td><input name="textfield86" type="text" class="cell" size="10"></td>
            <td><input name="textfield94" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">3</th>
            <td><input name="textfield3" type="text" class="cell" size="10"></td>
            <td><input name="textfield13" type="text" class="cell" size="10"></td>
            <td><input name="textfield28" type="text" class="cell" size="10"></td>
            <td><input name="textfield29" type="text" class="cell" size="10"></td>
            <td><input name="textfield58" type="text" class="cell" size="10"></td>
            <td><input name="textfield63" type="text" class="cell" size="10"></td>
            <td><input name="textfield71" type="text" class="cell" size="10"></td>
            <td><input name="textfield79" type="text" class="cell" size="10"></td>
            <td><input name="textfield87" type="text" class="cell" size="10"></td>
            <td><input name="textfield95" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">4</th>
            <td><input name="textfield4" type="text" class="cell" size="10"></td>
            <td><input name="textfield14" type="text" class="cell" size="10"></td>
            <td><input name="textfield27" type="text" class="cell" size="10"></td>
            <td><input name="textfield30" type="text" class="cell" size="10"></td>
            <td><input name="textfield59" type="text" class="cell" size="10"></td>
            <td><input name="textfield64" type="text" class="cell" size="10"></td>
            <td><input name="textfield72" type="text" class="cell" size="10"></td>
            <td><input name="textfield80" type="text" class="cell" size="10"></td>
            <td><input name="textfield88" type="text" class="cell" size="10"></td>
            <td><input name="textfield96" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">5</th>
            <td><input name="textfield5" type="text" class="cell" size="10"></td>
            <td><input name="textfield15" type="text" class="cell" size="10"></td>
            <td><input name="textfield26" type="text" class="cell" size="10"></td>
            <td><input name="textfield31" type="text" class="cell" size="10"></td>
            <td><input name="textfield60" type="text" class="cell" size="10"></td>
            <td><input name="textfield65" type="text" class="cell" size="10"></td>
            <td><input name="textfield73" type="text" class="cell" size="10"></td>
            <td><input name="textfield81" type="text" class="cell" size="10"></td>
            <td><input name="textfield89" type="text" class="cell" size="10"></td>
            <td><input name="textfield97" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">6</th>
            <td><input name="textfield6" type="text" class="cell" size="10"></td>
            <td><input name="textfield16" type="text" class="cell" size="10"></td>
            <td><input name="textfield25" type="text" class="cell" size="10"></td>
            <td><input name="textfield32" type="text" class="cell" size="10"></td>
            <td><input name="textfield51" type="text" class="cell" size="10"></td>
            <td><input name="textfield66" type="text" class="cell" size="10"></td>
            <td><input name="textfield74" type="text" class="cell" size="10"></td>
            <td><input name="textfield82" type="text" class="cell" size="10"></td>
            <td><input name="textfield90" type="text" class="cell" size="10"></td>
            <td><input name="textfield98" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">7</th>
            <td><input name="textfield7" type="text" class="cell" size="10"></td>
            <td><input name="textfield17" type="text" class="cell" size="10"></td>
            <td><input name="textfield24" type="text" class="cell" size="10"></td>
            <td><input name="textfield33" type="text" class="cell" size="10"></td>
            <td><input name="textfield50" type="text" class="cell" size="10"></td>
            <td><input name="textfield67" type="text" class="cell" size="10"></td>
            <td><input name="textfield75" type="text" class="cell" size="10"></td>
            <td><input name="textfield83" type="text" class="cell" size="10"></td>
            <td><input name="textfield91" type="text" class="cell" size="10"></td>
            <td><input name="textfield99" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">8</th>
            <td><input name="textfield8" type="text" class="cell" size="10"></td>
            <td><input name="textfield18" type="text" class="cell" size="10"></td>
            <td><input name="textfield23" type="text" class="cell" size="10"></td>
            <td><input name="textfield34" type="text" class="cell" size="10"></td>
            <td><input name="textfield49" type="text" class="cell" size="10"></td>
            <td><input name="textfield68" type="text" class="cell" size="10"></td>
            <td><input name="textfield76" type="text" class="cell" size="10"></td>
            <td><input name="textfield84" type="text" class="cell" size="10"></td>
            <td><input name="textfield92" type="text" class="cell" size="10"></td>
            <td><input name="textfield100" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">9</th>
            <td><input name="textfield9" type="text" class="cell" size="10"></td>
            <td><input name="textfield19" type="text" class="cell" size="10"></td>
            <td><input name="textfield22" type="text" class="cell" size="10"></td>
            <td><input name="textfield35" type="text" class="cell" size="10"></td>
            <td><input name="textfield48" type="text" class="cell" size="10"></td>
            <td><input name="textfield47" type="text" class="cell" size="10"></td>
            <td><input name="textfield46" type="text" class="cell" size="10"></td>
            <td><input name="textfield45" type="text" class="cell" size="10"></td>
            <td><input name="textfield44" type="text" class="cell" size="10"></td>
            <td><input name="textfield43" type="text" class="cell" size="10"></td>
        </tr>
        <tr>
            <th height="10%" scope="row">10</th>
            <td><input name="textfield10" type="text" class="cell" size="10"></td>
            <td><input name="textfield20" type="text" class="cell" size="10"></td>
            <td><input name="textfield21" type="text" class="cell" size="10"></td>
            <td><input name="textfield36" type="text" class="cell" size="10"></td>
            <td><input name="textfield37" type="text" class="cell" size="10"></td>
            <td><input name="textfield38" type="text" class="cell" size="10"></td>
            <td><input name="textfield39" type="text" class="cell" size="10"></td>
            <td><input name="textfield40" type="text" class="cell" size="10"></td>
            <td><input name="textfield41" type="text" class="cell" size="10"></td>
            <td><input name="textfield42" type="text" class="cell" size="10"></td>
        </tr>
        </tbody>
    </table>
    <input type="submit" name="Submit" value="提交">
    <input type="reset" name="Submit2" value="重置">
</form>
</body>
</html>