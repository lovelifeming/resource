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

<table border="1">
    <tr>
        <td>卡号</td>
        <td>姓名</td>
        <td>性别</td>
        <td>办卡日期</td>
        <td>押金</td>
    </tr>
    <!-- 使用struts2标签库中的iterator将所有数据遍历循环显示出来 -->
    <s:iterator value="#allUser" status="bcs">
        <tr>
            <td><s:property value="cid"></s:property></td>
            <td><s:property value="name"></s:property></td>
            <td><s:property value="sex"></s:property></td>
            <td><s:date name="cardDate" format="yyyy年MM月dd日"></s:date></td>
            <td><s:property value="%{formatDouble(deposit)}"></s:property></td>
        </tr>
    </s:iterator>
    <!-- 判断查询出来等于0，就显示“没有查找到数据” -->
    <s:if test="allUser.size()==0">
        <tr>
            <td colspan="7">没有查找到数据</td>
        </tr>
    </s:if>
</table>
</body>
<%
    String path = request.getContextPath();//获取application的名称
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
</html>
