<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加回复</title>
<style type="text/css">
	.area{
		width : 400px;
		height:200px;
		overflow-x: hidden;
		overflow-y: scroll;
	}
</style>
</head>
<body>
	<h1>添加回复</h1>
	<input id="replyId" type="hidden" value="<s:property value="replyId"/>" />
	<div>
		<span>回复内容：</span>
		<textarea class="area" id="reply"></textarea>
	</div>
	<div>
		<span>回复昵称：</span>
		<input type="text" id="nickname"/>
	</div>
	<div>
		<input type="button" value="提交" id="submit">
		<input type="button" value="返回">
	</div>
</body>
<script type="text/javascript" src="static/js/jquery-2.1.4.js"></script>
<script type="text/javascript" src="static/js/addReply.js"></script>
</html>