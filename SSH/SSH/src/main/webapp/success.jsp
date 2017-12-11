<%--
  Created by IntelliJ IDEA.
  User: zengsm
  Date: 2017/12/8
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String path = request.getContextPath();
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'query_success.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--    <link rel="stylesheet" type="text/css" href="styles.css">    -->

</head>

<body>
查询成功！ <br>
查询结果<br/>
名字 ：<s:property value="user.name"/> <br/>
密码：<s:property value="user.password"/> <br/>
邮箱：<s:property value="user.email"/> <br/>
住址：<s:property value="user.address"/> <br/>
</body>
</html>
