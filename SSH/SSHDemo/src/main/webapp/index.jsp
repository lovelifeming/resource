<%--
  Created by IntelliJ IDEA.
  User: zengsm
  Date: 2017/12/6
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Test SSH Index</title>
</head>
<body>
<div>
    <form action="login" method="post">
        用户名 : <input type="text" name="username"/>
        <br/>
        密码 : <input type="password" name="password"/>
        <br/>
        <input type="submit" value="Login"/>
        <input type="reset" value="Reset"/>
    </form>
</div>

<span><%=request.getAttribute("message") == null ? "" : request.getAttribute("message")%><br></span>
</body>
<%
    String path = request.getContextPath();//获取application的名称
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    out.println("*********" + path);
    out.println("********" + basePath);
%>

</html>
