<%--
  Created by IntelliJ IDEA.
  User: zengsm
  Date: 2017/12/7
  Time: 10:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<div>
    <h1>Happened some error!</h1>
    <span>查询失败！</span>
    <div>
        名字：<input value="resultJson.user.username"/> <br/>
        密码：<input value="resultJson.user.password"/> <br/>
        性别：<input value="resultJson.user.user_sex"/> <br/>
        编号：<input value="resultJson.user.user_no"/> <br/>
        生日：<input value="resultJson.user.user_birthday"/> <br/>
        班级：<input value="resultJson.user.user_class"/> <br/>
        <hr>
        <span>${resultJson}</span>
        <hr>
    </div>
</div>
</body>
</html>
