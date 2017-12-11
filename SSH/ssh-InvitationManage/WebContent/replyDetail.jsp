<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>回复列表</title>
<link rel="stylesheet" href="static/css/bootstrap.css">
<link rel="stylesheet" href="static/css/index.css">
</head>
<body>
	<h1 class="title"></h1>
	<div style="text-align:right;">
		<input id="id" type="hidden" value="<s:property value='viewId'/>">
		<a href="#" id="addReply">添加回复</a>
		<a href="#" id="back">返回帖子列表</a>
	</div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td>回复内容</td>
				<td>回复昵称</td>
				<td>发布时间</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="reply" value="replies">
				<tr>
					<td><s:property value="#reply.content"/> </td>
					<td><s:property value="#reply.author"/></td>
					<td><s:property value="#reply.createDate"/></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
<script type="text/javascript" src="static/js/jquery-2.1.4.js"></script>
<script type="text/javascript" src="static/js/replyDetail.js"></script>
</html>