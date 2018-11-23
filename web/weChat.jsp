<%--
  Created by IntelliJ IDEA.
  User: Yancy_Peng
  Date: 2018/11/9
  Time: 11:16 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信授权测试</title>
</head>
<style type="text/css">
    .button
    {
        font-size:12px;
        text-align:center;
        padding:0px;
        vertical-align:middle ;
        line-height:22px;
        margin:0px;
        Height:26px;
        Width:60px;
    }
</style>
<body>
    <sf:form method="get" action="authorized">
        <div align="center">
            <input type="submit" value="新增授权" class="button"/>
        </div>
    </sf:form>
</body>
</html>
