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
</head>

<body>
查询成功！ <br>
查询结果 <br/>
<div>
    <%--需要建立对应的界面JavaBean，后台传入对象实例--%>
    消息：<input value="resultJson.message"/><br/>
    名字：<input value="resultJson.user.username"/><br/>
    密码：<input value="resultJson.user.password"/> <br/>
    性别：<input value="resultJson.user.user_sex"/> <br/>
    编号：<input value="resultJson.user.user_no"/> <br/>
    生日：<input value="resultJson.user.user_birthday"/> <br/>
    班级：<input value="resultJson.user.user_class"/> <br/>
    <hr>
    <span>${resultJson}</span>
    <hr>
</div>
<%
    out.println(request.getAttribute("resultJson") + "<br/>");
    Enumeration en = request.getAttributeNames();
    while (en.hasMoreElements())
    {
        out.println(en.nextElement() + "<br/>");
    }
    out.println(response + "<br/>");
%>
<script type="text/javascript">
</script>
</body>
</html>
